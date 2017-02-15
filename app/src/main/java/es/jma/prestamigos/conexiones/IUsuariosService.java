package es.jma.prestamigos.conexiones;

import java.util.List;

import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz Retrofit de operaciones de usuarios
 * Created by jmiranda on 13/02/17.
 */

public interface IUsuariosService {

    /**
     * Hacer Log In con un email y contraseña
     * @param email
     * @param password
     * @return
     */
    @GET("usuarios/login")
    public Call<RespuestaREST<Usuario>> login (@Query("email") String email, @Query("password") String password);


}
