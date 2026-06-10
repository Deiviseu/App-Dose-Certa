package com.example.dosecerta;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Cadastro extends AppCompatActivity {

    DatabaseHelper db;
    EditText editNome, editCpf, editEmail, editSenha, editConfirmarSenha;
    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        db = new DatabaseHelper(this);

        editNome           = findViewById(R.id.editNome);
        editCpf            = findViewById(R.id.editCpf);
        editEmail          = findViewById(R.id.editEmail);
        editSenha          = findViewById(R.id.editSenha);
        editConfirmarSenha = findViewById(R.id.editConfirmarSenha);
        btnSalvar          = findViewById(R.id.btSalvar);

        btnSalvar.setOnClickListener(v -> {
            String nome      = editNome.getText().toString().trim();
            String cpf       = editCpf.getText().toString().trim();
            String email     = editEmail.getText().toString().trim();
            String senha     = editSenha.getText().toString().trim();
            String confirmar = editConfirmarSenha.getText().toString().trim();

            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else if (cpf.length() != 11) {
                Toast.makeText(this, "CPF deve ter 11 dígitos!", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show();
            } else if (!senha.equals(confirmar)) {
                Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT).show();
            } else {
                boolean sucesso = db.cadastrarUsuario(nome, cpf, email, senha);
                if (sucesso) {
                    Toast.makeText(this, "Usuário " + nome + " cadastrado!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao cadastrar.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}