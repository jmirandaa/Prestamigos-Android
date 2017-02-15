package es.jma.prestamigos.utils.ui;

import java.util.List;

import es.jma.prestamigos.dominio.Deuda;

/**
 * Clase de utilidad para sumas
 * Created by jmiranda on 15/02/17.
 */

public class UtilSuma {

    /**
     * Obtener lo que se debe de unas deudas
     * @param deudas
     * @return
     */
    public static double sumarDeudas(List<Deuda> deudas)
    {
        double suma = 0;

        if ((deudas != null) && (!deudas.isEmpty()))
        {
            for (Deuda deuda : deudas)
            {
                suma += (deuda.getCantidad() - deuda.getSaldado());
            }
        }

        return suma;
    }
}
