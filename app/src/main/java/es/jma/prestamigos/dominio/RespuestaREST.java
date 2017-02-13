package es.jma.prestamigos.dominio;

/**
 * Created by tulon on 13/02/17.
 */

public class RespuestaREST <T>{
    //Constantes
    public static final int COD_OK = 0;
    public static final int COD_ERROR = -1;

    public static final String MSG_OK = "OK";
    public static final String MSG_ERR = "Error genérico";


    //Atributos
    private int codError;
    private String msgError;
    private T contenido;

    //Constructores
    public RespuestaREST()
    {

    }

    /**
     * @return the codError
     */
    public int getCodError() {
        return codError;
    }

    /**
     * @param codError the codError to set
     */
    public void setCodError(int codError) {
        this.codError = codError;
    }

    /**
     * @return the msgError
     */
    public String getMsgError() {
        return msgError;
    }

    /**
     * @param msgError the msgError to set
     */
    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    /**
     * @return the contenido
     */
    public T getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(T contenido) {
        this.contenido = contenido;
    }


}