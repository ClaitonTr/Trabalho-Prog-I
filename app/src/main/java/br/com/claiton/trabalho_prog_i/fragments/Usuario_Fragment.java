package br.com.claiton.trabalho_prog_i.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import br.com.claiton.trabalho_prog_i.ActivityPrincipal;
import br.com.claiton.trabalho_prog_i.R;

/**
 * Created by Usuario on 15/12/2017.
 */

public class Usuario_Fragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.detalhes_perfil, container);

        android.app.FragmentManager mFragmentManager = ((ActivityPrincipal)getContext()).getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        Conf_Usuario mPrefsFragment = new Conf_Usuario();
        mFragmentTransaction.replace(R.id.prefs_container, mPrefsFragment);
        mFragmentTransaction.commit();


        return view;
    }
}
