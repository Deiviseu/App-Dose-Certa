package com.example.dosecerta;

public class Remedio {
    public int id;
    public String nome;
    public String dosagem;
    public String quantidade;
    public String descricao;
    public String fotoUri;
    public String numExpediente; // novo campo

    public Remedio(String nome, String dosagem, String quantidade, String descricao, String fotoUri) {
        this.nome = nome;
        this.dosagem = dosagem;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.fotoUri = fotoUri;
        this.numExpediente = "";
    }
}