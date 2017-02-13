package es.jma.prestamigos.eventbus;

import es.jma.prestamigos.dominio.RespuestaREST;
import es.jma.prestamigos.dominio.Usuario;

/**
 * Evento de Login
 * Created by jmiranda on 13/02/17.
 */

public class EventLogin extends EventBase{
    private Usuario usuario;

    public EventLogin(Usuario usuario)
    {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
