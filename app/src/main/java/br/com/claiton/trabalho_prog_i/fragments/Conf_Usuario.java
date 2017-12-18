package br.com.claiton.trabalho_prog_i.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import br.com.claiton.trabalho_prog_i.R;

/**
 * Created by Usuario on 15/12/2017.
 */

public class Conf_Usuario extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.usuario_conf);
    }
}
