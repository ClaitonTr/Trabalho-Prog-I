package br.com.claiton.trabalho_prog_i;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import br.com.claiton.trabalho_prog_i.fragments.Conf_Usuario;

/**
 * Created by Usuario on 29/11/2017.
 */

public class Detalhes_usuario extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_perfil);

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        Conf_Usuario mPrefsFragment = new Conf_Usuario();
        mFragmentTransaction.replace(R.id.prefs_container, mPrefsFragment);
        mFragmentTransaction.commit();

    }
}
