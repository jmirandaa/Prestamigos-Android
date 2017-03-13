package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.conexiones.IUsuariosService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventGenerico;
import es.jma.prestamigos.eventbus.EventLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tulon on 15/02/17.
 */

public class LoginComando extends Comando implements Callback<RespuestaREST<Usuario>> {

    public LoginComando(int pantalla) {
        super(pantalla);
    }

    /**
     * arg1 email, arg2 password
     * @param args
     */
    @Override
    public void ejecutar(Object... args) {

        try {
            //Controlar argumentos
            if (args.length != 2)
            {
                throw new RuntimeException("El argumento 1 debe ser el email y el argumento 2 el password");
            }

            String email = (String) args[0];
            String password = (String) args[1];

            //Construir petición
            Retrofit retrofit = construir();
            IUsuariosService usuariosService = retrofit.create(IUsuariosService.class);

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
            RespuestaREST<Usuario> usuario = response.body();

            if (usuario != null) {
                //Notificar resultado
                //Si estoy en login, ver si el usuario existe
                if (this.pantalla == KPantallas.PANTALLA_LOGIN) {
                    EventLogin eventLogin = new EventLogin(usuario.getContenido());
                    eventLogin.setCodigo(usuario.getCodError());
                    eventLogin.setMsg(usuario.getMsgError());
                    EventBus.getDefault().post(eventLogin);
                }
                //Si estoy en perfil, usar evento genérico
                else if (this.pantalla == KPantallas.PANTALLA_PERFIL)
                {
                    EventGenerico<Usuario> eventGenerico = new EventGenerico<Usuario>(usuario.getContenido());
                    eventGenerico.setCodigo(usuario.getCodError());
                    eventGenerico.setMsg(usuario.getMsgError());
                    EventBus.getDefault().post(eventGenerico);
                }
            }
            else
            {
                error();
            }
    }

    @Override
    public void onFailure(Call<RespuestaREST<Usuario>> call, Throwable t) {
           error();
    }

    /**
     * Notificar error
     */
    private void error()
    {
        //Notificar resultado
        EventLogin eventLogin = new EventLogin(null);
        eventLogin.setCodigo(-1);
        EventBus.getDefault().post(eventLogin);
    }
}
