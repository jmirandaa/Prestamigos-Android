package es.jma.prestamigos;

import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_NOMBRE;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_APELLIDOS;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_EMAIL;
import static es.jma.prestamigos.constantes.KShared.BUNDLE_AMIGO_ID;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import es.jma.prestamigos.navegacion.BaseActivity;

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
