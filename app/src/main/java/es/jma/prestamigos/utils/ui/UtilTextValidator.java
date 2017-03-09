package es.jma.prestamigos.utils.ui;

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
}
