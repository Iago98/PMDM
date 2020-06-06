package com.example.pfc.core;

public class RegistroRestaurante {

    int id;
    private String login;
    private String nombreHosteleria;
    private double distancia;

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombreHosteleria() {
        return nombreHosteleria;
    }

    public void setNombreHosteleria(String nombreHosteleria) {
        this.nombreHosteleria = nombreHosteleria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}