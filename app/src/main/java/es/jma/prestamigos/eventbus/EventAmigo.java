package es.jma.prestamigos.eventbus;

/**
 * Created by tulon on 21/02/17.
 */

public class EventAmigo extends EventBase {
    private long idAmigo;

    public EventAmigo(long idAmigo)
    {
        this.idAmigo = idAmigo;
    }

    public long getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(long idAmigo) {
        this.idAmigo = idAmigo;
    }
}
