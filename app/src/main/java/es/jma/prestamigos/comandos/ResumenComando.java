package es.jma.prestamigos.comandos;

import org.greenrobot.eventbus.EventBus;

import es.jma.prestamigos.conexiones.IDeudasService;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.ResumenDeuda;
import es.jma.prestamigos.eventbus.EventResumen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Hacer la llamada para obtener el resumen de las deudas
 * Created by jmiranda on 6/03/17.
 */

public class ResumenComando extends Comando implements Callback<RespuestaREST<ResumenDeuda>> {
    public ResumenComando(int pantalla) {
        super(pantalla);
    }

    @Override
    public void ejecutar(Object... args) {
        try {
            //Controlar argumentos
            if (args.length != 1)
            {
                throw new RuntimeException("El argumento 1 debe ser el id usuario");
            }
            long idUsuario = (long) args[0];


            //Construir petición
            Retrofit retrofit = construir();
            IDeudasService deudasService = retrofit.create(IDeudasService.class);

            //Llamar de forma asíncrona
            Call<RespuestaREST<ResumenDeuda>> call = deudasService.resumenDeudas(idUsuario);
            call.enqueue(this);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<RespuestaREST<ResumenDeuda>> call, Response<RespuestaREST<ResumenDeuda>> response) {
        //Si estoy en las deudas de otros
        if (this.pantalla == KPantallas.PANTALLA_RESUMEN)
        {
            RespuestaREST<ResumenDeuda> resumen = response.body();

            if (resumen != null) {
                ResumenDeuda contenido = resumen.getContenido();

                //Notificar resultado
                EventResumen eventResumen = new EventResumen(contenido);
                eventResumen.setCodigo(resumen.getCodError());
                eventResumen.setMsg(resumen.getMsgError());
                EventBus.getDefault().post(eventResumen);
            }
            else
            {
                error();
            }
        }
    }

    @Override
    public void onFailure(Call<RespuestaREST<ResumenDeuda>> call, Throwable t) {
        error();
    }

    /**
     * Notificar error
     */
    private void error()
    {
        //Notificar resultado
        EventResumen eventResumen = new EventResumen(null);
        eventResumen.setCodigo(-1);
        EventBus.getDefault().post(eventResumen);
    }
}
