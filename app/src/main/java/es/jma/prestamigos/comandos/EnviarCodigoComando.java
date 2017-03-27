package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.conexiones.IRecordarService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.eventbus.EventGenerico;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static es.jma.prestamigos.constantes.KAccion.ACCION_RECUPERAR_CODIGO;

/**
 * Hacer la llamada para enviar código de recordar contraseña
 * Created by jmiranda on 22/03/17.
 */

public class EnviarCodigoComando extends Comando implements Callback<RespuestaREST<Boolean>> {
    public EnviarCodigoComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 1)
            {
                throw new RuntimeException("El argumento 1 debe ser el email del usuario");
            }
            String email = (String) args[0];

            //Construir petición
            Retrofit retrofit = construir();
            IRecordarService recordarService = retrofit.create(IRecordarService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<Boolean>> call = recordarService.enviarEmail(email);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<Boolean>> call, Response<RespuestaREST<Boolean>> response) {
        //Pantalla de recordar contraseña
        if (this.pantalla == KPantallas.PANTALLA_RECORDAR_CONTRASEÑA)
        {
            RespuestaREST<Boolean> respuesta = response.body();

            if (respuesta != null) {
                Boolean contenido = respuesta.getContenido();

                //Notificar resultado
                EventGenerico<Boolean> eventGenerico = new EventGenerico<Boolean>(contenido);
                eventGenerico.setCodigo(ACCION_RECUPERAR_CODIGO);
                eventGenerico.setMsg(respuesta.getMsgError());
                EventBus.getDefault().post(eventGenerico);
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
        EventGenerico<Boolean> eventGenerico = new EventGenerico<>(false);
        eventGenerico.setCodigo(-1);
        EventBus.getDefault().post(eventGenerico);
    }
}
