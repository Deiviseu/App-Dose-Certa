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

        editNome = findViewById(R.id.editNome);
        editCpf = findViewById(R.id.editCpf);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        editConfirmarSenha = findViewById(R.id.editConfirmarSenha);
        btnSalvar = findViewById(R.id.btSalvar);

        btnSalvar.setOnClickListener(v -> {
            String nome = editNome.getText().toString();
            String cpf = editCpf.getText().toString();
            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();
            String confirmar = editConfirmarSenha.getText().toString();

            // Validações básicas
            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else if (!senha.equals(confirmar)) {
                Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT).show();
            } else {
                // Se tudo estiver OK, salva no banco
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