package es.jma.prestamigos;

import static es.jma.prestamigos.constantes.KShared.*;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.jma.prestamigos.comandos.ActualizarUsuarioComando;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.LoginComando;
import es.jma.prestamigos.comandos.NuevoUsuarioComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.constantes.KShared;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventGenerico;
import es.jma.prestamigos.navegacion.BaseFragment;
import es.jma.prestamigos.utils.ui.UtilTextValidator;
import es.jma.prestamigos.utils.ui.UtilUI;
import okhttp3.internal.Util;

/**
 * Created by jmiranda on 13/03/17.
 */

public class PerfilFragment extends BaseFragment {
    private PerfilFragment.OnFragmentInteractionListener mListener;

    @BindView(R.id.registro_iv_foto)
    CircleImageView iv_foto;
    @BindView(R.id.activity_registro)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.nuevo_usuario_progress)
    View mProgressView;
    @BindView(R.id.formulario_registro)
    View mView;

    @BindView(R.id.registro_nombre)
    EditText mNombreView;
    @BindView(R.id.registro_apellidos)
    EditText mApellidosView;
    @BindView(R.id.registro_email)
    EditText mEmailView;
    @BindView(R.id.registro_email_repetir)
    EditText mEmailRepView;
    @BindView(R.id.registro_password)
    EditText mPasswordView;
    @BindView(R.id.registro_password_repetir)
    EditText mPasswordRepView;

    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_perfil, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            //Botón de actualizar perfil
            case R.id.accion_actualizar_perfil:
                actualizarPerfil();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = getView(inflater,R.layout.activity_registro,container);

        //Email no se puede cambiar
        mEmailView.setEnabled(false);

        //Cambiar título
        getActionBar().setTitle(R.string.titulo_perfil);

        //Obtener información
        cargarDatos();

        return v;
    }

    /**
     * Conexión con el servicio de registrar
     */
    private void actualizarPerfil()
    {
        boolean correcto = true;
        //Comprobar si los parámetros son correctos
        String nombre =  mNombreView.getText().toString();
        String apellidos =  mApellidosView.getText().toString();
        String email =  mEmailView.getText().toString();
        String emailRep = email;
        String password =  mPasswordView.getText().toString();
        String passwordRep = mPasswordRepView.getText().toString();

        View focusView = null;
        if (TextUtils.isEmpty(nombre)) {
            mNombreView.setError(getString(R.string.error_field_required));
            focusView = mNombreView;
            correcto = false;
        }
        if (TextUtils.isEmpty(apellidos)) {
            mApellidosView.setError(getString(R.string.error_field_required));
            focusView = mApellidosView;
            correcto = false;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            correcto = false;
        }
        else if (!UtilTextValidator.isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            correcto = false;
        }
        if (!email.equals(emailRep)) {
            mEmailRepView.setError(getString(R.string.error_field_repeat));
            focusView = mEmailRepView;
            correcto = false;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            correcto = false;
        }
        else if (!UtilTextValidator.isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            correcto = false;
        }
        if (!password.equals(passwordRep)) {
            mPasswordRepView.setError(getString(R.string.error_field_repeat));
            focusView = mPasswordRepView;
            correcto = false;
        }

        //En caso de que sí, enviar
        if (correcto) {
            showProgress(true);

            //Actualizar shared
            SharedPreferences shared = getContext().getSharedPreferences(CLAVE_PREF, Context.MODE_PRIVATE);
            editor = shared.edit();

            //Nombre y apellidos
            StringBuilder nombreApellidos = new StringBuilder("");
            if (nombre != null)
            {
                nombreApellidos.append(nombre);
            }
            if (apellidos != null)
            {
                nombreApellidos.append(" ");
                nombreApellidos.append(apellidos);
            }
            editor.putString(CLAVE_NOMBRE,nombreApellidos.toString());
            editor.putString(CLAVE_PASSWORD, password);

            //Ejecutar
            Comando actualizar = new ActualizarUsuarioComando(KPantallas.PANTALLA_PERFIL);
            Usuario usuario = new Usuario(nombre, apellidos, email, password);
            usuario.setId(UtilUI.getIdUsuario(getContext()));
            actualizar.ejecutar(usuario);
        }
        else
        {
            if (focusView != null) {
                focusView.requestFocus();
            }
        }
    }

    /**
     * Cargar los datos de usuario
     */
    private void cargarDatos()
    {
        // Pantalla carga
        showProgress(true);
        Comando login = new LoginComando(KPantallas.PANTALLA_PERFIL);
        String email = UtilUI.getEmail(getContext());
        String password = UtilUI.getPassword(getContext());
        login.ejecutar(email,password);
    }

    /**
     * Evento de respuesta genérico de llamadas REST
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventActualizado(EventGenerico<Boolean> event) {
        Object objeto = event.getContenido();
        if (objeto instanceof Boolean) {
            Boolean contenido = event.getContenido();

            //Si es error de conexión
            if ((event.getCodigo() == -1) || (!contenido)) {
                Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                        .show();
            }
            //En caso contrario, indicar actualización
            else {
                if (editor != null)
                {
                    editor.commit();
                }

                Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_perfil_actualizado), Snackbar.LENGTH_LONG)
                        .show();
            }

            showProgress(false);
        }
    };

    /**
     * Evento de respuesta genérico de llamadas REST
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUsuario(EventGenerico<Usuario> event) {
        Object objeto = event.getContenido();
        if (objeto instanceof Usuario) {
            Usuario usuario = event.getContenido();

            //Si es error de conexión
            if ((event.getCodigo() == -1) || (usuario == null)) {
                Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                        .show();
            }
            //En caso contrario, actualizar datos de usuario
            else {
                mNombreView.setText(usuario.getNombre());
                mApellidosView.setText(usuario.getApellidos());
                mEmailView.setText(usuario.getEmail());
                mEmailRepView.setVisibility(View.GONE);
                mPasswordView.setText(UtilUI.getPassword(getContext()));
                mPasswordRepView.setText(UtilUI.getPassword(getContext()));

            }

            showProgress(false);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PerfilFragment.OnFragmentInteractionListener) {
            mListener = (PerfilFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Barra de progreso
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
