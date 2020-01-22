package com.example.todop3;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ListaTodo extends Application {

    private List<Todo> todo;
    private int pos;

    @Override
    public void onCreate() {
        super.onCreate();
        this.todo = new ArrayList<>();
    }

    public List<Todo> getTodoList() {
        return this.todo;
    }


    public void addTodo(String nombre, String fecha) {
        Todo todo = new Todo();
        todo.setFecha(fecha);
        todo.setFecha(nombre);
        this.todo.add(todo);
    }

    public void modifyTodo(int pos, String nombre, String fecha) {
        Todo todo = new Todo();
        todo.setFecha(fecha);
        todo.setTexto(nombre);
        this.todo.set(pos, todo);
    }


}
