package es.jma.prestamigos;

import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_NOMBRE;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_APELLIDOS;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_EMAIL;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_ID;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.comandos.BorrarAmigoComando;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.eventbus.EventGenerico;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilUI;

public class DetallesAmigoActivity extends BaseActivity {

    //Parámetros
    private String nombre;
    private String apellidos;
    private String email;
    private long id;

    //Campos
    @BindView(R.id.detalles_amigo_nombre)
    EditText etNombre;
    @BindView(R.id.detalles_amigo_apellidos)
    EditText etApellidos;
    @BindView(R.id.detalles_amigo_email)
    EditText etEmail;

    @BindView(R.id.activity_detalles_amigo)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.formulario_amigo)
    View mView;
    @BindView(R.id.detalles_amigo_progress)
    View mProgressView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detalle_amigo, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_amigo);

        //Leer parámetros
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getLong(BUNDLE_AMIGO_ID, -1);
            nombre = bundle.getString(BUNDLE_AMIGO_NOMBRE,"");
            apellidos = bundle.getString(BUNDLE_AMIGO_APELLIDOS,"");
            email = bundle.getString(BUNDLE_AMIGO_EMAIL, "");

            //Actualizar campos
            etNombre.setText(nombre);
            etApellidos.setText(apellidos);
            etEmail.setText(email);
        }


    }

    /**
     * Realizar la conexión
     */
    private void borrarAmigo()
    {
        String emailOrigen = UtilUI.getEmail(this);
        long idAmigo = id;

        if ((emailOrigen!= null) && (!emailOrigen.isEmpty()) && (id > 0))
        {
            showProgress(true);
            Comando borrar = new BorrarAmigoComando(KPantallas.PANTALLA_DETALLES_AMIGO);
            borrar.ejecutar(emailOrigen, idAmigo);
        }
    }

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
            case R.id.accion_borrar_amigo:
                borrarAmigo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * Evento de borrado
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBorrado(EventGenerico<Boolean> event) {
        Boolean resultado = event.getContenido();
        showProgress(false);

        //Si es error de conexión
        if ((event.getCodigo() == -1) || (!resultado))
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, actualizar listado amigos
        else
        {
            //Finalizar actividad con éxito
            setResult(RESULT_OK);
            finish();
        }

    };

    /**
     * Progreso
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
