package com.example.contactossqlite;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ListaContactos extends Application {

    private List<Contacto> contacto;

    public List<Contacto> getListaContacto() {
        return contacto;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.contacto = new ArrayList<>();
    }



    public void addContact(int id, String nombre , String apellido,String email ,int telefono) {
        Contacto contacto = new Contacto(id ,nombre , apellido,email, telefono);

        this.contacto.add(contacto);
    }

    public void modifyContact(int pos, int id, String nombre , String apellido,String email ,int telefono) {
        Contacto todo = new Contacto( id, nombre ,apellido,email,telefono);


        this.contacto.set(pos, todo);
    }
    public void eliminar(int pos) {
        this.contacto.remove(pos);
    }


}