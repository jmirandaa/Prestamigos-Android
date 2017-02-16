package es.jma.prestamigos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.adaptadores.DeudaAdapter;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.enums.TipoDeuda;
import es.jma.prestamigos.navegacion.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistorialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HistorialFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.rv_historial)
    RecyclerView rv;

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
        List<Deuda> deudas = Deuda.getDatosPrueba();

        DeudaAdapter adapter = new DeudaAdapter(TipoDeuda.TODAS,deudas);
        rv.setAdapter(adapter);

        return v;
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
}
