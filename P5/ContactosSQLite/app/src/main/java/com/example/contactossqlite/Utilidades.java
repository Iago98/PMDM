package com.example.contactossqlite;

public class Utilidades {


    public static final String TABLA_CONTACTO="contacto";
    public static final String CAMPO_ID="ID";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_APELLIDO="apellido";
    public static final String CAMPO_EMAIL="email";
    public static final String CAMPO_TELEFONO="telefono";
    public static final String CREAR_TABLA_CONTACTO="CREATE TABLE " +TABLA_CONTACTO+ " ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+CAMPO_NOMBRE+" TEXT, "+CAMPO_APELLIDO+" TEXT, "+CAMPO_EMAIL+" TEXT, "+CAMPO_TELEFONO+" INTEGER)";


}
