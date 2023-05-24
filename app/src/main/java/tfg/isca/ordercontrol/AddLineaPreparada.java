package tfg.isca.ordercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

import tfg.isca.ordercontrol.ipAddress.ip;

public class AddLineaPreparada extends AppCompatActivity {

    TextView cantidadNumber, loteNumber;
    Button anyadirCantidad, restarCantidad, anyadirLote, restarLote, guardar;

    static boolean fin = false;

    int cantidadActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_add_linea_preparada);

        cantidadNumber = findViewById(R.id.cantidadNumber);
        loteNumber = findViewById(R.id.loteNumber);

        cantidadActual = getIntent().getIntExtra("cantidadActual", 0);

        anyadirCantidad = findViewById(R.id.anyadirCantidad);
        anyadirCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(cantidadNumber.getText().toString());
                cantidad++;
                if ((cantidadActual + cantidad) <= getIntent().getIntExtra("cantidadMax", 9999)) {
                    cantidadNumber.setText(String.valueOf(cantidad));
                } else {
                    Toast.makeText(AddLineaPreparada.this, "Cantidad superior a la permitida", Toast.LENGTH_SHORT).show();
                    cantidad--;
                    cantidadNumber.setText(String.valueOf(cantidad));
                }
            }
        });
        restarCantidad = findViewById(R.id.restarCantidad);
        restarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(cantidadNumber.getText().toString());
                cantidad--;
                if (cantidad > 0)
                    cantidadNumber.setText(String.valueOf(cantidad));
                else {
                    Toast.makeText(AddLineaPreparada.this, "Cantida negativa", Toast.LENGTH_SHORT).show();
                    cantidad = 0;
                    cantidadNumber.setText(String.valueOf(cantidad));
                }
            }
        });
        anyadirLote = findViewById(R.id.anyadirLote);
        anyadirLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lote = Integer.parseInt(loteNumber.getText().toString());
                lote++;
                if (lote < 366) {
                    loteNumber.setText(String.valueOf(lote));
                } else {
                    Toast.makeText(AddLineaPreparada.this, "Lote superior a los dias de un año", Toast.LENGTH_SHORT).show();
                    lote = sacarLoteHoy();
                    cantidadNumber.setText(String.valueOf(lote));
                }
            }
        });
        restarLote = findViewById(R.id.restarLote);
        restarLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lote = Integer.parseInt(loteNumber.getText().toString());
                lote--;
                if (lote > 0) {
                    loteNumber.setText(String.valueOf(lote));
                } else {
                    Toast.makeText(AddLineaPreparada.this, "Lote inferior a 1", Toast.LENGTH_SHORT).show();
                    lote = sacarLoteHoy();
                    cantidadNumber.setText(String.valueOf(lote));
                }
            }
        });
        guardar = findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar(getIntent().getBooleanExtra("nuevo", true));
                finish();
            }
        });

        cantidadNumber.setText(String.valueOf(getIntent().getIntExtra("cantidad", 1)));
        loteNumber.setText(String.valueOf(getIntent().getIntExtra("lote", sacarLoteHoy())));

    }

    public int sacarLoteHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDate dia1enero = LocalDate.of(hoy.getYear() - 1, 12, 31);

        long days = ChronoUnit.DAYS.between(dia1enero, hoy);
        return (int) days;
    }

    public void guardar(boolean nuevo) {
        if (nuevo) {
            String url = ip.IP + "/app_pedidos/addLineaPreparada";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error response
                            Toast.makeText(AddLineaPreparada.this, "Algo ha ido mal creando la lineaprp", Toast.LENGTH_SHORT).show();
                            //System.out.println(error);
                        }
                    }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject body = new JSONObject();
                    try {
                        body.put("lineaPedido", getIntent().getIntExtra("idLineaPedido", 0));
                        body.put("lote", Integer.parseInt(loteNumber.getText().toString()));
                        body.put("cantidad", Integer.parseInt(cantidadNumber.getText().toString()));
                        body.put("tipoPaquete", getIntent().getIntExtra("idTipoPaquete", 0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return body.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };

            Volley.newRequestQueue(AddLineaPreparada.this).add(request);



            String url2 = ip.IP + "/app_pedidos/updateLineaPedido";
            StringRequest request2 = new StringRequest(Request.Method.PUT, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AddLineaPreparada.this, "Actualizada", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error response
                            Toast.makeText(AddLineaPreparada.this, "Algo ha ido mal actualizando la linea", Toast.LENGTH_SHORT).show();
                            //System.out.println(error);
                        }
                    }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject body = new JSONObject();
                    try {
                        body.put("id", getIntent().getIntExtra("idLineaPedido", 0));
                        body.put("cantidadActual",(getIntent().getIntExtra("cantidadActual", 9999)+Integer.parseInt(cantidadNumber.getText().toString())));
                        if(getIntent().getIntExtra("cantidadActual", 9999)+Integer.parseInt(cantidadNumber.getText().toString()) == getIntent().getIntExtra("cantidadMax",0)){
                            body.put("completada",true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return body.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };

            Volley.newRequestQueue(this).add(request2);
        }
    }
}