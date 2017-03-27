package es.jma.prestamigos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.adaptadores.DeudaAdapter;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.TodasDeudasComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.constantes.KReqCode;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.eventbus.EventDeudas;
import es.jma.prestamigos.navegacion.BaseFragment;
import es.jma.prestamigos.utils.ui.UtilSuma;
import es.jma.prestamigos.utils.ui.UtilUI;

import static es.jma.prestamigos.utils.ui.UtilUI.showProgress;


/**
 * Deudas
 * Created by jmiranda
 */
public class DeudasOtrosFragment extends BaseFragment {

    //Parámetro
    public static final String TIPO_DEUDA = "tipoDeuda";
    private int tipoDeuda;
    private TipoDeuda tipo;

    @BindView(R.id.deudas_total)
    TextView tvTotal;
    @BindView(R.id.searchDeudasOtros)
    SearchView searchView;
    @BindView(R.id.rv_deudas)
    RecyclerView rv;
    @BindView(R.id.deudas_progress)
    View mProgressView;
    @BindView(R.id.deudas_view)
    ScrollView mView;
    @BindView(R.id.coordinator_deudas)
    CoordinatorLayout coordinatorLayout;

    private OnFragmentInteractionListener mListener;

    public DeudasOtrosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DeudasOtrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeudasOtrosFragment newInstance(int param1) {
        DeudasOtrosFragment fragment = new DeudasOtrosFragment();
        Bundle args = new Bundle();
        args.putInt(TIPO_DEUDA, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipoDeuda = getArguments().getInt(TIPO_DEUDA);
        }
        setHasOptionsMenu(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = getView(inflater,R.layout.fragment_deudas_otros,container);

        mView.setSmoothScrollingEnabled(true);

        // Barra de búsqueda
        searchView.setVisibility(View.GONE);

        /* Al cerrar, hacer visible*/
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                UtilUI.toggleSearchView(getActionBar(), searchView);
                return false;
            }
        });

        //Tipo de deuda
        tipo = TipoDeuda.TODAS;

        //Ver tipo deudas
        if (tipoDeuda == KPantallas.PANTALLA_DEUDAS_OTROS)
        {
            tipo = TipoDeuda.DEBEN;
        }
        else if (tipoDeuda == KPantallas.PANTALLA_MIS_DEUDAS)
        {
            tipo = TipoDeuda.DEBO;
        }

        //Rellenar lista
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        //List<Deuda> deudas = Deuda.getDatosPrueba();
        List<Deuda> deudas = new ArrayList<>();

        final DeudaAdapter adapter = new DeudaAdapter(tipo, deudas);
        rv.setAdapter(adapter);

        //Otros
        tvTotal.setFocusable(true);
        tvTotal.setFocusableInTouchMode(true);
        tvTotal.requestFocusFromTouch();
        tvTotal.requestFocus();

        //Cambiar título y actualizar lista
        if (tipo.equals(TipoDeuda.DEBEN)) {
            getActionBar().setTitle(R.string.titulo_deudas_pendiente);
        }
        else if (tipo.equals(TipoDeuda.DEBO))
        {
            getActionBar().setTitle(R.string.titulo_mis_deudas);
        }
        actualizarDeudas();

        //Filtro para búsqueda
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                DeudaAdapter deudaAdapter = (DeudaAdapter) rv.getAdapter();
                //Si el texto no está vacío, filtrar
                if (!newText.isEmpty())
                {
                    deudaAdapter.getFilter().filter(newText);
                }
                else
                {
                    deudaAdapter.setDeudas(deudaAdapter.getDeudasSinFiltrar());
                    deudaAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        return v;
    }

    /**
     * Conectarse y descargar deudas
     */
    private void actualizarDeudas()
    {
        Comando deudas = new TodasDeudasComando(KPantallas.PANTALLA_DEUDAS_OTROS);
        long idUsuario = UtilUI.getIdUsuario(getContext());

        //Conectarse
        if (idUsuario != -1)
        {
            showProgress(true, mView, mProgressView, getContext());
            deudas.ejecutar(idUsuario, tipo, false);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        if (tipoDeuda == KPantallas.PANTALLA_DEUDAS_OTROS) {
            inflater.inflate(R.menu.menu_deudas_otros, menu);
        }
        else if (tipoDeuda == KPantallas.PANTALLA_MIS_DEUDAS)
        {
            inflater.inflate(R.menu.menu_deudas_otros, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            //Botón de buscar, activar barra de búsqueda
            case R.id.deudas_otros_buscar:
                UtilUI.toggleSearchView(getActionBar(), searchView);
                return true;
            //Botón de nueva deuda, crear nueva actividad
            case R.id.deudas_otros_nueva:

                Bundle bundle = new Bundle();
                bundle.putInt(TIPO_DEUDA,tipoDeuda);
                start(NuevaDeudaActivity.class, bundle, KReqCode.REQ_CODE_NUEVA_DEUDA);

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Si se vuelve de nueva deuda, actualizar datos
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == KReqCode.REQ_CODE_NUEVA_DEUDA);
        {
            if (resultCode == NuevaDeudaActivity.RESULT_OK) {
                actualizarDeudas();
            }
        }
    }

    /**
     * Evento de respuesta de llamadas REST
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeudas(EventDeudas event) {
        List<Deuda> deudas = event.getDeudas();

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
        //En caso contrario, actualizar adapter
        else
        {
            DeudaAdapter adapter = (DeudaAdapter) rv.getAdapter();
            adapter.setDeudasSinFiltrar(deudas);
            adapter.notifyDataSetChanged();

            //Si se está buscando algo, aplicar filtro
            String buscando = searchView.getQuery().toString();
            if ((buscando != null) && (!buscando.isEmpty())) {
                adapter.getFilter().filter(buscando);
            }
        }

        //Actualizar suma
        double suma = UtilSuma.sumarDeudas(deudas);
        tvTotal.setText(String.valueOf(suma) +" €");

        showProgress(false, mView, mProgressView, getContext());

    };

}
