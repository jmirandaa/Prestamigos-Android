package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import es.jma.prestamigos.conexiones.IOperacionesService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.Operacion;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.eventbus.EventDeudas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Hacer la llamada para crear una nueva operación
 * Created by jmiranda on 16/02/17.
 */

public class NuevaOpComando extends Comando implements Callback<RespuestaREST<List<Deuda>>> {
    public NuevaOpComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 2)
            {
                throw new RuntimeException("El argumento 1 debe ser la operacion y el argumento 2 el tipo");
            }
            Operacion operacion = (Operacion) args[0];
            TipoDeuda tipoDeuda = (TipoDeuda) args[1];

            //Traducir tipo
            int tipo = TipoDeuda.fromEnum(tipoDeuda);

            //Construir petición
            Retrofit retrofit = construir();
            IOperacionesService opsService = retrofit.create(IOperacionesService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<List<Deuda>>> call = opsService.nuevaOperacion(operacion, tipo);
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
        if ((this.pantalla == KPantallas.PANTALLA_DEUDAS_OTROS) || (this.pantalla == KPantallas.PANTALLA_MIS_DEUDAS))
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
