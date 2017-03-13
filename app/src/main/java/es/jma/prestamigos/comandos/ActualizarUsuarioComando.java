package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.conexiones.IUsuariosService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventGenerico;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by jmiranda on 13/03/17.
 */

public class ActualizarUsuarioComando extends Comando implements Callback<RespuestaREST<Boolean>> {
    public ActualizarUsuarioComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 1)
            {
                throw new RuntimeException("El argumento 1 debe ser el usuario");
            }
            Usuario usuario = (Usuario) args[0];

            //Construir petición
            Retrofit retrofit = construir();
            IUsuariosService usuariosService = retrofit.create(IUsuariosService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<Boolean>> call = usuariosService.actualizarUsuario(usuario);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<Boolean>> call, Response<RespuestaREST<Boolean>> response) {
        RespuestaREST<Boolean> usuario = response.body();

        //Notificar resultado
        if (usuario != null) {
            //Si estoy en perfil, usar evento genérico
            if (this.pantalla == KPantallas.PANTALLA_PERFIL)
            {
                EventGenerico<Boolean> eventGenerico = new EventGenerico<Boolean>(usuario.getContenido());
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
    public void onFailure(Call<RespuestaREST<Boolean>> call, Throwable t) {
        error();
    }

    /**
     * Notificar error
     */
    private void error()
    {
        //Notificar resultado
        EventGenerico<Boolean> eventGenerico = new EventGenerico<Boolean>(false);
        eventGenerico.setCodigo(-1);
        EventBus.getDefault().post(eventGenerico);
    }
}
