package es.jma.prestamigos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.jma.prestamigos.adaptadores.OperacionAdapter;
import es.jma.prestamigos.comandos.Comando;
import es.jma.prestamigos.comandos.TodasOpsComando;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.dominio.Operacion;
import es.jma.prestamigos.enums.TipoOperacion;
import es.jma.prestamigos.eventbus.EventOps;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilUI;

public class DetallesDeudaActivity extends BaseActivity {

    @BindView(R.id.activity_detalles_deuda)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.rv_operaciones)
    RecyclerView rv;
    @BindView(R.id.ops_progress)
    View mProgressView;

    @BindView(R.id.deuda_detalle_nombre)
    TextView tvNombre;
    @BindView(R.id.deuda_detalle_fecha)
    TextView tvFecha;
    @BindView(R.id.deuda_detalle_concepto)
    TextView tvConcepto;
    @BindView(R.id.deuda_detalle_cantidad)
    TextView tvCantidad;
    @BindView(R.id.deuda_detalle_restante)
    TextView tvPagado;

    //Atributos de la deuda
    long idDeuda;
    String nombre;
    String fecha;
    String concepto;
    String cantidad;
    String pagado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_deuda);

        //Extraer argumentos
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idDeuda = extras.getLong("idDeuda");
            nombre = extras.getString("nombre");
            fecha = extras.getString("fecha");
            concepto = extras.getString("concepto");
            cantidad = extras.getString("cantidad");
            pagado = extras.getString("pagado");
        }
        else
        {
            nombre = "";
            fecha = "??/??/????";
            concepto = "";
            cantidad = "";
            pagado = "";
        }

        //Rellenar info deuda
        tvNombre.setText(nombre);
        tvFecha.setText(fecha);
        tvConcepto.setText(concepto);
        tvCantidad.setText(cantidad + "€");
        tvPagado.setText(pagado + "€");

        //Rellenar lista
        rv.setLayoutManager(new LinearLayoutManager(this));
        //List<Operacion> operaciones = Operacion.getDatosPrueba();
        List<Operacion> operaciones = new ArrayList<>();

       OperacionAdapter adapter = new OperacionAdapter(operaciones);
       rv.setAdapter(adapter);
        actualizarOps();
    }

    /**
     * Conectarse y descargar ops
     */
    private void actualizarOps()
    {
        Comando comando = new TodasOpsComando(KPantallas.PANTALLA_DETALLES_DEUDA);
        long idUsuario = UtilUI.getIdUsuario(this);

        //Conectarse
        if (idUsuario != -1)
        {
            showProgress(true);
            comando.ejecutar(idDeuda, TipoOperacion.PAGAR, idUsuario);
        }
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
    * Evento de respuesta de llamadas REST
    * @param event
    */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOps(EventOps event) {
        List<Operacion> ops = event.getOperaciones();

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
            OperacionAdapter adapter = (OperacionAdapter) rv.getAdapter();
            adapter.setOperaciones(ops);
            adapter.notifyDataSetChanged();
        }

        showProgress(false);

    };

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
