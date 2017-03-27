package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import es.jma.prestamigos.conexiones.IUsuariosService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventUsuarios;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Hacer la llamada para obtener el listado de amigos
 * Created by jmiranda on 20/02/17.
 */

public class AmigosComando extends Comando implements Callback<RespuestaREST<List<Usuario>>> {
    public AmigosComando(int pantalla) {
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
            IUsuariosService usuariosService = retrofit.create(IUsuariosService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<List<Usuario>>> call = usuariosService.todosAmigos(email);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<List<Usuario>>> call, Response<RespuestaREST<List<Usuario>>> response) {
        //Si estoy en pantalla de nueva deuda o de amigos
        if ((this.pantalla == KPantallas.PANTALLA_NUEVA_DEUDA_OTROS) || (this.pantalla == KPantallas.PANTALLA_AMIGOS))
        {
            RespuestaREST<List<Usuario>> usuarios = response.body();

            if (usuarios != null) {
                List<Usuario> contenido = usuarios.getContenido();
                if (contenido == null)
                {
                    contenido = new ArrayList<>();
                }

                //Notificar resultado
                EventUsuarios eventUsuarios = new EventUsuarios(contenido);
                eventUsuarios.setCodigo(usuarios.getCodError());
                eventUsuarios.setMsg(usuarios.getMsgError());
                EventBus.getDefault().post(eventUsuarios);
            }
            else
            {
                error();
            }
        }
    }

    @Override
    public void onFailure(Call<RespuestaREST<List<Usuario>>> call, Throwable t) {
        error();
    }

    /**
     * Notificar error
     */
    private void error()
    {
        //Notificar resultado
        EventUsuarios eventUsuarios = new EventUsuarios(null);
        eventUsuarios.setCodigo(-1);
        EventBus.getDefault().post(eventUsuarios);
    }
}
