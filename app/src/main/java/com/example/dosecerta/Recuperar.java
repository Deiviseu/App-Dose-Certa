package com.example.dosecerta;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Recuperar extends AppCompatActivity {

    EditText edtCpf, edtNovaSenha, edtConfirmarSenha;
    Button btSalvar;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar);

        db                = new DatabaseHelper(this);
        edtCpf            = findViewById(R.id.edtCpf);
        edtNovaSenha      = findViewById(R.id.edtNovaSenha);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenha);
        btSalvar          = findViewById(R.id.btSalvar);

        btSalvar.setOnClickListener(v -> {
            String cpf       = edtCpf.getText().toString().trim();
            String nova      = edtNovaSenha.getText().toString().trim();
            String confirmar = edtConfirmarSenha.getText().toString().trim();

            if (cpf.isEmpty() || nova.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else if (cpf.length() != 11) {
                Toast.makeText(this, "CPF deve ter 11 dígitos!", Toast.LENGTH_SHORT).show();
            } else if (!nova.equals(confirmar)) {
                Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT).show();
            } else if (!db.cpfExiste(cpf)) {
                Toast.makeText(this, "CPF não encontrado!", Toast.LENGTH_SHORT).show();
            } else {
                boolean ok = db.atualizarSenha(cpf, nova);
                if (ok) {
                    Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao alterar senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}