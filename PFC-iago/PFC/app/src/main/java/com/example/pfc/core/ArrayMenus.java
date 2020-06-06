package com.example.pfc.core;

import java.util.ArrayList;

public class ArrayMenus {

    static ArrayList<Menu> listaMenus = new ArrayList<>();

    public static ArrayList<Menu> getListaMenus() {
        return listaMenus;
    }

    public static void setListaMenus(ArrayList<Menu> listaMenuss) {
        listaMenus = listaMenuss;
    }
}
