package tfg.isca.ordercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AddLineaPreparada extends AppCompatActivity {

    TextView cantidadNumber, loteNumber;
    Button anyadirCantidad, restarCantidad, anyadirLote, restarLote, guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_add_linea_preparada);

        cantidadNumber = findViewById(R.id.cantidadNumber);
        loteNumber = findViewById(R.id.loteNumber);

        anyadirCantidad = findViewById(R.id.anyadirCantidad);
        restarCantidad = findViewById(R.id.restarCantidad);
        anyadirLote = findViewById(R.id.anyadirLote);
        restarCantidad = findViewById(R.id.restarLote);

    }
}