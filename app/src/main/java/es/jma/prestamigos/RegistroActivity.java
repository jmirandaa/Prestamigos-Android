package es.jma.prestamigos;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mindorks.paracamera.Camera;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.NuevoUsuarioComando;
import es.jma.prestamigos.constantes.KAccion;
import es.jma.prestamigos.constantes.KMensajes;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.constantes.KShared;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventRegistro;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilTextValidator;

public class RegistroActivity extends BaseActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA_PERMISSION = 2;
    static final int RESULT_OK = 10;
    static final int RESULT_KO = 11;

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

    Camera camera;

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
            //Si es el de terminar
            case R.id.accion_registro_siguiente :
                registrar();
                break;
        }
        return true;
    }

    /**
     * Conexión con el servicio de registrar
     */
    private void registrar()
    {
        boolean correcto = true;
        //Comprobar si los parámetros son correctos
        String nombre =  mNombreView.getText().toString();
        String apellidos =  mApellidosView.getText().toString();
        String email =  mEmailView.getText().toString();
        String emailRep = mEmailRepView.getText().toString();
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
            Comando registro = new NuevoUsuarioComando(KPantallas.PANTALLA_REGISTRO);
            Usuario usuario = new Usuario(nombre, apellidos, email, password);
            registro.ejecutar(usuario);
        }
        else
        {
            if (focusView != null) {
                focusView.requestFocus();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);
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
        setContentView(R.layout.activity_registro);

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(50)
                .setImageHeight(300)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    /**
     * Llamado al sacar foto
     * @param view
     */
    @OnClick(R.id.registro_boton_foto)
    public void submit(View view) {
        //Sólo entrar en la cámara si está disponible
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            //Solicitar permisos de cámara
            if (mayRequestCamera())
            {
                //Abrir cámara
                try {
                    camera.takePicture();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            else {
                return;
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = camera.getCameraBitmap();
            if (imageBitmap != null) {
                iv_foto.setImageBitmap(imageBitmap);
            }
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                iv_foto.setImageBitmap(imageBitmap);
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Abrir cámara
                    try {
                        camera.takePicture();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                return;
            }

        }
    }

    /**
     * Solicitar permisos de cámara
     * @return
     */
    private boolean mayRequestCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Log.d("camera","camara");
            Snackbar.make(coordinatorLayout, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        }
                    });
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }

    /**
     * Evento de respuesta de llamadas REST
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRegistro(EventRegistro event) {
        Boolean resultado = event.isResultado();

        //Errores por parte del servidor
        if (KMensajes.MSG_NUEVO_USUARIO_EXISTE.equals(event.getMsg()))
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.error_user_exist), Snackbar.LENGTH_LONG)
                    .show();
        }
        else if (KMensajes.MSG_ERROR_NULL.equals(event.getCodigo()))
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.error_user_field), Snackbar.LENGTH_LONG)
                    .show();
        }
        //Si es error de conexión
        else if ((event.getCodigo() == -1) || (!resultado))
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, volver al login
        else
        {
            Intent intent = new Intent();
            intent.putExtra(KShared.BUNDLE_USUARIO_EMAIL, mEmailView.getText().toString());
            intent.putExtra(KShared.BUNDLE_USUARIO_PASSWORD, mPasswordView.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }

        showProgress(false);

    };

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
