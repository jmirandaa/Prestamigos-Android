package es.jma.prestamigos.utils.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase de utilidad para manejar fechas
 * Created by jmiranda on 16/02/17.
 */

public class UtilFechas {

    /**
     * Convertir una fecha a String
     * @param fecha
     * @return
     */
    public static String parseFecha (Date fecha)
    {
        String fechaString = null;
        if (fecha != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaString = sdf.format(fecha);
        }
        else
        {
            fechaString = "??/??/????";
        }
        return fechaString;
    }
}
