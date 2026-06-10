package com.example.dosecerta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelaBula extends AppCompatActivity {

    TextView txtTituloBula, txtNomeBula, txtLaboratorio;
    TextView txtIndicacoes, txtPosologia, txtContraindicacoes, txtEfeitosColaterais;
    TextView txtErro;
    ProgressBar progressBula;
    ScrollView scrollBula;
    Button btBulaCompleta;
    ImageButton btVoltar;

    String nomeRemedio;
    String urlBulaCompleta = "";

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bula);

        nomeRemedio = getIntent().getStringExtra("nome");

        txtTituloBula        = findViewById(R.id.txtTituloBula);
        txtNomeBula          = findViewById(R.id.txtNomeBula);
        txtLaboratorio       = findViewById(R.id.txtLaboratorio);
        txtIndicacoes        = findViewById(R.id.txtIndicacoes);
        txtPosologia         = findViewById(R.id.txtPosologia);
        txtContraindicacoes  = findViewById(R.id.txtContraindicacoes);
        txtEfeitosColaterais = findViewById(R.id.txtEfeitosColaterais);
        txtErro              = findViewById(R.id.txtErro);
        progressBula         = findViewById(R.id.progressBula);
        scrollBula           = findViewById(R.id.scrollBula);
        btBulaCompleta       = findViewById(R.id.btBulaCompleta);
        btVoltar             = findViewById(R.id.btVoltar);

        txtTituloBula.setText("Bula: " + nomeRemedio);

        btVoltar.setOnClickListener(v -> finish());

        btBulaCompleta.setOnClickListener(v -> {
            if (!urlBulaCompleta.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlBulaCompleta)));
            }
        });

        buscarBula(nomeRemedio);
    }

    private void buscarBula(String nome) {
        progressBula.setVisibility(View.VISIBLE);
        scrollBula.setVisibility(View.GONE);
        txtErro.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                // 1. Busca o produto na API da ANVISA
                String nomeCodificado = URLEncoder.encode(nome, "UTF-8");
                String urlBusca = "https://consultas.anvisa.gov.br/api/consulta/bulario/palavras-chave?count=5&filter%5BnomeProduto%5D=" + nomeCodificado;

                String jsonBusca = fazerGet(urlBusca);

                if (jsonBusca == null) {
                    mostrarErro("Sem conexão com a internet.\n\nVerifique sua conexão e tente novamente.");
                    return;
                }

                JSONObject obj = new JSONObject(jsonBusca);
                JSONArray content = obj.optJSONArray("content");

                if (content == null || content.length() == 0) {
                    mostrarErro("Bula não encontrada para \"" + nome + "\" na base da ANVISA.\n\nTente buscar pelo nome do princípio ativo.");
                    return;
                }

                // Pega o primeiro resultado
                JSONObject produto = content.getJSONObject(0);

                String nomeProduto   = produto.optString("nomeProduto", nome);
                String laboratorio   = produto.optString("nomeLaboratorio", "—");
                String numExpediente = produto.optString("numExpediente", "");

                // 2. Busca o texto da bula pelo expediente
                String indicacoes        = "Consulte a bula completa para informações detalhadas.";
                String posologia         = "Consulte a bula completa para informações detalhadas.";
                String contraindicacoes  = "Consulte a bula completa para informações detalhadas.";
                String efeitosColaterais = "Consulte a bula completa para informações detalhadas.";

                if (!numExpediente.isEmpty()) {
                    String urlDetalhe = "https://consultas.anvisa.gov.br/api/consulta/bulario/" + URLEncoder.encode(numExpediente, "UTF-8");
                    String jsonDetalhe = fazerGet(urlDetalhe);

                    if (jsonDetalhe != null) {
                        JSONObject detalhe = new JSONObject(jsonDetalhe);

                        String ind = detalhe.optString("indicacoes", "").trim();
                        String pos = detalhe.optString("posologia", "").trim();
                        String con = detalhe.optString("contraindicacoes", "").trim();
                        String efe = detalhe.optString("reacoesAdversas", "").trim();

                        if (!ind.isEmpty()) indicacoes        = ind;
                        if (!pos.isEmpty()) posologia         = pos;
                        if (!con.isEmpty()) contraindicacoes  = con;
                        if (!efe.isEmpty()) efeitosColaterais = efe;
                    }

                    urlBulaCompleta = "https://consultas.anvisa.gov.br/#/bulario/" + numExpediente;
                }

                // Variáveis finais para o lambda
                String nomeFinal        = nomeProduto;
                String labFinal         = laboratorio;
                String indFinal         = indicacoes;
                String posFinal         = posologia;
                String conFinal         = contraindicacoes;
                String efeFinal         = efeitosColaterais;

                handler.post(() -> {
                    progressBula.setVisibility(View.GONE);
                    scrollBula.setVisibility(View.VISIBLE);

                    txtNomeBula.setText(nomeFinal);
                    txtLaboratorio.setText("Laboratório: " + labFinal);
                    txtIndicacoes.setText(indFinal);
                    txtPosologia.setText(posFinal);
                    txtContraindicacoes.setText(conFinal);
                    txtEfeitosColaterais.setText(efeFinal);

                    if (urlBulaCompleta.isEmpty()) {
                        btBulaCompleta.setEnabled(false);
                        btBulaCompleta.setText("Bula completa indisponível");
                    }
                });

            } catch (Exception e) {
                mostrarErro("Erro ao buscar bula: " + e.getMessage());
            }
        });
    }

    private String fazerGet(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            int code = conn.getResponseCode();
            if (code != 200) return null;

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void mostrarErro(String msg) {
        handler.post(() -> {
            progressBula.setVisibility(View.GONE);
            txtErro.setVisibility(View.VISIBLE);
            txtErro.setText(msg);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}