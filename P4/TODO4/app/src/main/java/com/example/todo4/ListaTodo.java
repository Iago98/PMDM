package com.example.todo4;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ListaTodo extends Application {

    private List<Todo> todo;
    private String fecha;

    @Override
    public void onCreate() {
        super.onCreate();
        this.todo = new ArrayList<>();
    }

    public List<Todo> getTodoList() {
        return this.todo;
    }


    public void addTodo(String fecha , String nombre) {
        Todo todo = new Todo();

        todo.setTexto(nombre);
        todo.setFecha(fecha);
        System.out.println("fechapelo"+fecha);
        System.out.println("aqui"+todo.getFecha());
        this.todo.add(todo);
    }

    public void modifyTodo(int pos, String nombre, String fecha) {
        Todo todo = new Todo();

        todo.setFecha(fecha);
        todo.setTexto(nombre);
        this.todo.set(pos, todo);
    }
    public void eliminar(int pos) {
        this.todo.remove(pos);
    }


}