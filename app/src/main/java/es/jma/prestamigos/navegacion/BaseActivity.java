package es.jma.prestamigos.navegacion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by tulon on 6/02/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity) this).getSupportActionBar();
    }


    /**
     * Llamar siempre a ButterKnife
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * Encapsulación para empezar activities
     */
    protected void start(Class<? extends BaseActivity> activity, boolean cerrar) {
        startActivity(new Intent(this, activity));
        if (cerrar)
        {
            finish();
        }
    }

    protected void start(Class<? extends BaseActivity> activity, int reqCode) {
        startActivityForResult(new Intent(this, activity),reqCode);
    }

    protected void start(Class<? extends BaseActivity> activity, @Nullable Bundle bundle, int reqCode) {
        Intent intent = new Intent(this, activity);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, reqCode);
    }

    protected void start(Class<? extends BaseActivity> activity, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Devolver la barra de acción inicializada
     * @return
     */
    protected ActionBar getMyActionBar()
    {
        return actionBar;
    }

    /**
     * Cambiar de fragment
     * @param fragment
     * @param idFrame
     * @param bundle
     */
    protected void changeFragment(Fragment fragment, int idFrame, @Nullable Bundle bundle)
    {
        if (bundle != null)
        {
            fragment.setArguments(bundle);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(idFrame, fragment);
        ft.commit();
    }
}
