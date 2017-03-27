package es.jma.prestamigos.adaptadores;

import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_NOMBRE;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_APELLIDOS;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_EMAIL;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_ID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.jma.prestamigos.DetallesAmigoActivity;
import es.jma.prestamigos.NuevaDeudaActivity;
import es.jma.prestamigos.PrincipalActivity;
import es.jma.prestamigos.R;
import es.jma.prestamigos.constantes.KReqCode;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.navegacion.BaseFragment;

/**
 * Adapter para la lista de amigos
 * Created by jmiranda on 3/02/17.
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigosViewHolder> {
    private AmigosAdapter instance;

    public List<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Usuario> amigos) {
        this.amigos = amigos;
    }

    private List<Usuario> amigos;
    private BaseFragment fragment;

    public AmigosAdapter(List<Usuario> amigos, BaseFragment fragment)
    {
        this.instance = this;
        this.amigos = amigos;
        this.fragment = fragment;
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
                    //Recuperar datos del amigo
                    int posicion = getAdapterPosition();
                    Usuario usuario = amigos.get(posicion);

                    //Entrar en detalles
                    Bundle extra = new Bundle();
                    extra.putLong(BUNDLE_AMIGO_ID, usuario.getId());
                    extra.putString(BUNDLE_AMIGO_NOMBRE, usuario.getNombre());
                    extra.putString(BUNDLE_AMIGO_APELLIDOS, usuario.getApellidos());
                    extra.putString(BUNDLE_AMIGO_EMAIL, usuario.getEmail());
                    fragment.start(DetallesAmigoActivity.class, extra, KReqCode.REQ_CODE_BORRAR_AMIGO);
                }
            });

        }

    }
}
