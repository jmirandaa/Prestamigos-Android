package es.jma.prestamigos.conexiones;

import java.util.List;

import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.Operacion;
import es.jma.prestamigos.dominio.RespuestaREST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jmiranda on 16/02/17.
 */

public interface IOperacionesService {

    /**
     * Obtener todas las operaciones de una deuda
     * @param idDeuda
     * @param tipo
     * @param idUsuario
     * @return
     */
    @GET("operaciones/todas")
    public Call<RespuestaREST<List<Operacion>>> todasOperaciones  (@Query("idDeuda") long idDeuda, @Query("tipo") int tipo, @Query("idUsuario") int idUsuario);

    /**
     * Generar una nueva operación de una deuda
     * @param operacion
     * @param tipo
     * @return
     */
    @POST("operaciones/operacion")
    public Call<RespuestaREST<List<Deuda>>> nuevaOperacion   (@Body Operacion operacion, @Query("tipoDeuda") int tipo);
}
