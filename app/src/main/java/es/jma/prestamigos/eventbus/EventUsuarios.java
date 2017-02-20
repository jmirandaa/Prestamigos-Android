package es.jma.prestamigos.eventbus;

import java.util.List;

import es.jma.prestamigos.dominio.Usuario;

/**
 * Created by tulon on 20/02/17.
 */

public class EventUsuarios extends EventBase {
    private List<Usuario> usuarios;

    public EventUsuarios(List<Usuario> usuarios)
    {
        super();
        this.usuarios = usuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
