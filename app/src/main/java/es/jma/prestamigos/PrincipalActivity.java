package es.jma.prestamigos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import es.jma.prestamigos.constantes.KPantallas;
import es.jma.prestamigos.constantes.KShared;
import es.jma.prestamigos.navegacion.BaseActivity;
import es.jma.prestamigos.utils.ui.UtilUI;

public class PrincipalActivity extends BaseActivity
        implements PerfilFragment.OnFragmentInteractionListener, HistorialFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener, AmigosFragment.OnFragmentInteractionListener, DeudasOtrosFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Cargar datos del usuario
        View header = navigationView.getHeaderView(0);
        TextView tvNombre = (TextView) header.findViewById(R.id.tv_header_nombre);
        TextView tvEmail = (TextView) header.findViewById(R.id.tv_header_email);
        tvNombre.setText(UtilUI.getNombre(this));
        tvEmail.setText(UtilUI.getEmail(this));

        navigationView.setNavigationItemSelectedListener(this);

        //Por defecto cargar la primera opción
        MenuItem menuItem = navigationView.getMenu().getItem(0);
        menuItem.setChecked(true);
        onNavigationItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Cambiar al fragmento de deudas pendientes
        if (id == R.id.nav_deudas_pendientes) {
            Bundle params = new Bundle();
            params.putInt(DeudasOtrosFragment.TIPO_DEUDA, KPantallas.PANTALLA_DEUDAS_OTROS);
            changeFragment(new DeudasOtrosFragment(),R.id.mainFrame,params);

        }
        //Cambiar al fragmento de mis deudas
        else if (id == R.id.nav_mis_deudas) {
            Bundle params = new Bundle();
            params.putInt(DeudasOtrosFragment.TIPO_DEUDA, KPantallas.PANTALLA_MIS_DEUDAS);
            changeFragment(new DeudasOtrosFragment(), R.id.mainFrame, params);
        }
        //Cambiar al fragmento de resumen
        else if (id == R.id.nav_resumen) {
            changeFragment(new DashboardFragment(), R.id.mainFrame, null);
        }
        //Cambiar al fragmento de historial
        else if (id == R.id.nav_historial) {
            changeFragment(new HistorialFragment(), R.id.mainFrame, null);
        }
        //Cambiar al fragmento de amigos
        else if (id == R.id.nav_amigos) {
            changeFragment(new AmigosFragment(), R.id.mainFrame, null);

        }
        //Cerrar sesión
        else if (id == R.id.nav_cerrar_sesion) {
            //Borrar shared preferences
            SharedPreferences shared = getSharedPreferences(KShared.CLAVE_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.clear();
            editor.commit();
            //Volver al login
            start(LoginActivity.class, true);
        }
        //Datos perfil
        else if (id == R.id.nav_perfil)
        {
            changeFragment(new PerfilFragment(), R.id.mainFrame, null);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
