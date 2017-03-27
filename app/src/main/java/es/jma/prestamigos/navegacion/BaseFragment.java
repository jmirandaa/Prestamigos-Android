package es.jma.prestamigos.navegacion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Sobrescritura de Fragment para incluir ButterKnife y simplificar acciones
 * Created by jmiranda on 7/02/17.
 */

public class BaseFragment extends Fragment {
    private ActionBar actionBar;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected View getView(LayoutInflater inflater, int id, ViewGroup container)
    {
        View v = inflater.inflate(id, container, false);
        unbinder = ButterKnife.bind(this, v);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        return v;
    }

    public void start(Class<? extends BaseActivity> activity, @Nullable Bundle bundle, int reqCode) {
        Intent intent = new Intent(getActivity(), activity);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, reqCode);
    }

    protected void start(Class<? extends BaseActivity> activity, @Nullable Bundle bundle) {
        Intent intent = new Intent(getActivity(), activity);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Action Bar
     * @return
     */
    protected ActionBar getActionBar()
    {
        return actionBar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
