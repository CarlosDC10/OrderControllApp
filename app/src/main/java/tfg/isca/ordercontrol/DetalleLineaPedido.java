package tfg.isca.ordercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tfg.isca.ordercontrol.Adapters.LineaPedidoAdapter;
import tfg.isca.ordercontrol.Adapters.LineaPreparadaAdapter;
import tfg.isca.ordercontrol.Pojos.LineaPedido;
import tfg.isca.ordercontrol.Pojos.LineaPreparada;
import tfg.isca.ordercontrol.ipAddress.ip;

public class DetalleLineaPedido extends AppCompatActivity {

    private ArrayList<LineaPreparada> lineaPreparadaCards;
    private LineaPreparadaAdapter adapter;
    private GridView gridView;
    private TextView lineaPedido, estado, cantidadActualText;
    public ArrayList<Integer> ids = new ArrayList<>();
    private boolean primero = true, editable;
    public int cantidadActual = 0;

    public LineaPedido lineaPadre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_detalle_linea_pedido);

        lineaPedido = findViewById(R.id.lineaPedido);
        estado = findViewById(R.id.estadoLineaPreparada);
        cantidadActualText = findViewById(R.id.cantidadActual);

        gridView = findViewById(R.id.gridViewLineaPreparada);

        cantidadActual = getIntent().getIntExtra("cantidadActual", 9999);

        Object[] ListaIdLineas = (Object[]) getIntent().getSerializableExtra("lineasPreparadaIds");
        for (int i = 0; i < ListaIdLineas.length; i++) {
            ids.add(Integer.valueOf(String.valueOf(ListaIdLineas[i])));
        }

        lineaPadre = new LineaPedido(getIntent().getIntExtra("id", 9999), getIntent().getIntExtra("cantidad", 9999), getIntent().getStringExtra("tipoPaquete"), getIntent().getBooleanExtra("completada", false), getIntent().getStringExtra("unidad"), ids, cantidadActual);

        lineaPreparadaCards = new ArrayList<>();

        lineaPedido.setText(lineaPadre.getCantidad() + " " + lineaPadre.getUnidad() + " de " + lineaPadre.getTipoPaquete());
        cantidadActualText.setText(" Cantidad actual: " + lineaPadre.getCantidadActual());
        if (lineaPadre.isCompleatada()) {
            estado.setText("Completada");
            editable = false;
        } else {
            estado.setText("No completada");
            editable = true;
        }

        getAllLineas();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (editable) {
                    LineaPreparada cardItem = lineaPreparadaCards.get(position);
                    Intent intent = new Intent(DetalleLineaPedido.this, AddLineaPreparada.class);

                    if (cardItem.getCantidad() != 0) {
                        intent.putExtra("cantidad", cardItem.getCantidad());
                        intent.putExtra("nuevo", false);
                    } else {
                        intent.putExtra("nuevo", true);

                    }
                    intent.putExtra("tipoPaquete", cardItem.getTipoPaquete());
                    intent.putExtra("idTipoPaquete", cardItem.getIdTipoPaquete());
                    if (cardItem.getLote() != 0)
                        intent.putExtra("lote", cardItem.getLote());
                    intent.putExtra("idLineaPedido", getIntent().getIntExtra("id", 0));
                    intent.putExtra("unidad", cardItem.getUnidad());
                    intent.putExtra("cantidadMax", getIntent().getIntExtra("cantidad", 9999));
                    intent.putExtra("cantidadActual", cantidadActual);
                    intent.putExtra("idLineaPreparada",cardItem.getId());
                    intent.putExtra("idsOtrasLineas", getIntent().getSerializableExtra("lineasPreparadaIds"));

                    startActivity(intent);
                } else {
                    Toast.makeText(DetalleLineaPedido.this, "Linea completada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getAllLineas() {
        String url = ip.IP + "/app_pedidos/getLineaPreparada";

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonlineaPreparada = array.getJSONObject(i);
                                if (lineaPadre.getLineasPreparadas().contains(jsonlineaPreparada.getInt("id"))) {
                                    LineaPreparada linea = new LineaPreparada(jsonlineaPreparada.getInt("id"),
                                            jsonlineaPreparada.getInt("cantidad"), jsonlineaPreparada.getInt("lote"), null, 0, getIntent().getStringExtra("unidad"));
                                    linea.setTipoPaquete(String.valueOf(((JSONArray) (jsonlineaPreparada.get("tipoPaquete"))).get(1)));
                                    linea.setTipoPaquete(String.valueOf(((JSONArray) (jsonlineaPreparada.get("tipoPaquete"))).get(0)));

                                    lineaPreparadaCards.add(linea);
                                }
                            }
                            LineaPreparada nuevaLinea = new LineaPreparada(0, 0, 0, null, 0, "+");
                            if (!lineaPreparadaCards.contains(nuevaLinea))
                                lineaPreparadaCards.add(nuevaLinea);
                            adapter = new LineaPreparadaAdapter(DetalleLineaPedido.this, lineaPreparadaCards);
                            gridView.setAdapter(adapter);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetalleLineaPedido.this, "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(DetalleLineaPedido.this).add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (primero) {
            primero = false;
        } else {
            lineaPreparadaCards = new ArrayList<>();
            String url = ip.IP + "/app_pedidos/getLineaPedido/" + lineaPadre.getId();

            StringRequest stringRequest = new StringRequest
                    (Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray array = jsonObject.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonLineaPedido = array.getJSONObject(i);
                                    LineaPedido linea = new LineaPedido(jsonLineaPedido.getInt("id"),
                                            jsonLineaPedido.getInt("cantidad"), null, jsonLineaPedido.getBoolean("completada"),
                                            getIntent().getStringExtra("unidad"), null, jsonLineaPedido.getInt("cantidadActual"));
                                    linea.setTipoPaquete(String.valueOf(((JSONArray) (jsonLineaPedido.get("tipoPaquete"))).get(1)));

                                    List<Integer> lineasPrep = new ArrayList<>();
                                    for (int j = 0; j < ((JSONArray) (jsonLineaPedido.get("lineasPreparadas"))).length(); j++) {
                                        lineasPrep.add(Integer.valueOf(((JSONArray) (jsonLineaPedido.get("lineasPreparadas"))).get(j).toString()));
                                    }
                                    linea.setLineasPreparadas(lineasPrep);

                                    cantidadActual = jsonLineaPedido.getInt("cantidadActual");
                                    lineaPadre = linea;

                                    lineaPedido.setText(lineaPadre.getCantidad() + " " + lineaPadre.getUnidad() + " de " + lineaPadre.getTipoPaquete());
                                    cantidadActualText.setText(" Cantidad actual: " + lineaPadre.getCantidadActual());
                                    if (lineaPadre.isCompleatada()) {
                                        estado.setText("Completada");
                                        editable = false;
                                    } else {
                                        estado.setText("No completada");
                                        editable = true;
                                    }
                                    getAllLineas();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DetalleLineaPedido.this, "Algo ha ido mal actualizando la linea", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(DetalleLineaPedido.this).add(stringRequest);
        }
    }
}