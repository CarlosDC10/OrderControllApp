package tfg.isca.ordercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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
import tfg.isca.ordercontrol.Adapters.PedidoAdapter;
import tfg.isca.ordercontrol.Pojos.LineaPedido;
import tfg.isca.ordercontrol.Pojos.Pedido;

public class detallePedido extends AppCompatActivity {

    private ArrayList<LineaPedido> lineaPedidosCards;
    private LineaPedidoAdapter adapter;
    private GridView gridView;
    private TextView clienteYfecha, muelle, estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_detalle_pedido);

        clienteYfecha = findViewById(R.id.clienteYfecha);
        muelle = findViewById(R.id.muelle);
        estado = findViewById(R.id.estadoDetallePedido);


        gridView = findViewById(R.id.gridViewLineaPedido);

        lineaPedidosCards = new ArrayList<>();
        /*LineaPedido[] provisional = (LineaPedido[]) getIntent().getSerializableExtra("lineasPedido");
        for(LineaPedido linea : provisional){
            lineaPedidosCards.add(linea);
        }*/
        getAllLineas();

        clienteYfecha.setText(getIntent().getStringExtra("cliente")+" ("+getIntent().getStringExtra("fechaEntrega")+")");
        muelle.setText(getIntent().getStringExtra("muelle"));
        estado.setText(getIntent().getStringExtra("estado"));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LineaPedido cardItem = lineaPedidosCards.get(position);
                Intent intent = new Intent(detallePedido.this, DetalleLineaPedido.class);
                intent.putExtra("cantidad", cardItem.getCantidad());
                intent.putExtra("tipoPaquete", cardItem.getTipoPaquete());
                intent.putExtra("completada", cardItem.isCompleatada());
                intent.putExtra("lineasPreparada", cardItem.getLineasPreparadas().toArray());
                intent.putExtra("unidad", cardItem.getUnidad());
                startActivity(intent);
            }
        });
    }

    private void getAllLineas() {
        ArrayList<Integer> ids = new ArrayList<>();
        Object[] ListaIdLineas = (Object[]) getIntent().getSerializableExtra("ListaIdLineas");
        for(int i = 0; i<ListaIdLineas.length;i++){
            ids.add(Integer.valueOf(String.valueOf(ListaIdLineas[i])));
        }
        String url = "http://192.168.122.62:8069/app_pedidos/getLineaPedido";

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonPedido = array.getJSONObject(i);
                                if (ids.contains(jsonPedido.getInt("id"))) {
                                    LineaPedido linea = new LineaPedido(jsonPedido.getInt("id"),
                                            jsonPedido.getInt("cantidad"),null,jsonPedido.getBoolean("completada"),
                                            getIntent().getStringExtra("unidad"),null);
                                    linea.setTipoPaquete(String.valueOf(((JSONArray) (jsonPedido.get("tipoPaquete"))).get(1)));

                                    List<Integer> lineasPrep = new ArrayList<>();
                                    for(int j = 0; j<((JSONArray)(jsonPedido.get("lineasPreparadas"))).length();j++){
                                        lineasPrep.add(Integer.valueOf(((JSONArray)(jsonPedido.get("lineasPreparadas"))).get(j).toString()));
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

                    }
                });

        Volley.newRequestQueue(detallePedido.this).add(stringRequest);
    }
}