package com.example.todop3;

public class Todo {

    private String texto;
    private String fecha;

    public Todo() {

    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.getTexto() + ". fecha.: " + this.getFecha();
    }

}
