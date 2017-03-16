package es.jma.prestamigos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.comandos.AmigosComando;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.NuevaDeudaComando;
import es.jma.prestamigos.comandos.NuevoAmigoComando;
import es.jma.prestamigos.comandos.NuevoInvitadoComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.eventbus.EventAmigo;
import es.jma.prestamigos.eventbus.EventDeudas;
import es.jma.prestamigos.eventbus.EventUsuarios;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilTextValidator;
import es.jma.prestamigos.utils.ui.UtilUI;

import static es.jma.prestamigos.DeudasOtrosFragment.TIPO_DEUDA;

/**
 * FALTA:
 * - Probar con email
 */

public class NuevaDeudaActivity extends BaseActivity {
    static final int RESULT_OK = 10;
    private int tipoDeuda = -1;

    @BindView(R.id.searchNuevaDeuda)
    AutoCompleteTextView searchView;
    @BindView(R.id.activity_nueva_deuda)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.form_nueva_deuda)
    View mView;
    @BindView(R.id.nueva_deuda_progress)
    View mProgressView;
    @BindView(R.id.nueva_deuda_concepto)
    EditText mConcepto;
    @BindView(R.id.nueva_deuda_cantidad)
    EditText mCantidad;

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
                nuevaDeuda();
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
     * Si los campos son correctos, crear nueva deuda
     */
    private void nuevaDeuda()
    {
        //Ver si el usuario es amigo o no
        boolean correcto = true;
        String nombreAmigo = searchView.getText().toString();
        String concepto = mConcepto.getText().toString();
        String cantidadString = mCantidad.getText().toString();

        //Validar nombre o email
        View focusView = null;
        if (TextUtils.isEmpty(nombreAmigo)) {
            searchView.setError(getString(R.string.error_field_required));
            focusView = searchView;
            correcto = false;
        }
        else if (!UtilTextValidator.isStringLarge(nombreAmigo)) {
            searchView.setError(getString(R.string.error_field_short));
            focusView = searchView;
            correcto = false;
        }
        else if ((!UtilTextValidator.isEmailValid(nombreAmigo)) && (nombreAmigo.split(" ").length < 2))
        {
            searchView.setError(getString(R.string.error_surname_field));
            focusView = searchView;
            correcto = false;

        }

        //Concepto
        if (TextUtils.isEmpty(concepto)) {
            mConcepto.setError(getString(R.string.error_field_required));
            focusView = mConcepto;
            correcto = false;
        }
        else if (!UtilTextValidator.isStringLarge(concepto)) {
            mConcepto.setError(getString(R.string.error_field_short));
            focusView = mConcepto;
            correcto = false;
        }

        //Cantidad
        if (TextUtils.isEmpty(cantidadString)) {
            mCantidad.setError(getString(R.string.error_field_required));
            focusView = mCantidad;
            correcto = false;
        }
        else
        {
            try
            {
                Double.parseDouble(cantidadString);
            }
            catch (NumberFormatException e)
            {
                mCantidad.setError(getString(R.string.error_concept));
                focusView = mCantidad;
                correcto = false;
            }
        }


        //Si los datos son correctos, enviar
        if ((!correcto) && (focusView != null))
        {
            focusView.requestFocus();
        }
        else
        {
            showProgress(true);

            String emailOrigen = UtilUI.getEmail(this);
            //Email
            if (nombreAmigo.contains("@"))
            {
                Comando comando = new NuevoAmigoComando(KPantallas.PANTALLA_NUEVA_DEUDA_OTROS);
                String emailDestino = nombreAmigo;
                comando.ejecutar(emailDestino, emailOrigen);
            }
            //Nombre
            else
            {
                Comando comando = new NuevoInvitadoComando(KPantallas.PANTALLA_NUEVA_DEUDA_OTROS);
                String[] partesNombre = nombreAmigo.split(" ");
                String nombre = partesNombre[0];
                StringBuilder apellidos = new StringBuilder(partesNombre[1]);
                if (partesNombre.length > 2)
                {
                    for (int i=2;i<partesNombre.length;i++)
                    {
                        apellidos.append(" ");
                        apellidos.append(partesNombre[i]);
                    }
                }

                Usuario usuario = new Usuario(nombre,apellidos.toString());
                comando.ejecutar(usuario, emailOrigen);
            }

        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAmigo(EventAmigo event) {
        long amigo = event.getIdAmigo();

        //Si es error de conexión
        if (event.getCodigo() == -1)
        {
            showProgress(false);
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, actualizar listado amigos
        else if (amigo == -1)
        {
            showProgress(false);
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        else
        {
            //Llamar a servicio de nueva deuda
            Comando comando = new NuevaDeudaComando(KPantallas.PANTALLA_NUEVA_DEUDA_OTROS);
            long idOrigen = UtilUI.getIdUsuario(this);
            long idDestino = amigo;
            String concepto = mConcepto.getText().toString();
            double cantidad = Double.parseDouble(mCantidad.getText().toString());

            TipoDeuda tipo = null;
            //Ver tipo deudas
            if (tipoDeuda == KPantallas.PANTALLA_DEUDAS_OTROS)
            {
                tipo = TipoDeuda.DEBEN;
                comando.ejecutar(idDestino, idOrigen, cantidad, concepto, tipo);
            }
            else if (tipoDeuda == KPantallas.PANTALLA_MIS_DEUDAS)
            {
                tipo = TipoDeuda.DEBO;
                comando.ejecutar(idOrigen, idDestino, cantidad, concepto, tipo);
            }


        }

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeudas(EventDeudas event) {
        List<Deuda> deudas = event.getDeudas();
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
            //Finalizar actividad con éxito
            setResult(RESULT_OK);
            finish();
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
