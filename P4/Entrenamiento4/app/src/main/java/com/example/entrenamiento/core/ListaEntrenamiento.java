package com.example.entrenamiento.core;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ListaEntrenamiento extends Application {

    private List<Entrenamiento> entrenamiento;

    @Override
    public void onCreate() {
        super.onCreate();
        this.entrenamiento = new ArrayList<>();
    }

    public List<Entrenamiento> getEntrenamientoList() {
        return this.entrenamiento;
    }


    public void addEntrenamiento(String nombre, float km, float tiempo) {
        Entrenamiento entrenamiento = new Entrenamiento();

        entrenamiento.setNombreEntrenamiento(nombre);
        entrenamiento.setTiempoMinutos(tiempo);
        entrenamiento.setDistanciaKilometros(km);
        System.out.println("aqi"+km);
        this.entrenamiento.add(entrenamiento);
    }

    public void modifyEntrenamiento(int pos, String nombre, float km, float tiempo) {
        Entrenamiento entrenamiento = new Entrenamiento();

        entrenamiento.setNombreEntrenamiento(nombre);
        entrenamiento.setTiempoMinutos(tiempo);
        entrenamiento.setDistanciaKilometros(km);
        this.entrenamiento.set(pos, entrenamiento);
    }

    public void eliminar(int pos) {
        this.entrenamiento.remove(pos);
    }

}