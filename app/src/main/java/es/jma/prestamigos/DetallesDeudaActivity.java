package es.jma.prestamigos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import es.jma.prestamigos.navegacion.BaseActivity;

public class DetallesDeudaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_deuda);
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
