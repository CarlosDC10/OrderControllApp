package tfg.isca.ordercontrol.Adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import tfg.isca.ordercontrol.Pojos.LineaPedido;
import tfg.isca.ordercontrol.Pojos.LineaPreparada;
import tfg.isca.ordercontrol.R;


public class LineaPreparadaAdapter extends ArrayAdapter<LineaPreparada> {
    public LineaPreparadaAdapter(@NonNull Context context, @NonNull ArrayList<LineaPreparada> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LineaPreparada linea = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_lineapedido,parent,false);
        }

        TextView lineaPedido = convertView.findViewById(R.id.SuperiorCard);
        TextView lote = convertView.findViewById(R.id.inferiorCard);

        if(linea.getCantidad() == 0){
            lineaPedido.setText(linea.getUnidad());
            lineaPedido.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
        }else {
            lineaPedido.setText(linea.getCantidad() + " " + linea.getUnidad());
            lote.setText("Lote:"+String.valueOf(linea.getLote()));
        }

        return convertView;
    }
}
