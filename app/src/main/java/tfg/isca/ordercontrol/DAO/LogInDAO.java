package tfg.isca.ordercontrol.DAO;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;

import tfg.isca.ordercontrol.ipAddress.ip;

public class LogInDAO {
    private String session_id = "";

    public String getSession_id(){
        return this.session_id;
    }
    public String getSessionId(String usuario, String contrasenya, Context context) throws InterruptedException {
// Instantiate the RequestQueue using Volley.newRequestQueue() method
        RequestQueue queue = Volley.newRequestQueue(context); // Pass your context here

        // Set the URL for the POST request
        String url = ip.IP+"/web/session/authenticate";

        JSONObject body = new JSONObject();
        try {
            body.put("jsonrpc", "2.0");
            body.put("params", new JSONObject().put("db","odoodb").put("login",usuario).put("password",contrasenya));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CountDownLatch latch = new CountDownLatch(1);

        // Create a JsonObjectRequest with the POST method
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response
                        Toast.makeText(context, "Correcto", Toast.LENGTH_SHORT).show();
                        latch.countDown();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response
                        Toast.makeText(context, "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                        //System.out.println(error);
                        latch.countDown();
                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // Retrieve cookies from the response headers
                Map<String, String> headers = response.headers;
                String cookies = headers.get("Set-Cookie");
                String[] sesion = cookies.split(";");
                session_id = sesion[0].substring(11);
                latch.countDown();

                // Pass the response data to the super class for further processing
                return super.parseNetworkResponse(response);
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        // Add the request to the RequestQueue
        queue.add(request);
        latch.await();

        return session_id;
    }
}
