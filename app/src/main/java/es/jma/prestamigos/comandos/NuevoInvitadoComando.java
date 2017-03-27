package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.conexiones.IUsuariosService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventAmigo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Hacer la llamada para crear un nuevo amigo invitado
 * Created by jmiranda on 21/02/17.
 */

public class NuevoInvitadoComando extends Comando implements Callback<RespuestaREST<Long>> {
    public NuevoInvitadoComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 2)
            {
                throw new RuntimeException("El argumento 1 debe ser el usuario y el argumento 2 el email origen");
            }
            Usuario usuario = (Usuario) args[0];
            String emailOrigen = (String) args[1];


            //Construir petición
            Retrofit retrofit = construir();
            IUsuariosService usuariosService = retrofit.create(IUsuariosService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<Long>> call = usuariosService.nuevoInvitado(usuario, emailOrigen);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<Long>> call, Response<RespuestaREST<Long>> response) {
        //Si estoy en nueva deuda
        if ((this.pantalla == KPantallas.PANTALLA_NUEVA_DEUDA_OTROS) || (this.pantalla == KPantallas.PANTALLA_AMIGOS))
        {
            RespuestaREST<Long> amigo = response.body();

            if (amigo != null) {
                long contenido = amigo.getContenido();

                //Notificar resultado
                EventAmigo eventAmigo = new EventAmigo(contenido);
                eventAmigo.setCodigo(amigo.getCodError());
                eventAmigo.setMsg(amigo.getMsgError());
                EventBus.getDefault().post(eventAmigo);
            }
            else
            {
                error();
            }
        }
    }

    @Override
    public void onFailure(Call<RespuestaREST<Long>> call, Throwable t) {
        error();
    }

    /**
     * Notificar error
     */
    private void error()
    {
        //Notificar resultado
        EventAmigo eventAmigo = new EventAmigo(-1);
        eventAmigo.setCodigo(-1);
        EventBus.getDefault().post(eventAmigo);
    }
}
