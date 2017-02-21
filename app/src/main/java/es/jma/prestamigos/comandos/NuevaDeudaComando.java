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
 * Created by tulon on 21/02/17.
 */

public class NuevaDeudaComando extends Comando implements Callback<RespuestaREST<List<Deuda>>> {
    public NuevaDeudaComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 5)
            {
                throw new RuntimeException("El argumento 1 debe ser el idOrigen, el argumento 2 el idDestino, el argumento 3 la cantidad, " +
                        "el argumento 4 el concepto y el argumento 5 el tipo");
            }
            long idOrigen = (long) args[0];
            long idDestino = (long) args[1];
            double cantidad = (double) args[2];
            String concepto = (String) args[3];
            TipoDeuda tipoDeuda = (TipoDeuda) args[4];

            //Traducir tipo
            int tipo = TipoDeuda.fromEnum(tipoDeuda);

            //Construir petición
            Retrofit retrofit = construir();
            IDeudasService deudaService = retrofit.create(IDeudasService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<List<Deuda>>> call = deudaService.nuevaDeuda(idOrigen, idDestino, cantidad, concepto, tipo);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<List<Deuda>>> call, Response<RespuestaREST<List<Deuda>>> response) {
        //Si estoy en nueva deuda
        if (this.pantalla == KPantallas.PANTALLA_NUEVA_DEUDA_OTROS)
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
