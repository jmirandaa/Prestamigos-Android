package es.jma.prestamigos.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.jma.prestamigos.DetallesAmigoActivity;
import es.jma.prestamigos.NuevaDeudaActivity;
import es.jma.prestamigos.R;
import es.jma.prestamigos.dominio.Usuario;

/**
 * Created by tulon on 3/02/17.
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigosViewHolder> {
    private AmigosAdapter instance;
    private List<Usuario> amigos;

    public AmigosAdapter(List<Usuario> amigos)
    {
        this.instance = this;
        this.amigos = amigos;
    }

    @Override
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element_amigos, parent, false);
        AmigosViewHolder dvh = new AmigosViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(AmigosViewHolder holder, int position) {
        holder.nombre.setText(amigos.get(position).getNombre()+" "+amigos.get(position).getApellidos());
        //holder.cantidad.setText(deudas.get(position).getCantidad()+"€");
    }

    @Override
    public int getItemCount() {
        return amigos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class AmigosViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        ImageView personPhoto;

        AmigosViewHolder(final View itemView) {
            super(itemView);
            nombre = (TextView)itemView.findViewById(R.id.amigos_nombre_amigo);
            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);

            //Evento tocar elemento
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  int posicion = getAdapterPosition();

                    //Contexto
                    Context context = itemView.getContext();

                    //Entrar en detalles
                    Intent intent = new Intent(context, DetallesAmigoActivity.class);
                    context.startActivity(intent);
                }
            });

        }

    }
}
