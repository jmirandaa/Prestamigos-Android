package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import es.jma.prestamigos.conexiones.IDeudasService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.eventbus.EventDeudas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Hacer la llamada para obtener todas las deudas
 * Created by jmiranda on 15/02/17.
 */

public class TodasDeudasComando extends Comando implements Callback<RespuestaREST<List<Deuda>>> {
    public TodasDeudasComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 3)
            {
                throw new RuntimeException("El argumento 1 debe ser el idUsuario, el argumento 2 el tipo y el" +
                        " argumento 3 si está saldada");
            }
            long idUsuario = (long) args[0];
            TipoDeuda tipoDeuda = (TipoDeuda) args[1];
            boolean saldada = (Boolean) args[2];

            //Traducir tipo
            int tipo = TipoDeuda.fromEnum(tipoDeuda);

            //Construir petición
            Retrofit retrofit = construir();
            IDeudasService deudasService = retrofit.create(IDeudasService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<List<Deuda>>> call = deudasService.todasDeudas(idUsuario,tipo, saldada);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<List<Deuda>>> call, Response<RespuestaREST<List<Deuda>>> response) {
        //Si estoy en las deudas de otros
        if ((this.pantalla == KPantallas.PANTALLA_DEUDAS_OTROS) || (this.pantalla == KPantallas.PANTALLA_MIS_DEUDAS)
                || (this.pantalla == KPantallas.PANTALLA_HISTORIAL))
        {
            RespuestaREST<List<Deuda>> deudas = response.body();

            if (deudas != null) {
                List<Deuda> contenido = deudas.getContenido();
                if (contenido == null)
                {
                    contenido = new ArrayList<>();
                }

                //Notificar resultado
                EventDeudas eventDeudas = new EventDeudas(contenido);
                eventDeudas.setCodigo(deudas.getCodError());
                eventDeudas.setMsg(deudas.getMsgError());
                EventBus.getDefault().post(eventDeudas);
            }
            else
            {
                error();
            }
        }
    }

    @Override
    public void onFailure(Call<RespuestaREST<List<Deuda>>> call, Throwable t) {
        error();
    }

    /**
     * Notificar error
     */
    private void error()
    {
        //Notificar resultado
        EventDeudas eventDeudas = new EventDeudas(null);
        eventDeudas.setCodigo(-1);
        EventBus.getDefault().post(eventDeudas);
    }
}
