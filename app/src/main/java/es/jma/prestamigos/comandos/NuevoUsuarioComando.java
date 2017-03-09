package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.conexiones.IUsuariosService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventRegistro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by jmiranda on 9/03/17.
 */

public class NuevoUsuarioComando extends Comando implements Callback<RespuestaREST<Boolean>> {
    public NuevoUsuarioComando(int pantalla) {
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
            Call<RespuestaREST<Boolean>> call = usuariosService.nuevoUsuario(usuario);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<Boolean>> call, Response<RespuestaREST<Boolean>> response) {
        //Si estoy en pantalla de nueva deuda
        if (this.pantalla == KPantallas.PANTALLA_REGISTRO)
        {
            RespuestaREST<Boolean> resultado = response.body();

            if (resultado != null) {
                Boolean contenido = resultado.getContenido();
                if (contenido == null)
                {
                    contenido = false;
                }

                //Notificar resultado
                EventRegistro eventRegistro = new EventRegistro(contenido);
                eventRegistro.setCodigo(resultado.getCodError());
                eventRegistro.setMsg(resultado.getMsgError());
                EventBus.getDefault().post(eventRegistro);
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
        EventRegistro eventRegistro = new EventRegistro(false);
        eventRegistro.setCodigo(-1);
        EventBus.getDefault().post(eventRegistro);
    }
}
