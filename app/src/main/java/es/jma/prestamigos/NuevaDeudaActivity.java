package es.jma.prestamigos;

import android.content.Intent;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import es.jma.prestamigos.navegacion.BaseActivity;

import static es.jma.prestamigos.DeudasOtrosFragment.TIPO_DEUDA;

public class NuevaDeudaActivity extends BaseActivity {
    private int tipoDeuda = -1;

    @BindView(R.id.searchNuevaDeuda)
    SearchView searchView;
    private  SimpleCursorAdapter mAdapter;

    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };

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
            //Si es el de terminar
            case R.id.accion_anyadir:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nueva_deuda, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_deuda);

        //Leer parámetros
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tipoDeuda = bundle.getInt(TIPO_DEUDA, -1);
        }

        //Barra de búsqueda
        searchView.setIconified(false);
        searchView.clearFocus();

        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};

        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);;
        searchView.setSuggestionsAdapter(mAdapter);

        //Al seleccionar sugerencia, rellenar
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                searchView.setQuery(SUGGESTIONS[position],false);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });

        //Al introducir texto, mirar sugerencias
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });
    }


    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
