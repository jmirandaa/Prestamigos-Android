package es.jma.prestamigos.enums;

/**
 * Created by tulon on 8/02/17.
 */

public enum TipoDeuda {
    DEBEN, DEBO, TODAS;

    public static TipoDeuda getContrario(TipoDeuda tipoDeuda)
    {
        if (tipoDeuda == TipoDeuda.DEBO)
        {
            return TipoDeuda.DEBEN;
        }
        else if (tipoDeuda == TipoDeuda.DEBEN)
        {
            return TipoDeuda.DEBO;
        }
        else
        {
            return TipoDeuda.DEBEN;
        }
    }

    public static TipoDeuda fromInteger(int x) {
        switch(x)
        {
            case 0:
                return TODAS;
            case 1:
                return DEBEN;
            case 2:
                return DEBO;
        }
        return null;
    }
}