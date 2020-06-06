package com.example.pfc.core;

public class Menu {

    private Integer id;
    private String titulo;
    private String descripcion;
    private int color_resource;

    public int getColor_resource() {
        return color_resource;
    }

    public void setColor_resource(int color_resource) {
        this.color_resource = color_resource;
    }

    public Menu() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", color_resource=" + color_resource +
                '}';
    }
}
