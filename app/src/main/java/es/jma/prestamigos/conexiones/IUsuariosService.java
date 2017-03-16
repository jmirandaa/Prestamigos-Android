package es.jma.prestamigos.conexiones;

import java.util.List;

import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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


    /**
     * Listado de amigos de un usuario
     * @param email
     * @return
     */
    @GET("usuarios/amigos")
    public Call<RespuestaREST<List<Usuario>>> todosAmigos (@Query("email") String email);

    /**
     * Añadir nuevo amigo invitado
     * @param usuario
     * @param emailOrigen
     * @return
     */
    @POST("usuarios/invitado")
    public Call<RespuestaREST<Long>> nuevoInvitado  (@Body Usuario usuario, @Query("emailOrigen") String emailOrigen);

    /**
     * Añadir nuevo amigo
     * @param emailDestino
     * @param emailOrigen
     * @return
     */
    @POST("usuarios/amigo")
    public Call<RespuestaREST<Long>> nuevoAmigo  (@Query("emailDestino") String emailDestino, @Query("emailOrigen") String emailOrigen);

    /**
     * Borrar amigo
     * @param emailOrigen
     * @param idAmigo
     * @return
     */
    @DELETE("usuarios/amigo")
    public Call<RespuestaREST<Boolean>> borrarAmigo  (@Query("emailOrigen") String emailOrigen, @Query("idAmigo") long idAmigo);

    /**
     * Añadir nuevo usuario
     * @param usuario
     * @return
     */
    @POST("usuarios/usuario")
    public Call<RespuestaREST<Boolean>> nuevoUsuario  (@Body Usuario usuario);

    /**
     * Actualizar usuario
     * @param usuario
     * @return
     */
    @PUT("usuarios/usuario")
    public Call<RespuestaREST<Boolean>> actualizarUsuario  (@Body Usuario usuario);
}
