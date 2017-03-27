package es.jma.prestamigos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static es.jma.prestamigos.utils.ui.UtilUI.showProgress;


/**
 * Historial
 * Created by jmiranda
 */
public class HistorialFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.deudas_progress)
    View mProgressView;
    @BindView(R.id.rv_historial)
    RecyclerView rv;
    @BindView(R.id.coordinator_historial)
    CoordinatorLayout coordinatorLayout;

    public HistorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = getView(inflater,R.layout.fragment_historial, container);

        //Cambiar título
        getActionBar().setTitle(R.string.titulo_historial);

        //Añadir recyclerview
        //Rellenar lista
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Deuda> deudas = new ArrayList<>();

        DeudaAdapter adapter = new DeudaAdapter(TipoDeuda.TODAS,deudas);
        rv.setAdapter(adapter);
        actualizarHistorial();

        return v;
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

    /**
     * Conectarse y actualizar
     */
    private void actualizarHistorial()
    {
        Comando deudas = new TodasDeudasComando(KPantallas.PANTALLA_HISTORIAL);
        long idUsuario = UtilUI.getIdUsuario(getContext());

        //Conectarse
        if (idUsuario != -1)
        {
            showProgress(true, rv, mProgressView, getContext());
            deudas.ejecutar(idUsuario, TipoDeuda.TODAS, true);
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
            adapter.setDeudas(deudas);
            adapter.notifyDataSetChanged();
        }

        showProgress(false, rv, mProgressView, getContext());

    };

}
