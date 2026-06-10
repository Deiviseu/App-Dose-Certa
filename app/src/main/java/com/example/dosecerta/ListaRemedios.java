package com.example.dosecerta;

import java.util.ArrayList;
import java.util.List;

public class ListaRemedios {
    private static final List<Remedio> lista = new ArrayList<>();

    public static void adicionar(Remedio r) {
        lista.add(r);
    }

    public static List<Remedio> getLista() {
        return lista;
    }
}