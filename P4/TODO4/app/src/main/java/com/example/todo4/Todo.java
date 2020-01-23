package com.example.todo4;

public class Todo {
    private String texto;
    private String fecha;

    public Todo() {
        this.texto=texto;
        this.fecha=fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto=texto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha=fecha;
    }

    @Override
    public String toString() {
        return this.getTexto() + ". fecha: " + this.getFecha();
    }
}