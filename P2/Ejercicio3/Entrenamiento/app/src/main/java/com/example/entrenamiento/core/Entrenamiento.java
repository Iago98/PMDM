package com.example.entrenamiento.core;

import java.util.ArrayList;

public class Entrenamiento {

    private String nombreEntrenamiento;
    private float tiempoMinutos;
    private float distanciaKilometros;


    public float calcularMinutosKilometro() {


        return tiempoMinutos / distanciaKilometros;
    }

    public float calcularSegPorKilometro() {


        return tiempoMinutos * 60 / distanciaKilometros;

    }

    public float calcularvelocidadMedia() {

        return this.distanciaKilometros / (tiempoMinutos / 60);
    }

    @Override
    public String toString() {
        return "Entrenamiento: " + nombreEntrenamiento;
    }

    public String getNombreEntrenamiento() {
        return nombreEntrenamiento;
    }

    public void setNombreEntrenamiento(String nombreEntrenamiento) {
        this.nombreEntrenamiento = nombreEntrenamiento;
    }

    public float getTiempoMinutos() {
        return tiempoMinutos;
    }

    public void setTiempoMinutos(float tiempoMinutos) {
        this.tiempoMinutos = tiempoMinutos;
    }

    public float getDistanciaKilometros() {
        return distanciaKilometros;
    }

    public void setDistanciaKilometros(float distanciaKilometros) {
        this.distanciaKilometros = distanciaKilometros;
    }
}

