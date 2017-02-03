package es.jma.prestamigos.dominio;

import java.util.Date;

/**
 * Created by tulon on 3/02/17.
 */

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

    public Usuario(long id, String email, String nombre, String apellidos)
    {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
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
