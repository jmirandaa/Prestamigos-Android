package es.jma.prestamigos;

import static es.jma.prestamigos.constantes.KShared.*;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
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
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.eventbus.EventDeudas;
import es.jma.prestamigos.navegacion.BaseFragment;
import es.jma.prestamigos.utils.ui.UtilUI;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeudasOtrosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeudasOtrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeudasOtrosFragment extends BaseFragment {

    //Parámetro
    public static final String TIPO_DEUDA = "tipoDeuda";
    private int tipoDeuda;

    @BindView(R.id.deudas_total)
    TextView tvTotal;
    @BindView(R.id.searchDeudasOtros)
    SearchView searchView;
    @BindView(R.id.rv_deudas)
    RecyclerView rv;
    @BindView(R.id.deudas_progress)
    View mProgressView;
    @BindView(R.id.deudas_view)
    View mView;
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

        //Cambiar título
        if (tipoDeuda == KPantallas.PANTALLA_DEUDAS_OTROS) {
            getActionBar().setTitle(R.string.titulo_deudas_pendiente);
        }
        else if (tipoDeuda == KPantallas.PANTALLA_MIS_DEUDAS)
        {
            getActionBar().setTitle(R.string.titulo_mis_deudas);
        }

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Si el texto está vacío, buscar todo
                if (newText.isEmpty())
                {

                }
                return false;
            }
        });

        //Rellenar lista
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        //List<Deuda> deudas = Deuda.getDatosPrueba();
        List<Deuda> deudas = new ArrayList<>();

        DeudaAdapter adapter = new DeudaAdapter(deudas);
        rv.setAdapter(adapter);

        actualizarDeudas();

        //Otros
        tvTotal.setFocusable(true);
        tvTotal.setFocusableInTouchMode(true);
        tvTotal.requestFocusFromTouch();
        tvTotal.requestFocus();

        return v;
    }

    /**
     * Conectarse y descargar deudas
     */
    private void actualizarDeudas()
    {
        Comando deudas = new TodasDeudasComando(KPantallas.PANTALLA_DEUDAS_OTROS);

        SharedPreferences shared = getContext().getSharedPreferences(CLAVE_PREF, Context.MODE_PRIVATE);
        long idUsuario = shared.getLong(CLAVE_ID,-1);
        if (idUsuario != -1)
        {
            showProgress(true);
            deudas.ejecutar(idUsuario, TipoDeuda.DEBEN, false);
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
                start(NuevaDeudaActivity.class, bundle);

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Evento de respuesta de llamadas REST
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeudas(EventDeudas event) {
        List<Deuda> deudas = event.getDeudas();
        showProgress(false);

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
            adapter.setDeudas(deudas);
            adapter.notifyDataSetChanged();
        }

    };

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
