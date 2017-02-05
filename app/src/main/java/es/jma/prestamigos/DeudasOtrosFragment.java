package es.jma.prestamigos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.jma.prestamigos.adaptadores.DeudaAdapter;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.utils.ui.UtilUI;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeudasOtrosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeudasOtrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeudasOtrosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TIPO_DEUDA = "tipoDeuda";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int tipoDeuda;
    private String mParam2;

    private SearchView searchView;
    private ActionBar actionBar;
    private OnFragmentInteractionListener mListener;

    public DeudasOtrosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeudasOtrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeudasOtrosFragment newInstance(int param1, String param2) {
        DeudasOtrosFragment fragment = new DeudasOtrosFragment();
        Bundle args = new Bundle();
        args.putInt(TIPO_DEUDA, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipoDeuda = getArguments().getInt(TIPO_DEUDA);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_deudas_otros, container, false);

        //Cambiar título
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (tipoDeuda == KPantallas.PANTALLA_DEUDAS_OTROS) {
            actionBar.setTitle(R.string.titulo_deudas_pendiente);
        }
        else if (tipoDeuda == KPantallas.PANTALLA_MIS_DEUDAS)
        {
            actionBar.setTitle(R.string.titulo_mis_deudas);
        }

        // Barra de búsqueda
        searchView = (SearchView) v.findViewById(R.id.searchDeudasOtros);
        searchView.setVisibility(View.GONE);

        /* Al cerrar, hacer visible*/
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                UtilUI.toggleSearchView(actionBar, searchView);
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
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv_deudas);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<Deuda> deudas = Deuda.getDatosPrueba();

        DeudaAdapter adapter = new DeudaAdapter(deudas);
        rv.setAdapter(adapter);



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        if (tipoDeuda == KPantallas.PANTALLA_DEUDAS_OTROS) {
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
                UtilUI.toggleSearchView(actionBar, searchView);
                return true;
            //Botón de nueva deuda, crear nueva actividad
            case R.id.deudas_otros_nueva:

                Intent intent = new Intent(getActivity(), NuevaDeudaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TIPO_DEUDA,tipoDeuda);
                intent.putExtras(bundle);
                startActivity(intent);

                /*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new AmigosFragment();
                ft.replace(R.id.mainFrame, fragment);
                ft.commit();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
