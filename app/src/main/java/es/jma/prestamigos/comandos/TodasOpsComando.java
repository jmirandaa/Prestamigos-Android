package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import es.jma.prestamigos.conexiones.IOperacionesService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Operacion;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.enums.TipoOperacion;
import es.jma.prestamigos.eventbus.EventOps;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Hacer la llamada para obtener todas las operaciones
 * Created by jmiranda on 16/02/17.
 */

public class TodasOpsComando extends Comando implements Callback<RespuestaREST<List<Operacion>>> {
    public TodasOpsComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 3)
            {
                throw new RuntimeException("El argumento 1 debe ser el idDeuda, el argumento 2 el tipo y el" +
                        " argumento 3 el idUsuario");
            }
            long idDeuda = (long) args[0];
            TipoOperacion tipoOperacion = (TipoOperacion) args[1];
            long idUsuario = (long) args[2];

            //Traducir tipo
            int tipo = TipoOperacion.fromEnum(tipoOperacion);

            //Construir petición
            Retrofit retrofit = construir();
            IOperacionesService opsService = retrofit.create(IOperacionesService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<List<Operacion>>> call = opsService.todasOperaciones(idDeuda,tipo,(int)idUsuario);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<List<Operacion>>> call, Response<RespuestaREST<List<Operacion>>> response) {
        //Si estoy en la pantalla de detalles de deuda
        if (this.pantalla == KPantallas.PANTALLA_DETALLES_DEUDA)
        {
            RespuestaREST<List<Operacion>> ops = response.body();

            if (ops != null) {
                List<Operacion> contenido = ops.getContenido();
                if (contenido == null)
                {
                    contenido = new ArrayList<>();
                }

                //Notificar resultado
                EventOps eventOps = new EventOps(contenido);
                eventOps.setCodigo(ops.getCodError());
                eventOps.setMsg(ops.getMsgError());
                EventBus.getDefault().post(eventOps);
            }
            else
            {
                error();
            }
        }
    }

    @Override
    public void onFailure(Call<RespuestaREST<List<Operacion>>> call, Throwable t) {
        error();
    }

    /**
     * Notificar error
     */
    private void error()
    {
        //Notificar resultado
        EventOps eventOps = new EventOps(null);
        eventOps.setCodigo(-1);
        EventBus.getDefault().post(eventOps);
    }
}
