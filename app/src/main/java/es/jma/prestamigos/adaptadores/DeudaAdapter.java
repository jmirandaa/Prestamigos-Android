package es.jma.prestamigos.adaptadores;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.jma.prestamigos.R;
import es.jma.prestamigos.dominio.Deuda;

/**
 * Created by tulon on 3/02/17.
 */

public class DeudaAdapter extends RecyclerView.Adapter<DeudaAdapter.DeudaViewHolder> {

    private List<Deuda> deudas;

    public DeudaAdapter(List<Deuda> deudas)
    {
        this.deudas = deudas;
    }

    @Override
    public DeudaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_deuda, parent, false);
        DeudaViewHolder dvh = new DeudaViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DeudaViewHolder holder, int position) {
        holder.nombre.setText(deudas.get(position).getUsuarioDestino().getNombre()+" "+deudas.get(position).getUsuarioDestino().getApellidos());
        holder.fecha.setText("21/01/2016");
        holder.concepto.setText(deudas.get(position).getConcepto());
        holder.cantidad.setText(deudas.get(position).getCantidad()+"€");
    }

    @Override
    public int getItemCount() {
        return deudas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class DeudaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nombre;
        TextView fecha;
        TextView concepto;
        TextView cantidad;
        ImageView personPhoto;

        DeudaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view_deuda);
            nombre = (TextView)itemView.findViewById(R.id.deuda_nombre);
            fecha = (TextView)itemView.findViewById(R.id.deuda_fecha);
            concepto = (TextView)itemView.findViewById(R.id.deuda_concepto);
            cantidad = (TextView)itemView.findViewById(R.id.deuda_cantidad);
            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }
}
