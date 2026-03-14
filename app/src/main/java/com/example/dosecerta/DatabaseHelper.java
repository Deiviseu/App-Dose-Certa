package com.example.dosecerta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dosecerta.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tabelaUsuarios = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "cpf TEXT," +
                "email TEXT," +
                "senha TEXT)";
        db.execSQL(tabelaUsuarios);
    }

    public boolean cadastrarUsuario(String nome, String cpf, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("cpf", cpf);
        values.put("email", email);
        values.put("senha", senha);

        long resultado = db.insert("usuarios", null, values);
        return resultado != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    public boolean conferirLogin(String cpf, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Faz um SELECT procurando pelo CPF e Senha informados
        String query = "SELECT * FROM usuarios WHERE cpf = ? AND senha = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cpf, senha});

        boolean loginValido = cursor.getCount() > 0;
        cursor.close();
        return loginValido;
    }
}