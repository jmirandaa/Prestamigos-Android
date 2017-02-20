package es.jma.prestamigos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.comandos.AmigosComando;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventUsuarios;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilSuma;
import es.jma.prestamigos.utils.ui.UtilUI;

import static es.jma.prestamigos.DeudasOtrosFragment.TIPO_DEUDA;

public class NuevaDeudaActivity extends BaseActivity {
    private int tipoDeuda = -1;

    @BindView(R.id.searchNuevaDeuda)
    AutoCompleteTextView searchView;
    @BindView(R.id.activity_nueva_deuda)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.form_nueva_deuda)
    View mView;
    @BindView(R.id.nueva_deuda_progress)
    View mProgressView;
    private ArrayAdapter<String> mAdapter;

    private List<String> sugerencias = new ArrayList<String>();

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int idItem = item.getItemId();
        //Ver botón pulsado
        switch (idItem)
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            //Si es el de terminar
            case R.id.accion_anyadir:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nueva_deuda, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_deuda);

        //Leer parámetros
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tipoDeuda = bundle.getInt(TIPO_DEUDA, -1);
        }

        //Barra de búsqueda
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sugerencias);
        searchView.setAdapter(mAdapter);
        searchView.setThreshold(1);

        //Cargar amigos
        actualizarAmigos();

    }

    /**
     * Cargar listado de amigos*/
    private void actualizarAmigos()
    {
        Comando comando = new AmigosComando(KPantallas.PANTALLA_NUEVA_DEUDA_OTROS);
        String email = UtilUI.getEmail(this);

        if ((email != null) && (!email.isEmpty())) {
            //Cargar amigos
            showProgress(true);

            //Llamada
            comando.ejecutar(email);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Responder a eventos de usuarios
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUsuarios(EventUsuarios event) {
        List<Usuario> usuarios = event.getUsuarios();
        showProgress(false);

        //Si es error de conexión
        if (event.getCodigo() == -1)
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, actualizar listado amigos
        else
        {
            mAdapter.clear();
            if (usuarios != null)
            {
                for (Usuario usuario : usuarios)
                {
                    mAdapter.add(usuario.getNombre()+" "+usuario.getApellidos());
                }
            }
            mAdapter.notifyDataSetChanged();
        }

    };

    /**
     * Ocultar formulario
     * @param show
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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
