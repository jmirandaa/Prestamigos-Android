package es.jma.prestamigos.conexiones;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by tulon on 13/02/17.
 */

public class ConexionesREST implements Callback<RespuestaREST<Usuario>> {
    private static final String URL_BASE = "http://vps364393.ovh.net:8080/prestamos/";

    private static ConexionesREST instance;

    //Valor para indicar la pantalla de retorno
    private static int pantallaRetorno;

    private ConexionesREST()
    {

    }

    public static ConexionesREST getInstance(int pantallaRetorno)
    {
        if (instance == null)
        {
            instance = new ConexionesREST();
        }

        ConexionesREST.pantallaRetorno = pantallaRetorno;

        return instance;
    }


    /**
     * Obtener objeto de la clase Retrofit
     * @return
     */
    private Retrofit construir()
    {
        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

        return retrofit;
    }

    /**
     * Obtener servicio Usuarios
     * @param retrofit
     * @return
     */
    private IUsuariosService getUsuariosService(Retrofit retrofit)
    {
        IUsuariosService usuariosService = retrofit.create(IUsuariosService.class);
        return usuariosService;
    }


    /**
     * REST LogIn
     * @param email
     * @param password
     */
    public void login (String email, String password)
    {
        try {
            //Construir petición
            Retrofit retrofit = construir();
            IUsuariosService usuariosService = getUsuariosService(retrofit);

            //Llamar de forma asíncrona
            Call<RespuestaREST<Usuario>> call = usuariosService.login(email, password);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<Usuario>> call, Response<RespuestaREST<Usuario>> response) {

        //Si estoy en login, ver si el usuario existe
        if (pantallaRetorno == KPantallas.PANTALLA_LOGIN)
        {
            RespuestaREST<Usuario> usuario = response.body();

            //Notificar resultado
            EventLogin eventLogin = new EventLogin(usuario.getContenido());
            eventLogin.setCodigo(usuario.getCodError());
            eventLogin.setMsg(usuario.getMsgError());
            EventBus.getDefault().post(eventLogin);
        }

        ConexionesREST.pantallaRetorno = 0;
    }

    @Override
    public void onFailure(Call<RespuestaREST<Usuario>> call, Throwable t) {

        if (pantallaRetorno == KPantallas.PANTALLA_LOGIN)
        {
            //Notificar resultado
            EventLogin eventLogin = new EventLogin(null);
            eventLogin.setCodigo(-1);
            EventBus.getDefault().post(eventLogin);
        }
        ConexionesREST.pantallaRetorno = 0;
    }
}
