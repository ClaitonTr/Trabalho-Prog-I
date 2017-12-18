package br.com.claiton.trabalho_prog_i.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Usuario on 29/11/2017.
 */

public class Controle_Banco {
    private SQLiteDatabase sqLite;
    private BD_Auxiliar bd_aux;

    public Controle_Banco(Context c) {
        bd_aux = new BD_Auxiliar(c);
    }

    private Controle_Banco open () throws SQLException {
        sqLite = bd_aux.getWritableDatabase();
        return this;
    }

    private void close() {
        if (sqLite != null) {
            sqLite.close();
        }
    }

    public void insereusuario(String nome, String email, String uid, String sexo, String d_criacao) {
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("email", email);
        valores.put("uid", uid);
        valores.put("sexo", sexo);
        valores.put("data_criacao", d_criacao);
        open();
        sqLite.insert("usuario", null, valores);
        close();
    }

    public void atualizaNome(String email, String nome) {
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        open();
        sqLite.update("usuario", valores,"email = ?", new String[]{email});
        close();
    }

    public void atualizaFoto(String email, byte[] foto) {
        ContentValues valores = new ContentValues();
        valores.put("foto", foto);
        open();
        sqLite.update("usuario", valores,"email = ?", new String[]{email});
        close();
    }

    public void excluiUsuario() {
        open();
        sqLite.delete("usuario", null, null);
        close();
    }

    public HashMap<String, String> getUsuario() {
        HashMap<String, String> usuario = new HashMap<String, String>();
        String sql = "SELECT * FROM usuario";

        SQLiteDatabase bd = bd_aux.getReadableDatabase();
        Cursor cursor = bd.rawQuery(sql, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            usuario.put("nome", cursor.getString(cursor.getColumnIndex("nome")));
            usuario.put("email", cursor.getString(cursor.getColumnIndex("email")));
            usuario.put("uid", cursor.getString(cursor.getColumnIndex("uid")));
            usuario.put("data", cursor.getString(cursor.getColumnIndex("data_criacao")));
            usuario.put("sexo", cursor.getString(cursor.getColumnIndex("sexo")));

        }
        cursor.close();
        bd.close();



        return usuario;
    }

    public Bitmap getImage(String email) {

            String sql = "select foto from usuario where email = ?";
            String[] selection = new String[] { email };
            Cursor cur = sqLite.rawQuery(sql, selection);

            if (cur.moveToFirst()){
                byte[] imgByte = cur.getBlob(cur.getColumnIndex("foto"));
                cur.close();
                return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            }
            if (!cur.isClosed())
                cur.close();

            return null ;
    }

    public String getSexo(String email) {
        String sql = "select sexo from usuario where email = ?";
        String[] selection = new String[] { email };
        Cursor c = sqLite.rawQuery(sql, selection);

        String sexo = c.getString(c.getColumnIndex("sexo"));
        c.close();

        return sexo;
    }

}
