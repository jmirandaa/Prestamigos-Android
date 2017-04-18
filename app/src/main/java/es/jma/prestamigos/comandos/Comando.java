package es.jma.prestamigos.comandos;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit;
    }

}
