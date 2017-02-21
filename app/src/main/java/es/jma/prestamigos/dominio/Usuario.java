package es.jma.prestamigos.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tulon on 3/02/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {
    //Atributos
    private long id;
    private Date fechaRegistro;
    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private String avatarBase64;
    private boolean registrado;

    //Constructores
    public Usuario()
    {

    }

    public Usuario(long id)
    {
        this.id = id;
    }

    public Usuario(String nombre, String apellidos)
    {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Usuario(long id, String email, String nombre, String apellidos)
    {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    //Datos de prueba
    public static List<Usuario> getDatosPrueba()
    {
        List<Usuario> amigos = new ArrayList<>();
        amigos.add(new Usuario(1,"inv@invitados.com", "Javier", "Maestre"));
        amigos.add(new Usuario(2,"inv@invitados.com", "Jack", "O'Neill"));
        amigos.add(new Usuario(3,"inv@invitados.com", "John", "Carter"));
        return amigos;
    }

    //Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }
}
