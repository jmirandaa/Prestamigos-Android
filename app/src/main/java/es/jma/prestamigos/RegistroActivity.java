package es.jma.prestamigos;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import es.jma.prestamigos.navegacion.BaseActivity;

public class RegistroActivity extends BaseActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int idItem = item.getItemId();
        //Ver botón pulsado
        switch (idItem)
        {
            //Si es el de terminar
            case R.id.accion_registro_siguiente :
                //Comprobar si los parámetros son correctos

                //En caso de que sí, ir a principal
                //Cambiar de actividad
                start(PrincipalActivity.class,true);
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
    }
}
