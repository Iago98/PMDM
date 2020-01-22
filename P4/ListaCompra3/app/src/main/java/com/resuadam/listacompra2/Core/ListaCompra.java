package com.resuadam.listacompra2.Core;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ListaCompra extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        this.items = new ArrayList<>();
    }

    public List<Item> getItemList() {
        return this.items;
    }

    public void addItem(String nombre, int num) {
        Item item = new Item(nombre);
        item.setNum(num);
        this.items.add(item);
    }

    public void modifyItem(int pos, String nombre, int num) {
        Item item = new Item(nombre);
        item.setNum(num);
        this.items.set(pos, item);
    }

    private List<Item> items;
    private int pos;
}