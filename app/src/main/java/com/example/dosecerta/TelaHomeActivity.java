package com.example.dosecerta;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TelaHomeActivity extends AppCompatActivity {

    LinearLayout listaContainer;
    TextView txtVazio;
    EditText edtBuscar;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_home);

        db             = new DatabaseHelper(this);
        listaContainer = findViewById(R.id.listaContainer);
        txtVazio       = findViewById(R.id.txtVazio);
        edtBuscar      = findViewById(R.id.edtBuscar);

        ImageView btAdicionar = findViewById(R.id.btAdicionar);
        btAdicionar.setOnClickListener(v ->
                startActivity(new Intent(this, cadastro_remedio.class))
        );

        edtBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carregarRemedios(s.toString().trim());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarRemedios(edtBuscar.getText().toString().trim());
    }

    private void carregarRemedios(String busca) {
        listaContainer.removeAllViews();

        List<Remedio> lista = busca.isEmpty()
                ? db.buscarRemedios()
                : db.buscarRemediosPorNome(busca);

        if (lista.isEmpty()) {
            txtVazio.setText(busca.isEmpty()
                    ? "Você ainda não possui remédios adicionados"
                    : "Nenhum remédio encontrado para \"" + busca + "\"");
            txtVazio.setVisibility(View.VISIBLE);
            return;
        }

        txtVazio.setVisibility(View.GONE);

        for (Remedio r : lista) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setBackground(getDrawable(R.drawable.edittext_bg));

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(20, 50, 20, 0);
            card.setLayoutParams(cardParams);

            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                    200, LinearLayout.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(imgParams);
            img.setBackground(getDrawable(R.drawable.edittext_bg));
            if (r.fotoUri != null && !r.fotoUri.isEmpty()) {
                img.setImageURI(android.net.Uri.parse(r.fotoUri));
            } else {
                img.setImageResource(R.drawable.dorflex);
            }

            TextView txt = new TextView(this);
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 165);
            txt.setLayoutParams(txtParams);
            txt.setText(r.nome + "\n" + r.dosagem);
            txt.setTextSize(20);
            txt.setBackground(getDrawable(R.drawable.edittext_bg));

            card.addView(img);
            card.addView(txt);

            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, DetalhesDoRemedio.class);
                intent.putExtra("id", r.id);
                intent.putExtra("nome", r.nome);
                intent.putExtra("dosagem", r.dosagem);
                intent.putExtra("quantidade", r.quantidade);
                intent.putExtra("descricao", r.descricao);
                intent.putExtra("fotoUri", r.fotoUri);
                intent.putExtra("numExpediente", r.numExpediente); // novo
                startActivity(intent);
            });

            listaContainer.addView(card);
        }
    }
}