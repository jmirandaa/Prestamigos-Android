package es.jma.prestamigos.comandos;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by tulon on 15/02/17.
 */

public abstract class Comando {
    public static final String URL_BASE = "http://vps364393.ovh.net:8080/prestamos/";
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
