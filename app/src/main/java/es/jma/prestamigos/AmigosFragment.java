package es.jma.prestamigos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.adaptadores.AmigosAdapter;
import es.jma.prestamigos.adaptadores.DeudaAdapter;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.navegacion.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AmigosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AmigosFragment extends BaseFragment {

    @BindView(R.id.rv_amigos)
    RecyclerView rv;

    private OnFragmentInteractionListener mListener;

    public AmigosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = getView(inflater,R.layout.fragment_amigos,container);

        //Cambiar título
        getActionBar().setTitle(R.string.titulo_amigos);

        //Rellenar lista
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<Usuario> amigos = Usuario.getDatosPrueba();

        AmigosAdapter adapter = new AmigosAdapter(amigos);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_amigo, menu);
    }

}
