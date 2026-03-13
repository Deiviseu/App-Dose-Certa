package com.example.dosecerta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    Button btEntrar, btSair;
    TextView linkEsqueciSenha, linkCadastrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btEntrar = findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(this);

        btSair = findViewById(R.id.btSair);
        btSair.setOnClickListener(this);

        linkCadastrar = findViewById(R.id.linkCadastrar);
        linkCadastrar.setOnClickListener(this);

        linkEsqueciSenha = findViewById(R.id.linkEsqueciSenha);
        linkEsqueciSenha.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btEntrar) {
            Intent tela_home = new Intent(this, TelaHomeActivity.class);
            startActivity(tela_home);

        } else if (id == R.id.btSair) {
            finish();
        }else if (id == R.id.linkEsqueciSenha) {
            Intent activity_recuperar = new Intent(this, Recuperar.class);
            startActivity(activity_recuperar);

        } else if (id == R.id.linkCadastrar) {
            Intent activity_cadastro = new Intent(this, Cadastro.class);
            startActivity(activity_cadastro);
        }
    }

}