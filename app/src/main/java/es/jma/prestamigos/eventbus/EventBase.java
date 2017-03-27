package es.jma.prestamigos.eventbus;

/**
 * Información común pasada a los eventos de EventBus
 * Created by jmiranda on 13/02/17.
 */

public class EventBase {
    private String msg;
    private int codigo;

    public EventBase()
    {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
