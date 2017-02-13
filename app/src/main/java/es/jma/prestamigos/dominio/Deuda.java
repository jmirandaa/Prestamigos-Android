package es.jma.prestamigos.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tulon on 3/02/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Deuda {
    /*Atributos*/
    private long id ;
    private String idDeuda;
    private Date fechaRegistro;
    private String concepto ;
    private double cantidad ;
    private double saldado ;
    private Usuario usuario;
    private Usuario usuarioDestino;

    /*Constructores*/
    public Deuda()
    {

    }

    public Deuda(long id, String concepto, double cantidad)
    {
        this.id = id;
        this.concepto = concepto;
        this.cantidad = cantidad;
    }

    /**
     * Datos de prueba
     * @return
     */
    public static List<Deuda> getDatosPrueba()
    {
        ArrayList<Deuda> deudas = new ArrayList<Deuda>();

        Usuario u1 = new Usuario(1, "usuario1@usuario.com", "Javier", "Maestre");
        Usuario u2 = new Usuario(2, "usuario2@usuario.com", "Jack", "O'Neill");
        Usuario u3 = new Usuario(3, "usuario3@usuario.com", "John", "Carter");

        Deuda d1 = new Deuda(1, "Sushi", 15);
        Deuda d2 = new Deuda(2, "Gasolina", 20);
        Deuda d3 = new Deuda(3, "Viaje", 50);

        d1.setUsuarioDestino(u1);
        d2.setUsuarioDestino(u2);
        d3.setUsuarioDestino(u3);

        deudas.add(d1);
        deudas.add(d2);
        deudas.add(d3);

        return  deudas;
    }

    /* Getters y setters */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(String idDeuda) {
        this.idDeuda = idDeuda;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getSaldado() {
        return saldado;
    }

    public void setSaldado(double saldado) {
        this.saldado = saldado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(Usuario usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }
}
