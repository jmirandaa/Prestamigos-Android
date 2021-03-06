package es.jma.prestamigos.conexiones;

import java.util.List;

import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.ResumenDeuda;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interfaz Retrofit de operaciones de deudas
 * Created by jmiranda on 15/02/17.
 */

public interface IDeudasService {
    /**
     * Obtener las deudas de un usuario por su id
     * @param idUsuario
     * @param tipo
     * @param saldada
     * @return
     */
    @GET("deudas/todasId")
    public Call<RespuestaREST<List<Deuda>>> todasDeudas (@Query("idUsuario") long idUsuario, @Query("tipo") int tipo, @Query("saldada") boolean saldada);

    /**
     * Añadir nueva deuda
     * @param idUsuarioOrigen
     * @param idDestino
     * @param cantidad
     * @param concepto
     * @param tipo
     * @return
     */
    @POST("deudas/deuda")
    public Call<RespuestaREST<List<Deuda>>> nuevaDeuda (@Query("idUsuarioOrigen") long idUsuarioOrigen, @Query("idDestino") long idDestino,
                                                         @Query("cantidad") double cantidad, @Query("concepto") String concepto, @Query("tipo") int tipo);

    /**
     * Información de resumen de deudas
     * @param idUsuario
     * @return
     */
    @GET("deudas/resumen")
    public Call<RespuestaREST<ResumenDeuda>> resumenDeudas (@Query("idUsuario") long idUsuario);
}
