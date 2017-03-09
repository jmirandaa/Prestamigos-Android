package es.jma.prestamigos.eventbus;

import java.util.List;

import es.jma.prestamigos.dominio.Operacion;

/**
 * Evento de ops
 * Created by jmiranda on 16/02/17.
 */

public class EventOps extends EventBase {
    private List<Operacion> operaciones;

    public EventOps(List<Operacion> operaciones)
    {
        this.operaciones = operaciones;
    }

    public List<Operacion> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<Operacion> operaciones) {
        this.operaciones = operaciones;
    }
}
