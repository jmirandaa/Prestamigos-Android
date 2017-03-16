package es.jma.prestamigos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.adaptadores.AmigosAdapter;
import es.jma.prestamigos.adaptadores.DeudaAdapter;
import es.jma.prestamigos.comandos.AmigosComando;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.NuevoAmigoComando;
import es.jma.prestamigos.comandos.NuevoInvitadoComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.constantes.KReqCode;
import es.jma.prestamigos.dominio.Deuda;
import es.jma.prestamigos.dominio.Usuario;
import es.jma.prestamigos.eventbus.EventAmigo;
import es.jma.prestamigos.eventbus.EventUsuarios;
import es.jma.prestamigos.navegacion.BaseFragment;
import es.jma.prestamigos.utils.ui.UtilTextValidator;
import es.jma.prestamigos.utils.ui.UtilUI;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AmigosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AmigosFragment extends BaseFragment {

    @BindView(R.id.rv_amigos)
    RecyclerView rv;
    @BindView(R.id.amigos_progress)
    View mProgressView;
    @BindView(R.id.coordinator_amigos)
    CoordinatorLayout coordinatorLayout;

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
        List<Usuario> amigos = new ArrayList<>();

        AmigosAdapter adapter = new AmigosAdapter(amigos, this);
        rv.setAdapter(adapter);

        //Actualizar datos
        actualizarAmigos();
        return v;
    }

    /**
     * Actualizar listado de amigos
     */
    public void actualizarAmigos()
    {
        Comando amigos = new AmigosComando(KPantallas.PANTALLA_AMIGOS);
        String email = UtilUI.getEmail(getContext());

        //Conectarse
        if ((email != null) && (!email.isEmpty()))
        {
            showProgress(true);
            amigos.ejecutar(email);
        }
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
     * Responder a eventos de usuarios
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUsuarios(EventUsuarios event) {
        List<Usuario> usuarios = event.getUsuarios();

        //Si es error de conexión
        if (event.getCodigo() == -1)
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, actualizar listado amigos
        else
        {
            AmigosAdapter adapter = (AmigosAdapter) rv.getAdapter();
            adapter.setAmigos(usuarios);
            adapter.notifyDataSetChanged();
        }

        showProgress(false);

    };

    /**
     * Responder a eventos de amigo
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAmigo(EventAmigo event) {
        Long idAmigo = event.getIdAmigo();

        //Si es error de conexión
        if (event.getCodigo() == -1)
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, actualizar listado amigos
        else
        {
            actualizarAmigos();
        }

        showProgress(false);

    };

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            //Botón de añadir amigo local
            case R.id.amigos_nuevo:
                //Crear diálogo
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                final View dialogView = layoutInflater.inflate(R.layout.dialog_accion_nuevo_amigo, null);
                builder.setView(dialogView);
                builder.setTitle(getResources().getString(R.string.add_friend));

                final EditText mNombreView = (EditText) dialogView.findViewById(R.id.campo_nombre);
                final EditText mApellidosView = (EditText) dialogView.findViewById(R.id.campo_apellidos);

                //Botón aceptar
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nombre = mNombreView.getText().toString();
                        String apellidos = mApellidosView.getText().toString();

                        Usuario usuario = new Usuario(nombre, apellidos);
                        String emailOrigen = UtilUI.getEmail(getContext());
                        Comando nuevoAmigo = new NuevoInvitadoComando(KPantallas.PANTALLA_AMIGOS);
                        nuevoAmigo.ejecutar(usuario, emailOrigen);
                        showProgress(true);
                    }
                });

                //Botón cancelar
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                //Abrir diálogo
                final AlertDialog dialog = builder.create();
                //Desactivar botón OK
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                mNombreView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (comprobarNombreApellidos(mNombreView, mApellidosView))
                        {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                        else
                        {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                mApellidosView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (comprobarNombreApellidos(mNombreView, mApellidosView))
                        {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                        else
                        {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Comprobar si el nombre y apellidos son correctos
     * @param mNombreView
     * @param mApellidosView
     * @return
     */
    public boolean comprobarNombreApellidos(EditText mNombreView, EditText mApellidosView)
    {
        boolean correcto = true;

        String nombre = mNombreView.getText().toString();
        String apellidos = mApellidosView.getText().toString();

        if (TextUtils.isEmpty(nombre)) {
            mNombreView.setError(getString(R.string.error_field_required));
            correcto = false;
        }
        if (TextUtils.isEmpty(apellidos)) {
            mApellidosView.setError(getString(R.string.error_field_required));
            correcto = false;
        }
        return correcto;
    }

    /**
     * Si se vuelve de borrar amigo, actualizar datos
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == KReqCode.REQ_CODE_BORRAR_AMIGO);
        {
            if (resultCode == DetallesAmigoActivity.RESULT_OK) {
                actualizarAmigos();
            }
        }
    }

    /**
     * Progreso
     * @param show
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            rv.setVisibility(show ? View.GONE : View.VISIBLE);
            rv.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rv.setVisibility(show ? View.GONE : View.VISIBLE);
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
            rv.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
