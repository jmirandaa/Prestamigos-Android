package es.jma.prestamigos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import es.jma.prestamigos.comandos.CambiarPasswordComando;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.EnviarCodigoComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.constantes.KShared;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventGenerico;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilTextValidator;

import static es.jma.prestamigos.constantes.KAccion.ACCION_RECUPERAR_CAMBIAR_PASS;
import static es.jma.prestamigos.constantes.KAccion.ACCION_RECUPERAR_CODIGO;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_EMAIL;
import static es.jma.prestamigos.utils.ui.UtilUI.showProgress;

/**
 * Recuperar password
 * Created by jmiranda
 */
public class RecuperarPasswordActivity extends BaseActivity {

    @BindView(R.id.activity_recuperar_password)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recuperar_progress)
    View mProgressView;
    @BindView(R.id.formulario_recuperar)
    View mView;

    @BindView(R.id.recuperar_email)
    EditText mEmailView;
    @BindView(R.id.recuperar_codigo)
    EditText mCodeView;
    @BindView(R.id.recuperar_password)
    EditText mPasswordView;
    @BindView(R.id.recuperar_password_repetir)
    EditText mPasswordRepView;
    @BindView(R.id.recover_button)
    Button recoverButton;
    @BindView(R.id.code_button)
    Button codeButton;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int idItem = item.getItemId();
        //Ver botón pulsado
        switch (idItem)
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        //Leer parámetros
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String email = bundle.getString(BUNDLE_AMIGO_EMAIL, "");

            //Actualizar campos
            mEmailView.setText(email);
        }
    }

    /**
     * Enviar código de seguridad
     * @param view
     */
    @OnClick(R.id.code_button)
    public void accionObtenerCodigo(View view)
    {
        boolean correcto = true;
        String email = mEmailView.getText().toString();

        View focusView = null;
        //Validar email
        if (!UtilTextValidator.isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            correcto = false;
        }

        //En caso de que sí, enviar
        if (correcto) {
            showProgress(true, mView, mProgressView, this);

            Comando enviarCodigo = new EnviarCodigoComando(KPantallas.PANTALLA_RECORDAR_CONTRASEÑA);
            enviarCodigo.ejecutar(email);

        }
        else
        {
            if (focusView != null) {
                focusView.requestFocus();
            }
        }
    }

    /**
     * Comprobar datos y enviar nueva contraseña
     * @param view
     */
    @OnClick(R.id.recover_button)
    public void accionCambiarPassword(View view) {
        boolean correcto = true;

        String email = mEmailView.getText().toString();
        String cod = mCodeView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRep = mPasswordRepView.getText().toString();

        View focusView = null;
        //Validar email
        if (!UtilTextValidator.isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            correcto = false;
        }

        //Código
        if (TextUtils.isEmpty(cod)) {
            mCodeView.setError(getString(R.string.error_field_required));
            focusView = mCodeView;
            correcto = false;
        }

        //Contraseña
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

        //Repetir contraseña
        if (!password.equals(passwordRep)) {
            mPasswordRepView.setError(getString(R.string.error_field_repeat));
            focusView = mPasswordRepView;
            correcto = false;
        }


        //En caso de que sí, enviar
        if (correcto) {
            showProgress(true, mView, mProgressView, this);

            Usuario usuario = new Usuario();
            usuario.setNombre(cod);
            usuario.setEmail(email);
            usuario.setPassword(password);

            Comando cambiarPassword = new CambiarPasswordComando(KPantallas.PANTALLA_RECORDAR_CONTRASEÑA);
            cambiarPassword.ejecutar(usuario);
        }
        else
        {
            if (focusView != null) {
                focusView.requestFocus();
            }
        }
    }

    /**
     * Evento de respuesta de llamadas REST
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRecuperar(EventGenerico<Boolean> event) {
        Boolean resultado = event.getContenido();

        //Si es error de conexión
        if ((event.getCodigo() == -1) || ((!resultado) && (event.getCodigo() != ACCION_RECUPERAR_CAMBIAR_PASS)))
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //Si se ha enviado el código, habilitar el otro botón
        else if (event.getCodigo() == ACCION_RECUPERAR_CODIGO)
        {
            recoverButton.setEnabled(true);
        }
        //En caso contrario, volver al login
        else if (event.getCodigo() == ACCION_RECUPERAR_CAMBIAR_PASS)
        {
            if (!resultado)
            {
                Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_codigo), Snackbar.LENGTH_LONG)
                        .show();
            }
            else {
                Intent intent = new Intent();
                intent.putExtra(KShared.BUNDLE_USUARIO_EMAIL, mEmailView.getText().toString());
                intent.putExtra(KShared.BUNDLE_USUARIO_PASSWORD, mPasswordView.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        showProgress(false, mView, mProgressView, this);

    };

}
