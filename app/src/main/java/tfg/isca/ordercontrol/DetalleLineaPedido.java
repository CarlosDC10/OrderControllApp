package tfg.isca.ordercontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.isca.ordercontrol.Adapters.LineaPreparadaAdapter;
import tfg.isca.ordercontrol.Pojos.LineaPedido;
import tfg.isca.ordercontrol.Pojos.LineaPreparada;

public class DetalleLineaPedido extends AppCompatActivity {

    private ArrayList<LineaPreparada> lineaPreparadaCards;
    private LineaPreparadaAdapter adapter;
    private GridView gridView;
    private TextView lineaPedido, estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_detalle_linea_pedido);

        lineaPedido = findViewById(R.id.lineaPedido);
        estado = findViewById(R.id.estadoLineaPreparada);

        gridView = findViewById(R.id.gridViewLineaPreparada);

        lineaPreparadaCards = new ArrayList<>();
        LineaPreparada[] provisional = (LineaPreparada[]) getIntent().getSerializableExtra("lineasPreparada");
        for(LineaPreparada linea : provisional){
            lineaPreparadaCards.add(linea);
        }
        lineaPreparadaCards.add(new LineaPreparada(0,0,null,"+"));

        adapter = new LineaPreparadaAdapter(this,lineaPreparadaCards);
        gridView.setAdapter(adapter);

        lineaPedido.setText(getIntent().getIntExtra("cantidad",9999)+" "+getIntent().getStringExtra("unidad")+" de "+getIntent().getStringExtra("tipoPaquete"));
        if(getIntent().getBooleanExtra("compleatada",false)){
            estado.setText("Completada");
        }else{
            estado.setText("No completada");
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LineaPreparada cardItem = lineaPreparadaCards.get(position);
                Intent intent = new Intent(DetalleLineaPedido.this, AddLineaPreparada.class);
                intent.putExtra("cantidad", cardItem.getCantidad());
                intent.putExtra("tipoPaquete", cardItem.getTipoPaquete());
                intent.putExtra("lote", cardItem.getLote());
                intent.putExtra("unidad", cardItem.getUnidad());
                startActivity(intent);
            }
        });
    }
}