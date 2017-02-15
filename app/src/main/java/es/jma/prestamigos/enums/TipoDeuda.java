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

    /**
     * Traducir de int a enum
     * @param x
     * @return
     */
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

    /**
     * Traducir de enum a int
     * @param tipo
     * @return
     */
    public static int fromEnum (TipoDeuda tipo){
        int num = 0;
        if (tipo.equals(TipoDeuda.TODAS))
        {
            num = 0;
        }
        else if (tipo.equals(TipoDeuda.DEBEN))
        {
            num = 1;
        }
        else if (tipo.equals(TipoDeuda.DEBO))
        {
            num = 2;
        }

        return num;
    }
}