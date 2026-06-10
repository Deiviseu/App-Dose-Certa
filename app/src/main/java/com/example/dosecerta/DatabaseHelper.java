package com.example.dosecerta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dosecerta.db";
    private static final int DATABASE_VERSION = 3; // era 2

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "cpf TEXT UNIQUE," +
                "email TEXT," +
                "senha TEXT)");

        db.execSQL("CREATE TABLE remedios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "dosagem TEXT," +
                "quantidade TEXT," +
                "descricao TEXT," +
                "fotoUri TEXT," +
                "numExpediente TEXT DEFAULT '')"); // novo campo
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Migração suave: só adiciona a coluna, não apaga dados
            db.execSQL("ALTER TABLE remedios ADD COLUMN numExpediente TEXT DEFAULT ''");
        }
    }

    // ─── USUÁRIOS ────────────────────────────────────────────

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

    public boolean conferirLogin(String cpf, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE cpf = ? AND senha = ?",
                new String[]{cpf, senha});
        boolean valido = cursor.getCount() > 0;
        cursor.close();
        return valido;
    }

    public boolean atualizarSenha(String cpf, String novaSenha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("senha", novaSenha);
        int linhas = db.update("usuarios", values, "cpf = ?", new String[]{cpf});
        return linhas > 0;
    }

    public boolean cpfExiste(String cpf) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM usuarios WHERE cpf = ?", new String[]{cpf});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    // ─── REMÉDIOS ────────────────────────────────────────────

    public boolean cadastrarRemedio(Remedio r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", r.nome);
        values.put("dosagem", r.dosagem);
        values.put("quantidade", r.quantidade);
        values.put("descricao", r.descricao);
        values.put("fotoUri", r.fotoUri);
        values.put("numExpediente", r.numExpediente != null ? r.numExpediente : ""); // novo
        long resultado = db.insert("remedios", null, values);
        return resultado != -1;
    }

    public List<Remedio> buscarRemedios() {
        List<Remedio> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM remedios ORDER BY nome ASC", null);

        if (cursor.moveToFirst()) {
            do {
                Remedio r = new Remedio(
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dosagem")),
                        cursor.getString(cursor.getColumnIndexOrThrow("quantidade")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fotoUri"))
                );
                r.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                r.numExpediente = cursor.getString(cursor.getColumnIndexOrThrow("numExpediente")); // novo
                lista.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public boolean deletarRemedio(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int linhas = db.delete("remedios", "id = ?", new String[]{String.valueOf(id)});
        return linhas > 0;
    }

    public boolean editarRemedio(Remedio r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", r.nome);
        values.put("dosagem", r.dosagem);
        values.put("quantidade", r.quantidade);
        values.put("descricao", r.descricao);
        values.put("fotoUri", r.fotoUri);
        values.put("numExpediente", r.numExpediente != null ? r.numExpediente : ""); // novo
        int linhas = db.update("remedios", values, "id = ?", new String[]{String.valueOf(r.id)});
        return linhas > 0;
    }

    public List<Remedio> buscarRemediosPorNome(String busca) {
        List<Remedio> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM remedios WHERE nome LIKE ? ORDER BY nome ASC",
                new String[]{"%" + busca + "%"});

        if (cursor.moveToFirst()) {
            do {
                Remedio r = new Remedio(
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dosagem")),
                        cursor.getString(cursor.getColumnIndexOrThrow("quantidade")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fotoUri"))
                );
                r.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                r.numExpediente = cursor.getString(cursor.getColumnIndexOrThrow("numExpediente")); // novo
                lista.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // novo método — salva só o expediente sem mexer nos outros campos
    public boolean salvarExpediente(int id, String numExpediente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("numExpediente", numExpediente);
        int linhas = db.update("remedios", values, "id = ?", new String[]{String.valueOf(id)});
        return linhas > 0;
    }
}