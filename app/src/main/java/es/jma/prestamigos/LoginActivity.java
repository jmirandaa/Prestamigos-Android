package es.jma.prestamigos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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

import static es.jma.prestamigos.constantes.KShared.CLAVE_AVATAR;
import static es.jma.prestamigos.constantes.KShared.CLAVE_EMAIL;
import static es.jma.prestamigos.constantes.KShared.CLAVE_ID;
import static es.jma.prestamigos.constantes.KShared.CLAVE_NOMBRE;
import static es.jma.prestamigos.constantes.KShared.CLAVE_PASSWORD;
import static es.jma.prestamigos.constantes.KShared.CLAVE_PREF;
import static es.jma.prestamigos.utils.ui.UtilUI.showProgress;

/**
 * Login
 * Created by jmiranda
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
    @BindView(R.id.textViewOlvidado)
    TextView tvOlvidado;

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

        //Botón olvidado
        tvOlvidado.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString(KShared.BUNDLE_AMIGO_EMAIL, mEmailView.getText().toString());
                start(RecuperarPasswordActivity.class, extras, KReqCode.REQ_CODE_PASSWORD_OLVIDADO);
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
            showProgress(true, mLoginFormView, mProgressView, this);
            Comando login = new LoginComando(KPantallas.PANTALLA_LOGIN);
            login.ejecutar(email,password);

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
        showProgress(false, mLoginFormView, mProgressView, this);

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

