package es.jma.prestamigos.conexiones;

import java.util.List;

import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.RespuestaREST;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tulon on 15/02/17.
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
}
