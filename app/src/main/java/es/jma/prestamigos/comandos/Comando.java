package es.jma.prestamigos.comandos;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Clase abstracta que define lo necesario para hacer llamadas a través
 * de RetroFit
 * Created by jmiranda on 15/02/17.
 */

public abstract class Comando {
    public static final String URL_BASE = "http://lyxeon.es:8080/prestamos/"; //En producción debería usarse una conexión segura https
    protected int pantalla;

    protected Comando(int pantalla)
    {
        this.pantalla = pantalla;
    }

    /**
     * Comando
     * @param args
     */
    public abstract  void ejecutar(Object... args);

    /**
     * Obtener objeto de la clase Retrofit
     * @return
     */
    protected Retrofit construir()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit;
    }

}
