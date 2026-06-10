package com.example.dosecerta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class cadastro_remedio extends AppCompatActivity {

    EditText edtNome, edtDosagem, edtQuantidade, edtDescricao;
    TextView lblQuantidade;
    ImageView imgFoto;
    Button btEscolherFoto, btSalvar;
    RadioGroup rgTipo;
    Uri fotoUri = null;

    ActivityResultLauncher<Intent> galeriaLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fotoUri = result.getData().getData();
                    imgFoto.setImageURI(fotoUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_remedio);

        edtNome        = findViewById(R.id.edtNome);
        edtDosagem     = findViewById(R.id.edtDosagem);
        edtQuantidade  = findViewById(R.id.edtQuantidade);
        edtDescricao   = findViewById(R.id.edtDescricao);
        lblQuantidade  = findViewById(R.id.lblQuantidade);
        imgFoto        = findViewById(R.id.imgFoto);
        btEscolherFoto = findViewById(R.id.btEscolherFoto);
        btSalvar       = findViewById(R.id.btSalvar);
        rgTipo         = findViewById(R.id.rgTipo);

        rgTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbLiquido) {
                lblQuantidade.setVisibility(View.GONE);
                edtQuantidade.setVisibility(View.GONE);
                edtQuantidade.setText("");
            } else {
                lblQuantidade.setVisibility(View.VISIBLE);
                edtQuantidade.setVisibility(View.VISIBLE);
            }
        });

        btEscolherFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galeriaLauncher.launch(intent);
        });

        btSalvar.setOnClickListener(v -> {
            String nome      = edtNome.getText().toString().trim();
            String dosagem   = edtDosagem.getText().toString().trim();
            String descricao = edtDescricao.getText().toString().trim();
            String quantidade= edtQuantidade.getText().toString().trim();
            String foto      = fotoUri != null ? fotoUri.toString() : "";
            boolean isLiquido = rgTipo.getCheckedRadioButtonId() == R.id.rbLiquido;

            if (nome.isEmpty() || dosagem.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isLiquido && quantidade.isEmpty()) {
                Toast.makeText(this, "Informe a quantidade de comprimidos!", Toast.LENGTH_SHORT).show();
                return;
            }

            String qtd = isLiquido ? "—" : quantidade;

            DatabaseHelper db = new DatabaseHelper(this);
            boolean ok = db.cadastrarRemedio(new Remedio(nome, dosagem, qtd, descricao, foto));

            if (ok) {
                Toast.makeText(this, "Remédio cadastrado!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}