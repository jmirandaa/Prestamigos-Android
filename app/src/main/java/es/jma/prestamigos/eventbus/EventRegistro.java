package es.jma.prestamigos.eventbus;

/**
 * Evento de registro
 * Created by jmiranda on 9/03/17.
 */

public class EventRegistro extends EventBase {
    private boolean resultado;

    public EventRegistro(boolean resultado) {
        super();
        this.resultado = resultado;
    }

    public boolean isResultado() {
        return resultado;
    }

    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }
}
