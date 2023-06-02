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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tfg.isca.ordercontrol.Adapters.LineaPedidoAdapter;
import tfg.isca.ordercontrol.Pojos.LineaPedido;
import tfg.isca.ordercontrol.ipAddress.ip;

public class detallePedido extends AppCompatActivity {

    private ArrayList<LineaPedido> lineaPedidosCards;
    private LineaPedidoAdapter adapter;
    private GridView gridView;
    private TextView clienteYfecha, muelle, estado;
    private boolean primero = true;
    private ArrayList<Integer> idsLineas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_detalle_pedido);

        clienteYfecha = findViewById(R.id.clienteYfecha);
        muelle = findViewById(R.id.muelle);
        estado = findViewById(R.id.estadoDetallePedido);

        Object[] ListaIdLineas = (Object[]) getIntent().getSerializableExtra("ListaIdLineas");
        for (int i = 0; i < ListaIdLineas.length; i++) {
            idsLineas.add(Integer.valueOf(String.valueOf(ListaIdLineas[i])));
        }

        gridView = findViewById(R.id.gridViewLineaPedido);

        lineaPedidosCards = new ArrayList<>();
        getAllLineas();

        clienteYfecha.setText(getIntent().getStringExtra("cliente") + " (" + getIntent().getStringExtra("fechaEntrega") + ")");
        muelle.setText(getIntent().getStringExtra("muelle"));
        estado.setText(getIntent().getStringExtra("estado"));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LineaPedido cardItem = lineaPedidosCards.get(position);
                Intent intent = new Intent(detallePedido.this, DetalleLineaPedido.class);
                intent.putExtra("cantidad", cardItem.getCantidad());
                intent.putExtra("cantidadActual", cardItem.getCantidadActual());
                intent.putExtra("tipoPaquete", cardItem.getTipoPaquete());
                intent.putExtra("completada", cardItem.isCompleatada());
                intent.putExtra("lineasPreparadaIds", cardItem.getLineasPreparadas().toArray());
                intent.putExtra("unidad", cardItem.getUnidad());
                intent.putExtra("id", cardItem.getId());
                intent.putExtra("session_id", getIntent().getStringExtra("session_id"));
                startActivity(intent);
            }
        });
    }

    private void getAllLineas() {
        String url = ip.IP + "/app_pedidos/getLineaPedido";

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonLineaPedido = array.getJSONObject(i);
                                if (idsLineas.contains(jsonLineaPedido.getInt("id"))) {
                                    LineaPedido linea = new LineaPedido(jsonLineaPedido.getInt("id"),
                                            jsonLineaPedido.getInt("cantidad"), null, jsonLineaPedido.getBoolean("completada"),
                                            getIntent().getStringExtra("unidad"), null, jsonLineaPedido.getInt("cantidadActual"));
                                    linea.setTipoPaquete(String.valueOf(((JSONArray) (jsonLineaPedido.get("tipoPaquete"))).get(1)));

                                    List<Integer> lineasPrep = new ArrayList<>();
                                    for (int j = 0; j < ((JSONArray) (jsonLineaPedido.get("lineasPreparadas"))).length(); j++) {
                                        lineasPrep.add(Integer.valueOf(((JSONArray) (jsonLineaPedido.get("lineasPreparadas"))).get(j).toString()));
                                    }
                                    linea.setLineasPreparadas(lineasPrep);

                                    lineaPedidosCards.add(linea);
                                }
                            }
                            adapter = new LineaPedidoAdapter(detallePedido.this, lineaPedidosCards);

                            gridView.setAdapter(adapter);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(detallePedido.this, "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                    }
                }) /*{
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.isEmpty()) {
                    headers = new HashMap<>();
                }
                headers.put("Cookie", "session_id=" + getIntent().getStringExtra("session_id"));
                return headers;
            }
        }*/;


        Volley.newRequestQueue(detallePedido.this).add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (primero) {
            primero = false;
        } else {
            lineaPedidosCards = new ArrayList<>();
            String url = ip.IP + "/app_pedidos/getPedido/" + getIntent().getIntExtra("id", 0);

            StringRequest stringRequest = new StringRequest
                    (Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray array = jsonObject.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonPedido = array.getJSONObject(i);

                                    ArrayList<Integer> lineasPedido = new ArrayList<>();
                                    for (int j = 0; j < ((JSONArray) (jsonPedido.get("lineas"))).length(); j++) {
                                        lineasPedido.add(Integer.valueOf(((JSONArray) (jsonPedido.get("lineas"))).get(j).toString()));
                                    }
                                    if(jsonPedido.get("estado").toString().equals("P")){
                                        estado.setText("En producción");
                                    }else if(jsonPedido.get("estado").toString().equals("C")){
                                        estado.setText("Completada");
                                    }

                                    idsLineas = lineasPedido;

                                    getAllLineas();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(detallePedido.this, "Algo ha ido mal actualizando la linea", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(detallePedido.this).add(stringRequest);
        }
    }
}
