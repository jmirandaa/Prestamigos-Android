package es.jma.prestamigos.eventbus;

/**
 * Evento usando genéricos para el contenido
 * Created by jmiranda on 13/03/17.
 */

public class EventGenerico <T> extends EventBase {
    private T contenido;

    public EventGenerico(T contenido)
    {
        this.contenido = contenido;
    }

    public T getContenido() {
        return contenido;
    }

    public void setContenido(T contenido) {
        this.contenido = contenido;
    }
}
