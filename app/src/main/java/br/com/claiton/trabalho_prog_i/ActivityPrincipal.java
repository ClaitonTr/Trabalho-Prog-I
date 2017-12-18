package br.com.claiton.trabalho_prog_i;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.HashMap;

import br.com.claiton.trabalho_prog_i.conexao.GerenciadorLogin;
import br.com.claiton.trabalho_prog_i.db.Controle_Banco;
import br.com.claiton.trabalho_prog_i.fragments.Conf_Usuario;
import br.com.claiton.trabalho_prog_i.fragments.Usuario_Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IPickResult {


    private CircleImageView circleImageView, usuario;
    private TextView nomeUsuario;
    private DrawerLayout drawer;
    private Controle_Banco bd;
    CropImageView cropImageView;

    @Override
    protected void onStart() {
        super.onStart();
        if (!autenticado()) {
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        Log.d("main", "oncreate");
        Toolbar toolbar = findViewById(R.id.toolbar_principal);
        setSupportActionBar(toolbar);


        bd = new Controle_Banco(getApplicationContext());


        /*HashMap<String, String> usuario = bd.getUsuario();

        String nome = usuario != null? usuario.get("nome"): "";
        String email = usuario != null? usuario.get("email"): "";
        String sexo = usuario != null? usuario.get("sexo"): "";*/


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View perfil = navigationView.getHeaderView(0);
        circleImageView = perfil.findViewById(R.id.img_perfil);
        nomeUsuario = perfil.findViewById(R.id.text_nome);
        ImageButton ed = perfil.findViewById(R.id.edita_usuario);



       /* nomeUsuario.setText(nome);
        try {
            if (bd.getImage(email) == null) {
                if (sexo.equals("Masculino")) {
                    circleImageView.setImageResource(R.drawable.avatar_mas);
                } else {
                    circleImageView.setImageResource(R.drawable.avatar_fem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

       ed.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               editaUsuario();
           }
       });

        /*circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagem();
            }
        });

        nomeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudaNome();
            }
        });*/

    }

    private boolean autenticado() {
        GerenciadorLogin gerenciadorLogin = new GerenciadorLogin(getApplicationContext());
        Intent intent;
        if(!gerenciadorLogin.isLoggedIn()) {
                intent = new Intent(ActivityPrincipal.this,
                        Activity_Login.class);
                startActivity(intent);

            return true;
        }
        return false;
    }

    private void imagem() {
        PickSetup setup = new PickSetup()
                .setTitle("Mudar foto")
                .setCameraButtonText("Câmera")
                .setGalleryButtonText("Galeria")
                .setCancelText("Cancelar")
                .setProgressText("Aguarde...");
        PickImageDialog.build(setup).show(this);
    }

    private void editaUsuario() {
        LayoutInflater infl = LayoutInflater.from(this);
        View input = infl.inflate(R.layout.nome_usuario, null);
        final EditText novo = input.findViewById(R.id.texto_nome);
        usuario = input.findViewById(R.id.foto);

        usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagem();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nome = novo.getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) usuario.getDrawable();
                Bitmap foto = drawable.getBitmap();

                if(!nome.isEmpty()) nomeUsuario.setText(nome);
                 circleImageView.setImageBitmap(foto);

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setTitle("Alterar nome de usuário");
        dialog.setView(input);
        dialog.show();
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            usuario.setImageBitmap(pickResult.getBitmap());
        } else {
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_slideshow) {
            /*Intent intent = new Intent(ActivityPrincipal.this,
                    Detalhes_usuario.class);
            startActivity(intent);*/
            fragment = new Usuario_Fragment();
        } else if (id == R.id.nav_manage) {
                //fragment = new MinhaFragment();
        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.sair) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GerenciadorLogin login = new GerenciadorLogin(getApplicationContext());
                    login.setLogin(false);

                    Intent intent = new Intent(ActivityPrincipal.this,
                            Activity_Login.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setTitle("Tem ceteza que deseja sair?");
            dialog.show();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frg_container, fragment);
            ft.commit();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
