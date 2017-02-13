package es.jma.prestamigos;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mindorks.paracamera.Camera;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.jma.prestamigos.navegacion.BaseActivity;

public class RegistroActivity extends BaseActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA_PERMISSION = 2;
    static final int RESULT_OK = 10;
    static final int RESULT_KO = 11;

    @BindView(R.id.registro_iv_foto)
    CircleImageView iv_foto;
    @BindView(R.id.activity_registro)
    CoordinatorLayout coordinatorLayout;
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
                //Comprobar si los parámetros son correctos

                //En caso de que sí, comprobar parámetros
                setResult(RESULT_OK);
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);
        return true;
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
}
