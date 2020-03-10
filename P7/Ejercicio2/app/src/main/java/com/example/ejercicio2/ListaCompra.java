package com.example.ejercicio2;

import java.util.ArrayList;

public class ListaCompra {
    static ArrayList<Compra> listaCompra= new ArrayList<Compra>();

    public static ArrayList<Compra> getListaCompra() {
        return listaCompra;
    }

    public static void setListaCompra(ArrayList<Compra> listaCompra1) {
   listaCompra = listaCompra1;
    }
}
