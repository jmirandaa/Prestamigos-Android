package es.jma.prestamigos.filtros;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import es.jma.prestamigos.adaptadores.DeudaAdapter;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.utils.ui.UtilTextValidator;

/**
 * Filtro para el Adapter de deudas
 * Created by jmiranda on 14/03/17.
 */

public class DeudaFilter extends Filter {

    protected DeudaAdapter deudaAdapter;

    public DeudaFilter(DeudaAdapter deudaAdapter)
    {
        this.deudaAdapter = deudaAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence restriccion) {
        List<Deuda> deudas = deudaAdapter.getDeudasSinFiltrar();
        TipoDeuda tipoDeuda = deudaAdapter.getTipoDeuda();
        FilterResults resultados = new FilterResults();

        if (deudas != null) {
            //Si no hay restricciones, devolver toda la lista
            if (restriccion == null || restriccion.length() == 0) {
                resultados.values = deudas;
                resultados.count = deudas.size();
            } else {
                // Filtrar
                List<Deuda> deudasFiltradas = new ArrayList<Deuda>();
                for (Deuda deuda : deudas) {
                    Usuario usuarioOrigen = deuda.getUsuario();
                    Usuario usuarioDestino = deuda.getUsuarioDestino();

                    //Filtrar por nombre y apellidos
                    String nombre = null;
                    String apellidos = null;

                    if (tipoDeuda.equals(TipoDeuda.DEBEN) && (usuarioOrigen != null)) {
                        nombre = usuarioOrigen.getNombre();
                        apellidos = usuarioOrigen.getApellidos();
                    } else if (tipoDeuda.equals(TipoDeuda.DEBO) && (usuarioDestino != null)) {
                        nombre = usuarioDestino.getNombre();
                        apellidos = usuarioDestino.getApellidos();
                    }

                    if ((nombre != null) && (apellidos != null)) {
                        String nombreYApellido = nombre + " " + apellidos;

                        //Añadir si contiene parte
                        String original = UtilTextValidator.eliminarTildes(nombreYApellido.toUpperCase());
                        String buscado = UtilTextValidator.eliminarTildes(restriccion.toString().toUpperCase());
                        if (original.contains(buscado)) {
                            deudasFiltradas.add(deuda);
                        }
                    }

                }

                resultados.values = deudasFiltradas;
                resultados.count = deudasFiltradas.size();

            }
        }
        return resultados;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.count != 0)
        {
            deudaAdapter.setDeudas((List<Deuda>)results.values);
        }
        else
        {
            deudaAdapter.setDeudas(new ArrayList<Deuda>());
        }
        deudaAdapter.notifyDataSetChanged();
    }
}
