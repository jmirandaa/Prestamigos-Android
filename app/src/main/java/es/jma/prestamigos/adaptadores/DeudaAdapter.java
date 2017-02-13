package es.jma.prestamigos.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.jma.prestamigos.DetallesDeudaActivity;
import es.jma.prestamigos.R;
import es.jma.prestamigos.constantes.KAccion;
import es.jma.prestamigos.dominio.Deuda;

/**
 * Created by tulon on 3/02/17.
 */

public class DeudaAdapter extends RecyclerView.Adapter<DeudaAdapter.DeudaViewHolder> {
    private DeudaAdapter instance;
    private List<Deuda> deudas;

    public DeudaAdapter(List<Deuda> deudas)
    {
        this.instance = this;
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

    public class DeudaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nombre;
        TextView fecha;
        TextView concepto;
        TextView cantidad;
        Button accion1, accion2;
        ImageView personPhoto;

        DeudaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view_deuda);
            nombre = (TextView)itemView.findViewById(R.id.deuda_nombre);
            fecha = (TextView)itemView.findViewById(R.id.deuda_fecha);
            concepto = (TextView)itemView.findViewById(R.id.deuda_concepto);
            cantidad = (TextView)itemView.findViewById(R.id.deuda_cantidad);
            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            boolean saldada = false;

            //Acción de los botones
            accion1 = (Button)itemView.findViewById(R.id.deuda_accion1);
            accion2 = (Button)itemView.findViewById(R.id.deuda_accion2);

            accion1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Abrir diálogo
                    abrirDialogoCantidad(v, KAccion.ACCION_SALDAR);
                }
            });

            accion2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Abrir diálogo
                    abrirDialogoCantidad(v, KAccion.ACCION_AUMENTAR);
                }
            });

            //Acción al tocar
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();

                    //Contexto
                    Context context = v.getContext();

                    //Entrar en detalles
                    Intent intent = new Intent(context, DetallesDeudaActivity.class);
                    context.startActivity(intent);
                }
            });

            //Si la deuda ha slido saldada, ocultar botones
            if (saldada)
            {
                accion1.setVisibility(View.GONE);
                accion2.setVisibility(View.GONE);
            }
            else
            {
                accion1.setVisibility(View.VISIBLE);
                accion2.setVisibility(View.VISIBLE);
            }

        }

        /**
         * Abrir diálogo para introducir cantidad
         * @param v
         */
        private void abrirDialogoCantidad(View v, int tipoAccion)
        {
            //Recuperar posición actual
            int posicion = getAdapterPosition();


            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());

            builder.setView(layoutInflater.inflate(R.layout.dialog_accion_deuda, null));

            //Título
            if (tipoAccion == KAccion.ACCION_SALDAR)
            {
                builder.setTitle(R.string.dialogo_deuda_titulo1);
            }
            else if (tipoAccion == KAccion.ACCION_AUMENTAR)
            {
                builder.setTitle(R.string.dialogo_deuda_titulo2);
            }

            //Botón aceptar
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    //deudas.remove(0);
                    //instance.notifyDataSetChanged();
                }
            });

            //Botón cancelar
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            //Abrir diálogo
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }
}
