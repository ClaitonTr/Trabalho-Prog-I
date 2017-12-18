package br.com.claiton.trabalho_prog_i;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import br.com.claiton.trabalho_prog_i.db.Controle_Banco;

/**
 * Created by Usuario on 27/11/2017.
 */

public class Activity_Cadastro extends AppCompatActivity {
    private static final String URl_CADASTRO = "http://192.168.2.104/cardb/Cadastro.php";
    private RadioButton masc, fem;
    private String sexo = "";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        final EditText texto_nome = findViewById(R.id.nome);
        final EditText texto_email = findViewById(R.id.email);
        final EditText texto_senha = findViewById(R.id.senha);
        final EditText texto_confirma = findViewById(R.id.confirma_senha);
        Button cadastra = findViewById(R.id.b_cadastro);
        Button login = findViewById(R.id.b_para_login);
        masc = findViewById(R.id.mas);
        fem = findViewById(R.id.fem);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        cadastra.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {
                String nome = texto_nome.getText().toString().trim();
                String email = texto_email.getText().toString().trim();
                String senha = texto_senha.getText().toString().trim();
                String confirma = texto_confirma.getText().toString().trim();
                String erro = "";
                if (campoVazio(nome)) {
                    texto_nome.requestFocus();
                    texto_nome.setHintTextColor(Color.parseColor("#ff0000"));
                    erro = "nome";
                } else if(campoVazio(email)) {
                    texto_email.requestFocus();
                    erro = "email";
                } else if (!verificaEmail(email)) {
                    texto_email.requestFocus();
                    Toast.makeText(getApplicationContext(), "O email fornecido não é válido", Toast.LENGTH_LONG).show();
                } else if(campoVazio(senha)) {
                    texto_senha.requestFocus();
                    erro = "senha";
                } else if(campoVazio(confirma)) {
                    texto_confirma.requestFocus();
                    erro = "confirma senha";
                } else if (!masc.isChecked() && !fem.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Escolha o sexo", Toast.LENGTH_LONG).show();
                } else if (!senha.equals(confirma)) {
                    Toast.makeText(getApplicationContext(), "As senhas não correspondem", Toast.LENGTH_LONG).show();
                } else {
                    cadastraUsuario(nome, email, senha);
                }

                if (!erro.isEmpty()) Toast.makeText(getApplicationContext(), "O campo " + erro + " está vazio", Toast.LENGTH_LONG).show();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void escolheSexo(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.mas:
                if (checked)
                    sexo = "Masculino";
                break;
            case R.id.fem:
                if (checked)
                    sexo = "Feminino";
                break;
        }
    }

    private void cadastraUsuario(final String nome, final String email, final String senha) {
        String tag_string_req = "req_registro";

        pDialog.setMessage("Cadastrando...");
        showDialog();


        StringRequest sReq = new StringRequest(Request.Method.POST, URl_CADASTRO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean erro = obj.getBoolean("erro");
                    if (!erro) {
                        Toast.makeText(getApplicationContext(), "Usuário cadastrado!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        String erroMsg = obj.getString("erro_msg");
                        Toast.makeText(getApplicationContext(), erroMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("email", email);
                params.put("senha", senha);
                params.put("sexo", sexo);

                return params;
            }
        };

        sReq.setTag(tag_string_req);
        ControleConexao.getInstance(getApplicationContext()).addRequestQueue(sReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean campoVazio(String campo) {
        return TextUtils.isEmpty(campo) || campo.trim().isEmpty();
    }

    private boolean verificaEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Activity_Cadastro.this, Activity_Login.class);
        startActivity(i);
        finish();
    }*/
}
