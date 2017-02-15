package es.jma.prestamigos.eventbus;

import java.util.List;

import es.jma.prestamigos.dominio.Deuda;

/**
 * Evento de deudas
 * Created by jmiranda on 15/02/17.
 */

public class EventDeudas extends EventBase {
    private List<Deuda> deudas;

    public EventDeudas(List<Deuda> deudas)
    {
        this.deudas = deudas;
    }

    public List<Deuda> getDeudas() {
        return deudas;
    }

    public void setDeudas(List<Deuda> deudas) {
        this.deudas = deudas;
    }
}
