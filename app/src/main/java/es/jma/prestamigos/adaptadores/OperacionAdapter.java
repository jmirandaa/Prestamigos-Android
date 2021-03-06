package es.jma.prestamigos.adaptadores;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.jma.prestamigos.R;
import es.jma.prestamigos.dominio.Operacion;
import es.jma.prestamigos.enums.TipoOperacion;
import es.jma.prestamigos.utils.ui.UtilFechas;

/**
 * Adapter para el listado de operaciones
 * Created by jmiranda on 8/02/17.
 */

public class OperacionAdapter extends RecyclerView.Adapter<OperacionAdapter.OperacionViewHolder> {
    private List<Operacion> operaciones;
    String tipoPagado;
    String tipoAumentada;

    public OperacionAdapter(List<Operacion> operaciones)
    {
        this.operaciones = operaciones;
    }

    public List<Operacion> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<Operacion> operaciones) {
        this.operaciones = operaciones;
    }

    @Override
    public OperacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_operacion, parent, false);
        tipoPagado = v.getResources().getText(R.string.op_tipo_pagar).toString();
        tipoAumentada = v.getResources().getText(R.string.op_tipo_aumentar).toString();
        OperacionViewHolder dvh = new OperacionViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(OperacionViewHolder holder, int position) {
        Operacion operacion = operaciones.get(position);
        TipoOperacion tipoOperacion = operacion.getTipo();

        //Traducir tipo
        String tipo = null;
        if (tipoOperacion.equals(TipoOperacion.PAGAR))
        {
            tipo = tipoPagado;
        }
        else
        {
            tipo = tipoAumentada;
        }

        //Procesar fecha
        String fecha = UtilFechas.parseFecha(operacion.getFechaRegistro());

        //Procesar cantidad
        double cantidad = operacion.getCantidad();

        holder.tipo.setText(tipo);
        holder.fecha.setText(fecha);
        holder.valor.setText(cantidad+"€");
    }

    @Override
    public int getItemCount() {
        return operaciones.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class OperacionViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        @BindView(R.id.op_tipo)
        TextView tipo;
        @BindView(R.id.op_fecha)
        TextView fecha;
        @BindView(R.id.op_valor)
        TextView valor;

        OperacionViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
