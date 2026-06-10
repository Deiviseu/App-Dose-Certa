package com.example.dosecerta;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetalhesDoRemedio extends AppCompatActivity {

    TextView nome, dosagem, descricao, quantidade;
    ImageView imagem;
    Button btBula, btEditar, btDeletar;
    DatabaseHelper db;
    int remedioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_do_remedio);

        db = new DatabaseHelper(this);

        nome       = findViewById(R.id.txtNomeRemedio);
        dosagem    = findViewById(R.id.txtDosagem);
        descricao  = findViewById(R.id.txtDescricao);
        imagem     = findViewById(R.id.imgRemedio);
        quantidade = findViewById(R.id.txtQuantidade);
        btBula     = findViewById(R.id.btBula);
        btEditar   = findViewById(R.id.btEditar);
        btDeletar  = findViewById(R.id.btDeletar);

        remedioId                 = getIntent().getIntExtra("id", -1);
        String nomeRecebido       = getIntent().getStringExtra("nome");
        String dosagemRecebida    = getIntent().getStringExtra("dosagem");
        String descricaoRecebida  = getIntent().getStringExtra("descricao");
        String quantidadeRecebida = getIntent().getStringExtra("quantidade");
        String fotoUri            = getIntent().getStringExtra("fotoUri");
        String numExpediente      = getIntent().getStringExtra("numExpediente"); // novo

        nome.setText(nomeRecebido);
        dosagem.setText(dosagemRecebida);
        descricao.setText(descricaoRecebida);
        quantidade.setText(quantidadeRecebida);

        if (fotoUri != null && !fotoUri.isEmpty()) {
            imagem.setImageURI(Uri.parse(fotoUri));
        }

        // Bula — passa id, nome e expediente para a TelaBula
        btBula.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelaBula.class);
            intent.putExtra("id", remedioId);
            intent.putExtra("nome", nomeRecebido);
            intent.putExtra("numExpediente", numExpediente); // novo
            startActivity(intent);
        });

        // Editar
        btEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, editar_remedio.class);
            intent.putExtra("id", remedioId);
            intent.putExtra("nome", nomeRecebido);
            intent.putExtra("dosagem", dosagemRecebida);
            intent.putExtra("quantidade", quantidadeRecebida);
            intent.putExtra("descricao", descricaoRecebida);
            intent.putExtra("fotoUri", fotoUri);
            intent.putExtra("numExpediente", numExpediente); // novo
            startActivity(intent);
            finish();
        });

        // Deletar
        btDeletar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Deletar remédio")
                    .setMessage("Tem certeza que deseja deletar " + nomeRecebido + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        boolean ok = db.deletarRemedio(remedioId);
                        if (ok) {
                            Toast.makeText(this, "Remédio deletado!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Erro ao deletar!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
}