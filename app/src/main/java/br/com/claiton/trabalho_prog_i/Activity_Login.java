package br.com.claiton.trabalho_prog_i;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.claiton.trabalho_prog_i.conexao.ControleConexao;
import br.com.claiton.trabalho_prog_i.conexao.GerenciadorLogin;
import br.com.claiton.trabalho_prog_i.db.Controle_Banco;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Usuario on 27/11/2017.
 */

public class Activity_Login extends AppCompatActivity {
    private static final String URl_LOGIN = "http://192.168.2.104/cardb/Login.php";

    private TextInputLayout texto_email, texto_senha;
    private ProgressDialog pDialog;
    private GerenciadorLogin gerenciadorLogin;
    private Controle_Banco bd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*final TextInputLayout senhaWrapper =  findViewById(R.id.password);
        senhaWrapper.setHint("senha");*/

        Button login = findViewById(R.id.b_login);
        Button cadastro = findViewById(R.id.b_para_cadastro);
        texto_email = findViewById(R.id.email_login);
        texto_senha = findViewById(R.id.senha_login);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        gerenciadorLogin = new GerenciadorLogin(getApplicationContext());
        bd = new Controle_Banco(getApplicationContext());

        if (gerenciadorLogin.isLoggedIn()) {
            Intent intent = new Intent(Activity_Login.this, ActivityPrincipal.class);
            startActivity(intent);
            finish();
        }

        /*VAI NO CADASTRO
        texto_senha.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (texto_senha.getEditText().length() < 6)
                    texto_senha.setError("Mínimo 6 caracteres");
                else
                    texto_senha.setError(null);
            }
        });*/

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = texto_email.getEditText().getText().toString();
                String senha = texto_senha.getEditText().getText().toString();

                if(email.isEmpty() || email.trim().isEmpty()) {
                    texto_email.setError("Email vazio");
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    texto_email.setError("Este email não é válido");
                } else if(senha.isEmpty() || senha.trim().isEmpty()) {
                    texto_senha.setError("Senha vazia");
                }else {
                    texto_senha.setErrorEnabled(false);
                    texto_email.setErrorEnabled(false);
                    texto_email.getEditText().setText("");
                    texto_senha.getEditText().setText("");

                    verficaLogin(email, senha);
                    gerenciadorLogin.setLogin(true);
                    /*if (email.equals("claiton@ct.com.br") && senha.equals("claiton2017")) {
                        Intent intent = new Intent(Activity_Login.this, ActivityPrincipal.class);
                        startActivity(intent);
                        finish();
                    }  else {
                        Toast.makeText(getApplicationContext(), "Email ou senha incorretos", Toast.LENGTH_LONG).show();
                    }*/
                }
                InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Activity_Cadastro.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void verficaLogin (final String email, final String senha) {
        String tag_req = "req_login";

       pDialog.setMessage("Verficando conta...");
       pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URl_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "resposta " + response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean erro = obj.getBoolean("erro");

                    if (!erro) {
                        gerenciadorLogin.setLogin(true);

                        JSONObject usuario = obj.getJSONObject("usuario");
                        String nome = usuario.getString("nome");
                        String email = usuario.getString("email");
                        String uid = usuario.getString("id_unico");
                        String criacao = usuario.getString("data_criacao");
                        String sexo = usuario.getString("sexo");


                        bd.insereusuario(nome, email, uid, sexo, criacao);

                        Intent intent = new Intent(Activity_Login.this, ActivityPrincipal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String erroMsg = obj.getString("erro_msg");
                        Toast.makeText(getApplicationContext(), erroMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro Json: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro " + error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("senha", senha);

                return params;
            }
        };

        stringRequest.setTag(tag_req);
        ControleConexao.getInstance(getApplicationContext()).addRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent= new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

}
