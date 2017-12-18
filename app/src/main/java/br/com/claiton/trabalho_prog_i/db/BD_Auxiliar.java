package br.com.claiton.trabalho_prog_i.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Claiton T. on 29/11/2017.
 */

public class BD_Auxiliar extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "carona";
    private static final String TABLE_NAME = "usuario";

    //atributos
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_SEXO = "sexo";
    private static final String KEY_DATA_CRIACAO = "data_criacao";
    private static final String KEY_FOTO = "foto";


    public BD_Auxiliar(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +"("
                + KEY_ID + " INTEGER NOT NULL PRIMARY KEY,"
                + KEY_NOME + " TEXT NOT NULL,"
                + KEY_EMAIL + " TEXT UNIQUE NOT NULL,"
                + KEY_UID + " TEXT NOT NULL,"
                + KEY_SEXO + " TEXT NOT NULL,"
                + KEY_DATA_CRIACAO + " TEXT,"
                + KEY_FOTO + " BLOB"
                +")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
