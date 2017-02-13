package es.jma.prestamigos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.adaptadores.OperacionAdapter;
import es.jma.prestamigos.dominio.Operacion;
import es.jma.prestamigos.navegacion.BaseActivity;

public class DetallesDeudaActivity extends BaseActivity {

    @BindView(R.id.rv_operaciones)
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_deuda);

        //Rellenar lista
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<Operacion> operaciones = Operacion.getDatosPrueba();

       OperacionAdapter adapter = new OperacionAdapter(operaciones);
       rv.setAdapter(adapter);
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
