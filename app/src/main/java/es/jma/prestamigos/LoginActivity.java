package es.jma.prestamigos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import static es.jma.prestamigos.constantes.KShared.*;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.LoginComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.constantes.KReqCode;
import es.jma.prestamigos.constantes.KShared;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventLogin;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilTextValidator;
import es.jma.prestamigos.utils.ui.UtilUI;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    // UI references.
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.coordinator_login)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.email_sign_in_button)
    Button mEmailSignInButton;
    @BindView(R.id.textViewRegistro)
    TextView tvRegistro;

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        //Botón acceder
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });

        //Botón registro
        tvRegistro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start(RegistroActivity.class,KReqCode.REQ_CODE_REGISTRO);
            }
        });

        //Si ya se había iniciado sesión, saltar
        long idUsuario = UtilUI.getIdUsuario(this);
        if (idUsuario != -1)
        {
            start(PrincipalActivity.class,true);
        }

    }

    /**
     * Acción login
     */
    private void login()
    {

        // Resetear errores.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Email y contraseña
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Comprobar password
        if (!TextUtils.isEmpty(password) && !UtilTextValidator.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Comprobar email
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!UtilTextValidator.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // Hubo error
            focusView.requestFocus();
        } else {
            //Guardar contraseña
            SharedPreferences shared = getSharedPreferences(CLAVE_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString(CLAVE_PASSWORD, password);
            editor.commit();

            // Pantalla carga
            showProgress(true);
            Comando login = new LoginComando(KPantallas.PANTALLA_LOGIN);
            login.ejecutar(email,password);

        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    /**
     * Responder al resultado del LogIn
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(EventLogin event) {
        Usuario usuario = event.getUsuario();
        showProgress(false);

        //Si no existe, informar
        if (event.getCodigo() == 1)
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_login), Snackbar.LENGTH_LONG)
                    .show();
        }
        //Si es error de conexión
        else if (event.getCodigo() == -1)
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, avanzar de pantalla
        else
        {
            //Guardar datos
            SharedPreferences shared = getSharedPreferences(CLAVE_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            //Id usuario
            editor.putLong(CLAVE_ID,usuario.getId());
            //Email
            editor.putString(CLAVE_EMAIL, usuario.getEmail());
            //Avatar
            String avatar = usuario.getAvatarBase64();
            if (avatar != null)
            {
                editor.putString(CLAVE_AVATAR, avatar);
            }
            //Nombre y apellidos
            String nombre = usuario.getNombre();
            String apellidos = usuario.getApellidos();
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
            //Commit
            editor.commit();

            //Ir a pantalla principal
            start(PrincipalActivity.class,true);
        }

    };

    /**
     * Acción al terminar registro
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == KReqCode.REQ_CODE_REGISTRO);
        {
            if (resultCode == RegistroActivity.RESULT_OK) {
                //Rellenar email y password
                String email = intent.getStringExtra(KShared.BUNDLE_USUARIO_EMAIL);
                String password = intent.getStringExtra(KShared.BUNDLE_USUARIO_PASSWORD);
                if ((email != null) && (password != null))
                {
                    mEmailView.setText(email);
                    mPasswordView.setText(password);
                }
            }
        }
    }
}

