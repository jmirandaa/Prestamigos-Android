package es.jma.prestamigos.conexiones;

import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interfaz Retrofit de operaciones de recordar contraseña
 * Created by jmiranda on 22/03/17.
 */

public interface IRecordarService {

    /**
     * Enviar correo con código de seguridad
     * @param email
     * @return
     */
    @POST("recordar/enviarEmail")
    public Call<RespuestaREST<Boolean>> enviarEmail  (@Query("email") String email);

    /**
     * Resetear la contraseña si el token es correcto
     * @param usuario
     * @return
     */
    @POST("recordar/resetearPassword")
    public Call<RespuestaREST<Boolean>> resetearPassword  (@Body Usuario usuario);
}
