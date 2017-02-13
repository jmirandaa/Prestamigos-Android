package es.jma.prestamigos.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.jma.prestamigos.enums.TipoOperacion;

/**
 * Created by tulon on 8/02/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Operacion {
    //Atributos
    private long id;
    private Date fechaRegistro;
    private double cantidad;
    private Deuda deuda;
    private TipoOperacion tipo;
    private Usuario usuario;

    //Constructores
    public Operacion()
    {

    }

    public Operacion(int id, double cantidad, Deuda deuda, TipoOperacion tipo, Usuario usuario)
    {
        this.id = id;
        this.cantidad = cantidad;
        this.deuda = deuda;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    /**
     * Datos de prueba
     * @return
     */
    public static List<Operacion> getDatosPrueba()
    {
        List<Operacion> operaciones = new ArrayList<Operacion> ();
        List<Deuda> deudas = Deuda.getDatosPrueba();
        List<Usuario> usuarios = Usuario.getDatosPrueba();

        Operacion op1 = new Operacion(1,2, deudas.get(0), TipoOperacion.PAGAR, usuarios.get(0));
        Operacion op2= new Operacion(2,1, deudas.get(1), TipoOperacion.PAGAR, usuarios.get(1));
        Operacion op3 = new Operacion(3,3, deudas.get(2), TipoOperacion.PAGAR, usuarios.get(2));

        operaciones.add(op1);
        operaciones.add(op2);
        operaciones.add(op3);

        return operaciones;
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Deuda getDeuda() {
        return deuda;
    }

    public void setDeuda(Deuda deuda) {
        this.deuda = deuda;
    }

    public TipoOperacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoOperacion tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
