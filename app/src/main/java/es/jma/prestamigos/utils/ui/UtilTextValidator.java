package es.jma.prestamigos.utils.ui;

import java.text.Normalizer;

/**
 * Clase de utilidad para validar strings
 * Created by jmirando on 9/03/17.
 */

public class UtilTextValidator {

    /**
     * Validar email
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Validar password
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Eliminar tildes
     * @param texto
     * @return
     */
    public static String eliminarTildes(String texto) {
        return texto == null ? null : Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
