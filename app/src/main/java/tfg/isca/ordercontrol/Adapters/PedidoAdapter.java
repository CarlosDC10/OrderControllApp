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

import tfg.isca.ordercontrol.Pojos.Pedido;
import tfg.isca.ordercontrol.R;

public class PedidoAdapter extends ArrayAdapter<Pedido> {
    public PedidoAdapter(@NonNull Context context, @NonNull ArrayList<Pedido> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Pedido pedido = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_pedido,parent,false);
        }

        TextView cliente = convertView.findViewById(R.id.clienteCard);
        TextView fecha = convertView.findViewById(R.id.fechaCard);
        TextView muelle = convertView.findViewById(R.id.muelleCard);

        cliente.setText(pedido.getCliente());
        fecha.setText(pedido.getFechaEntrega());
        muelle.setText(pedido.getMuelle());

        return convertView;
    }
}
