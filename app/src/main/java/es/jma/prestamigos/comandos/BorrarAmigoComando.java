package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.conexiones.IUsuariosService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.eventbus.EventGenerico;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Borrar amigo
 * Created by jmiranda on 16/03/17.
 */

public class BorrarAmigoComando extends Comando implements Callback<RespuestaREST<Boolean>> {
    public BorrarAmigoComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 2)
            {
                throw new RuntimeException("El argumento 1 debe ser el email del usuario y el argumento 2 el id del amigo");
            }
            String email = (String) args[0];
            long idAmigo = (long) args[1];

            //Construir petición
            Retrofit retrofit = construir();
            IUsuariosService usuariosService = retrofit.create(IUsuariosService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<Boolean>> call = usuariosService.borrarAmigo(email,idAmigo);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<Boolean>> call, Response<RespuestaREST<Boolean>> response) {
        //Si estoy en pantalla de nueva deuda o de amigos
        if (this.pantalla == KPantallas.PANTALLA_DETALLES_AMIGO)
        {
            RespuestaREST<Boolean> respuesta = response.body();

            if (respuesta != null) {
                Boolean contenido = respuesta.getContenido();

                //Notificar resultado
                EventGenerico<Boolean> eventBorrar = new EventGenerico<Boolean>(contenido);
                eventBorrar.setCodigo(respuesta.getCodError());
                eventBorrar.setMsg(respuesta.getMsgError());
                EventBus.getDefault().post(eventBorrar);
            }
            else
            {
                error();
            }
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
        EventGenerico<Boolean> eventBorrar = new EventGenerico<Boolean>(false);
        eventBorrar.setCodigo(-1);
        EventBus.getDefault().post(eventBorrar);
    }
}
