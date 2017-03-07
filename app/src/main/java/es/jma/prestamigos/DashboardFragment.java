package es.jma.prestamigos;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.ResumenComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.ResumenDeuda;
import es.jma.prestamigos.eventbus.EventResumen;
import es.jma.prestamigos.navegacion.BaseFragment;
import es.jma.prestamigos.utils.ui.UtilUI;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.resumen_progress)
    View mProgressView;
    @BindView(R.id.resumen)
    View mView;
    @BindView(R.id.coordinator_resumen)
    CoordinatorLayout coordinatorLayout;

    //Total
    @BindView(R.id.tv_dashboard_valor_deudas_totales)
    TextView tvDeudasTotales;
    //Debo
    @BindView(R.id.tv_dashboard_valor_debo)
    TextView tvDebo;
    //Deben
    @BindView(R.id.tv_dashboard_valor_medeben)
    TextView tvDeben;
    //Numero debo
    @BindView(R.id.tv_dashboard_valor_numero_debo)
    TextView tvNumDebo;
    //Numero deben
    @BindView(R.id.tv_dashboard_valor_numero_medeben)
    TextView tvNumDeben;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
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
        View v = getView(inflater,R.layout.fragment_dashboard,container);

        //Cambiar título
        getActionBar().setTitle(R.string.titulo_resumen);

        //Obtener información
        actualizarResumen();

        return v;
    }

    /**
     * Conectarse y actualizar
     */
    private void actualizarResumen()
    {
        Comando resumen = new ResumenComando(KPantallas.PANTALLA_RESUMEN);
        long idUsuario = UtilUI.getIdUsuario(getContext());

        //Conectarse
        if (idUsuario != -1)
        {
            showProgress(true);
            resumen.ejecutar(idUsuario);
        }
    }


    /**
     * Evento de respuesta de llamadas REST
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResumen(EventResumen event) {
        ResumenDeuda resumenDeuda = event.getResumenDeudas();

        //Si no existe, informar
        if (event.getCodigo() == 1)
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_login), Snackbar.LENGTH_LONG)
                    .show();
        }
        //Si es error de conexión
        else if ((event.getCodigo() == -1) || (resumenDeuda == null))
        {
            Snackbar.make(coordinatorLayout, getResources().getText(R.string.msg_error_conexion), Snackbar.LENGTH_LONG)
                    .show();
        }
        //En caso contrario, actualizar datos
        else
        {
            String moneda = "€";
            tvDeudasTotales.setText(resumenDeuda.getTotal()+"€");
            tvDebo.setText(resumenDeuda.getTodalDebo()+"€");
            tvDeben.setText(resumenDeuda.getTotalMeDeben()+"€");
            tvNumDebo.setText(String.valueOf(resumenDeuda.getNumDeudasDebo()));
            tvNumDeben.setText(String.valueOf(resumenDeuda.getNumDeudasMeDeben()));
        }

        showProgress(false);

    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DashboardFragment.OnFragmentInteractionListener) {
            mListener = (DashboardFragment.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

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
