package es.jma.prestamigos.eventbus;

import es.jma.prestamigos.dominio.ResumenDeuda;

/**
 * Created by tulon on 6/03/17.
 */

public class EventResumen extends EventBase {
    private ResumenDeuda resumenDeudas;

    public EventResumen(ResumenDeuda resumenDeuda) {
        super();
        this.resumenDeudas = resumenDeuda;
    }

    public ResumenDeuda getResumenDeudas() {
        return resumenDeudas;
    }

    public void setResumenDeudas(ResumenDeuda resumenDeudas) {
        this.resumenDeudas = resumenDeudas;
    }
}
