package tfg.isca.ordercontrol.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import tfg.isca.ordercontrol.Pojos.LineaPedido;
import tfg.isca.ordercontrol.Pojos.Pedido;
import tfg.isca.ordercontrol.R;

public class LineaPedidoAdapter extends ArrayAdapter<LineaPedido> {
    public LineaPedidoAdapter(@NonNull Context context, @NonNull ArrayList<LineaPedido> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LineaPedido linea = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_lineapedido,parent,false);
        }

        TextView mensaje = convertView.findViewById(R.id.SuperiorCard);
        TextView estado = convertView.findViewById(R.id.inferiorCard);

        mensaje.setText(linea.getCantidad()+" "+linea.getUnidad()+" de "+linea.getTipoPaquete());
        if(linea.isCompleatada()){
            estado.setText("Compleatada");
        }else{
            estado.setText("No compleatada");
        }

        return convertView;
    }
}
