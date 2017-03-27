package es.jma.prestamigos.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.jma.prestamigos.DetallesDeudaActivity;
import es.jma.prestamigos.R;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.NuevaOpComando;
import es.jma.prestamigos.constantes.KAccion;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.Operacion;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.enums.TipoOperacion;
import es.jma.prestamigos.filtros.DeudaFilter;
import es.jma.prestamigos.utils.ui.UtilFechas;
import es.jma.prestamigos.utils.ui.UtilUI;

/**
 * Adapter para el listado de deudas
 * Created by jmiranda on 3/02/17.
 */

public class DeudaAdapter extends RecyclerView.Adapter<DeudaAdapter.DeudaViewHolder> implements Filterable {
    private DeudaAdapter instance;
    private List<Deuda> deudas;
    private List<Deuda> deudasSinFiltrar;
    private TipoDeuda tipoDeuda;
    private DeudaFilter deudaFilter;

    public DeudaAdapter(TipoDeuda tipoDeuda, List<Deuda> deudas)
    {
        this.instance = this;
        this.deudas = deudas;
        this.deudasSinFiltrar = deudas;
        this.tipoDeuda = tipoDeuda;
    }

    public List<Deuda> getDeudas() {
        return deudas;
    }

    public TipoDeuda getTipoDeuda() {
        return tipoDeuda;
    }

    public void setDeudas(List<Deuda> deudas) {
        this.deudas = deudas;
    }

    public List<Deuda> getDeudasSinFiltrar() {
        return deudasSinFiltrar;
    }

    public void setDeudasSinFiltrar(List<Deuda> deudasSinFiltrar) {
        this.deudasSinFiltrar = deudasSinFiltrar;
        this.deudas = deudasSinFiltrar;
    }

    @Override
    public DeudaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_deuda, parent, false);
        DeudaViewHolder dvh = new DeudaViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DeudaViewHolder holder, int position) {
        Deuda deuda = deudas.get(position);
        Usuario usuarioOrigen = deuda.getUsuario();
        Usuario usuarioDestino = deuda.getUsuarioDestino();


        //Procesar nombre y apellidos
        boolean debo=false, meDebe=false;
        String nombre = null;
        String apellidos = null;
        double cantidad = deuda.getCantidad() - deuda.getSaldado();

        //Si se mira los que deben, el nombre es el origen

        if (tipoDeuda.equals(TipoDeuda.DEBEN) && (usuarioOrigen != null))
        {
            nombre = usuarioOrigen.getNombre();
            apellidos = usuarioOrigen.getApellidos();
        }
        //Si se mira los que debo, el nombre es el destino
        else if (tipoDeuda.equals(TipoDeuda.DEBO) && (usuarioDestino != null))
        {
            nombre = usuarioDestino.getNombre();
            apellidos = usuarioDestino.getApellidos();
        }
        //Si se miran todas, es el que tenga un id distinto
        else if ((tipoDeuda.equals(TipoDeuda.TODAS)) && (usuarioOrigen != null) && (usuarioDestino != null))
        {
            long idUsuario = UtilUI.getIdUsuario(holder.itemView.getContext());
            long idUsuarioOrigen = usuarioOrigen.getId();
            long idUsuarioDestino = usuarioDestino.getId();

            //Le debo
            if (idUsuario == idUsuarioOrigen)
            {
                nombre = usuarioDestino.getNombre();
                apellidos = usuarioDestino.getApellidos();
                debo=true;
            }
            //Me debe
            else if (idUsuario == idUsuarioDestino)
            {
                nombre = usuarioOrigen.getNombre();
                apellidos = usuarioOrigen.getApellidos();
                meDebe=true;
            }

            //En este caso usar sólo la cantidad
            cantidad = deuda.getCantidad();
        }

        if (nombre == null)
        {
            nombre = "";
        }
        if (apellidos == null)
        {
            apellidos = "";
        }

        //Procesar fecha
        String fechaTexto = UtilFechas.parseFecha(deuda.getFechaRegistro());

        //Actualizar holder
        holder.nombre.setText(nombre+" "+apellidos);
        holder.fecha.setText(fechaTexto);
        holder.concepto.setText(deuda.getConcepto());
        holder.cantidad.setText(cantidad +"€");

        boolean saldada = (deuda.getCantidad() - deuda.getSaldado()) == 0.0;
        //Si la deuda ha slido saldada, ocultar botones y poner tipo
        if (saldada)
        {
            String texto = null;
            if (debo)
            {
                texto = holder.cantidad.getResources().getText(R.string.dashboard_debia).toString();
            }
            else if (meDebe)
            {
                texto = holder.cantidad.getResources().getText(R.string.dashboard_medebian).toString();

            }
            if (texto != null)
            {
                holder.cantidad.append(" (");
                holder.cantidad.append(texto);
                holder.cantidad.append(")");
            }

            holder.accion1.setVisibility(View.GONE);
            holder.accion2.setVisibility(View.GONE);
        }
        else
        {
            holder.accion1.setVisibility(View.VISIBLE);
            holder.accion2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return deudas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        if (deudaFilter == null)
        {
            deudaFilter = new DeudaFilter(this);
        }
        return deudaFilter;
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
                    Deuda deuda = deudas.get(posicion);

                    //Contexto
                    Context context = v.getContext();

                    //Entrar en detalles
                    Bundle bundle = new Bundle();
                    bundle.putLong("idDeuda",deuda.getId());
                    bundle.putString("nombre", nombre.getText().toString());
                    bundle.putString("fecha", fecha.getText().toString());
                    bundle.putString("concepto",concepto.getText().toString());
                    bundle.putString("cantidad", String.valueOf(deuda.getCantidad()));
                    bundle.putString("pagado", String.valueOf(deuda.getSaldado()));

                    Intent intent = new Intent(context, DetallesDeudaActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }

        /**
         * Abrir diálogo para introducir cantidad
         * @param v
         */
        private void abrirDialogoCantidad(View v, int tipoAccion)
        {
            //Recuperar posición actual
            int posicion = getAdapterPosition();
            final Deuda deuda = deudas.get(posicion);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());

            final View dialogView = layoutInflater.inflate(R.layout.dialog_accion_deuda, null);
            builder.setView(dialogView);

            TipoOperacion tipo = null;
            //Título
            if (tipoAccion == KAccion.ACCION_SALDAR)
            {
                builder.setTitle(R.string.dialogo_deuda_titulo1);
                tipo = TipoOperacion.PAGAR;
            }
            else if (tipoAccion == KAccion.ACCION_AUMENTAR)
            {
                builder.setTitle(R.string.dialogo_deuda_titulo2);
                tipo = TipoOperacion.AUMENTAR;
            }

            final TipoOperacion tipoFinal = tipo;

            //Botón aceptar
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //Recuperar valor
                    EditText etCantidad = (EditText) dialogView.findViewById(R.id.campo_cantidad);

                    Editable valor = etCantidad.getText();
                    if ((valor != null) && (!valor.toString().isEmpty())) {
                        //Crear operacion
                        Operacion operacion = new Operacion();
                        double cantidad = Double.parseDouble(valor.toString());
                        operacion.setCantidad(cantidad);
                        operacion.setDeuda(deuda);
                        operacion.setTipo(tipoFinal);
                        operacion.setUsuario(new Usuario(UtilUI.getIdUsuario(dialogView.getContext())));

                        //LLamar al servicio
                        Comando comando = new NuevaOpComando(KPantallas.PANTALLA_DEUDAS_OTROS);
                        comando.ejecutar(operacion, tipoDeuda);
                    }
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
