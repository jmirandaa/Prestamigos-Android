package es.jma.prestamigos.utils.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.View;

/**
 * Created by tulon on 4/02/17.
 */

public class UtilUI {

    /* Ocultar y desocultar la barra de búsqueda */
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
}
