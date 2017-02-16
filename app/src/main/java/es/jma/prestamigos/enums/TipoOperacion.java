package es.jma.prestamigos.enums;

/**
 * Created by tulon on 8/02/17.
 */

public enum TipoOperacion {
    PAGAR, AUMENTAR;

    public static TipoOperacion fromInteger(int x) {
        switch(x)
        {
            case 0:
                return PAGAR;
            case 1:
                return AUMENTAR;
        }
        return null;
    }

    public static int fromEnum (TipoOperacion tipo){
        int num = 0;
        if (tipo.equals(TipoOperacion.PAGAR))
        {
            num = 0;
        }
        else if (tipo.equals(TipoOperacion.AUMENTAR))
        {
            num = 1;
        }

        return num;
    }

}