package tfg.isca.ordercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tfg.isca.ordercontrol.Adapters.PedidoAdapter;
import tfg.isca.ordercontrol.Pojos.LineaPedido;
import tfg.isca.ordercontrol.Pojos.LineaPreparada;
import tfg.isca.ordercontrol.Pojos.Pedido;
import tfg.isca.ordercontrol.ipAddress.ip;

public class listaPedidos extends AppCompatActivity {

    private ArrayList<Pedido> pedidosCards;
    private PedidoAdapter adapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_lista_pedidos);

        pedidosCards = new ArrayList<Pedido>();

        /*pedidosCards.add(new Pedido(
                3, "Consum", "(10-05-2023)", "Puerta trasera", "En produccion", "caja(s)",
                Arrays.asList(
                        new LineaPedido(
                                20, "400", false,
                                "caja(s)", Arrays.asList(
                                new LineaPreparada(
                                        9, 119, "400", "cajas(s)"
                                ),
                                new LineaPreparada(
                                        9, 120, "400", "cajas(s)"
                                )
                        )
                        ), new LineaPedido(
                                30, "240", false,
                                "caja(s)", Arrays.asList(
                                new LineaPreparada(
                                        10, 119, "240", "cajas(s)"
                                ),
                                new LineaPreparada(
                                        9, 120, "240", "cajas(s)"
                                )
                        )
                        )
                )
        ));
        pedidosCards.add(new Pedido(
                2, "Ricardo", "(12-05-2023)", "Puerta delantera", "En produccion", "caja(s)",
                Arrays.asList(
                        new LineaPedido(
                                20, "400", false,
                                "caja(s)", Arrays.asList(
                                new LineaPreparada(
                                        9, 119, "400", "cajas(s)"
                                ),
                                new LineaPreparada(
                                        9, 120, "400", "cajas(s)"
                                )
                        )
                        ), new LineaPedido(
                                30, "240", false,
                                "caja(s)", Arrays.asList(
                                new LineaPreparada(
                                        10, 119, "240", "cajas(s)"
                                ),
                                new LineaPreparada(
                                        9, 120, "240", "cajas(s)"
                                )
                        )
                        )
                )
        ));
        pedidosCards.add(new Pedido(
                1, "ValenFoods", "(15-05-2023)", "Puerta delantera", "En produccion", "caja(s)",
                Arrays.asList(
                        new LineaPedido(
                                20, "400", false,
                                "caja(s)", Arrays.asList(
                                new LineaPreparada(
                                        9, 119, "400", "cajas(s)"
                                ),
                                new LineaPreparada(
                                        9, 120, "400", "cajas(s)"
                                )
                        )
                        ), new LineaPedido(
                                30, "240", false,
                                "caja(s)", Arrays.asList(
                                new LineaPreparada(
                                        10, 119, "240", "cajas(s)"
                                ),
                                new LineaPreparada(
                                        9, 120, "240", "cajas(s)"
                                )
                        )
                        )
                )
        ));*/
        getAllPedidos();
        System.out.println(pedidosCards);

        gridView = findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pedido cardItem = pedidosCards.get(position);
                Intent intent = new Intent(listaPedidos.this, detallePedido.class);
                intent.putExtra("ListaIdLineas",cardItem.getLineasPedido().toArray());
                intent.putExtra("cliente", cardItem.getCliente());
                intent.putExtra("fechaEntrega", cardItem.getFechaEntrega());
                intent.putExtra("muelle", cardItem.getMuelle());
                intent.putExtra("estado", cardItem.getEstado());
                intent.putExtra("unidad", cardItem.getUnidad());
                intent.putExtra("id",cardItem.getId());
                startActivity(intent);
            }
        });
    }

    private void getAllPedidos() {
        String url = ip.IP+"/app_pedidos/getPedido";

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonPedido = array.getJSONObject(i);
                                if (jsonPedido.getString("estado").equals("P")) {
                                    Pedido pedido = new Pedido(jsonPedido.getInt("id"), null,
                                            jsonPedido.getString("fechaEntrega"), null, "En produccion", null, null);
                                    pedido.setCliente(String.valueOf(((JSONArray) (jsonPedido.get("cliente"))).get(1)));
                                    List<Integer> lineas = new ArrayList<>();
                                    for(int j = 0; j<((JSONArray)(jsonPedido.get("lineas"))).length();j++){
                                        lineas.add(Integer.valueOf(((JSONArray)(jsonPedido.get("lineas"))).get(j).toString()));
                                    }
                                    pedido.setLineasPedido(lineas);

                                    if (jsonPedido.getString("muelle").equals("P")) {
                                        pedido.setMuelle("Puerta principal");
                                    } else if (jsonPedido.getString("muelle").equals("T")) {
                                        pedido.setMuelle("Puerta trasera");
                                    }

                                    if (jsonPedido.getString("unidad").equals("C")) {
                                        pedido.setUnidad("caja(s)");
                                    } else if (jsonPedido.getString("unidad").equals("P")) {
                                        pedido.setUnidad("palet(s)");
                                    }else if (jsonPedido.getString("unidad").equals("B")) {
                                        pedido.setUnidad("bolsa(s)");
                                    }

                                    pedidosCards.add(pedido);
                                }
                            }
                            adapter = new PedidoAdapter(listaPedidos.this, pedidosCards);

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

        Volley.newRequestQueue(listaPedidos.this).add(stringRequest);
    }
}