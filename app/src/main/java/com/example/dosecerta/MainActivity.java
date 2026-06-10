package com.example.dosecerta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btEntrar, btSair;
    TextView linkEsqueciSenha, linkCadastrar;
    EditText editLoginCpf, editLoginSenha;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        editLoginCpf   = findViewById(R.id.lgCpf);
        editLoginSenha = findViewById(R.id.lgSenha);
        btEntrar       = findViewById(R.id.btEntrar);
        btSair         = findViewById(R.id.btSair);
        linkCadastrar  = findViewById(R.id.linkCadastrar);
        linkEsqueciSenha = findViewById(R.id.linkEsqueciSenha);

        btEntrar.setOnClickListener(this);
        btSair.setOnClickListener(this);
        linkCadastrar.setOnClickListener(this);
        linkEsqueciSenha.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btEntrar) {
            String cpf   = editLoginCpf.getText().toString().trim();
            String senha = editLoginSenha.getText().toString().trim();

            if (cpf.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha CPF e senha!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.conferirLogin(cpf, senha)) {
                Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, TelaHomeActivity.class));
                finish(); // impede voltar para o login
            } else {
                Toast.makeText(this, "CPF ou Senha incorretos!", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.btSair) {
            finish();

        } else if (id == R.id.linkEsqueciSenha) {
            startActivity(new Intent(this, Recuperar.class));

        } else if (id == R.id.linkCadastrar) {
            startActivity(new Intent(this, Cadastro.class));
        }
    }
}