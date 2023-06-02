package tfg.isca.ordercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tfg.isca.ordercontrol.DAO.LogInDAO;

public class MainActivity extends AppCompatActivity {

    private Button botonEntrar;
    private EditText usuario, contrasenya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        botonEntrar = findViewById(R.id.button);
        usuario = findViewById(R.id.editTextTextPersonName);
        contrasenya = findViewById(R.id.editTextTextPassword);
        botonEntrar.setBackgroundColor(Color.parseColor("#0041B4"));

        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogInDAO login = new LogInDAO();
                String user = usuario.getText().toString();
                String pass = contrasenya.getText().toString();
                try {
                    login.getSessionId(user,pass, MainActivity.this);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(login.getSession_id().isEmpty()){
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, listaPedidos.class);
                    intent.putExtra("session_id",login.getSession_id());
                    startActivity(intent);
                }
            }
        });
    }
}