package es.jma.prestamigos.utils.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.View;

import es.jma.prestamigos.constantes.KShared;

/**
 * Clase de utilidad para la UI y SharedPreferences
 * Created by jmiranda on 4/02/17.
 */

public class UtilUI {

    /**
     * Ocultar y desocultar la barra de búsqueda
     * @param actionBar
     * @param searchView
     */
    public static void toggleSearchView (ActionBar actionBar, SearchView searchView)
    {
        //Si está invisible, hacerla visible
        if (searchView.getVisibility() == View.GONE)
        {
            actionBar.hide();
            searchView.setVisibility(View.VISIBLE);
            searchView.setIconified(false);
        }
        //En caso contrario, hacerla visible
        else
        {
            actionBar.show();
            searchView.clearFocus();
            searchView.setVisibility(View.GONE);
        }
    }

    /**
     * Leer el idUsuario de las SharedPreferences
     * @param context
     * @return
     */
    public static long getIdUsuario(Context context)
    {
        long idUsuario = -1;

        SharedPreferences shared = context.getSharedPreferences(KShared.CLAVE_PREF, Context.MODE_PRIVATE);
        idUsuario = shared.getLong(KShared.CLAVE_ID,-1);

        return idUsuario;
    }

    /**
     * Leer el email de las SharedPreferences
     * @param context
     * @return
     */
    public static String getEmail(Context context)
    {
        String email = null;

        SharedPreferences shared = context.getSharedPreferences(KShared.CLAVE_PREF, Context.MODE_PRIVATE);
        email = shared.getString(KShared.CLAVE_EMAIL,"");

        return email;
    }

    /**
     * Leer el password de las SharedPreferences
     * @param context
     * @return
     */
    public static String getPassword(Context context)
    {
        String password = null;

        SharedPreferences shared = context.getSharedPreferences(KShared.CLAVE_PREF, Context.MODE_PRIVATE);
        password = shared.getString(KShared.CLAVE_PASSWORD,"");

        return password;
    }

    /**
     * Leer el nombre y apellido de las SharedPreferences
     * @param context
     * @return
     */
    public static String getNombre(Context context)
    {
        String nombre = null;

        SharedPreferences shared = context.getSharedPreferences(KShared.CLAVE_PREF, Context.MODE_PRIVATE);
        nombre = shared.getString(KShared.CLAVE_NOMBRE,"");

        return nombre;
    }

    /**
     * Mostrar barra de progreso
     * @param show
     * @param mView
     * @param mProgressView
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, final View mView, final View mProgressView, Context context) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mView.setVisibility(show ? View.GONE : View.VISIBLE);
            mView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
