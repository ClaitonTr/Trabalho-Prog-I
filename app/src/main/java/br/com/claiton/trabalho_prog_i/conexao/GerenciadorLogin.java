package br.com.claiton.trabalho_prog_i.conexao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Usuario on 28/11/2017.
 */

public class GerenciadorLogin {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context contexto;

    private static final String PREF_NAME = "br.com.claiton.trabalho_prog_i.AppCarLogin";
    private static final String KEY_IS_LOGGED_IN = "estaLogado";

    @SuppressLint("CommitPrefEdits")
    public GerenciadorLogin(Context contexto) {
            this.contexto = contexto;
            prefs = contexto.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = prefs.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

}
