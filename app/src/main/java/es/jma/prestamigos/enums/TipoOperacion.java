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

}