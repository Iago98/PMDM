package com.example.contactossqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class ContactEditionActivity extends AppCompatActivity {
    EditText edNombre,edApellido,edEmail,edTelefono;
    ListaContactos app;

public static int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("alala");

        setContentView(R.layout.activity_contact_edition);
        Intent datosEnviados = this.getIntent();
        System.out.println("alala2");
        app = (ListaContactos) this.getApplication();
        System.out.println("alala3");
        final Button btGuardar = (Button) this.findViewById(R.id.btGuardar);
        final Button btCancelar = (Button) this.findViewById(R.id.btCancelar);
         edNombre = (EditText) this.findViewById(R.id.edNombre);
         edApellido = (EditText) this.findViewById(R.id.edApellido);
      edEmail = (EditText) this.findViewById(R.id.edEmail);
      edTelefono = (EditText) this.findViewById(R.id.edTelefono);
        System.out.println("alala4");
        final int pos = datosEnviados.getExtras().getInt("pos");
        System.out.println("alala5");
        String nombre = "";
        String apellido = "";
        String email = "";
        String telefono = "";
        System.out.println("alala6");
        if (pos >= 0) {
            nombre = app.getListaContacto().get(pos).getNombre();
            apellido = app.getListaContacto().get(pos).getApellido();
            email = app.getListaContacto().get(pos).getEmail();
            telefono = String.valueOf(app.getListaContacto().get(pos).getTelefono());
        }
        System.out.println("alala7");
        edNombre.setText(nombre);
        edApellido.setText(apellido);
        edEmail.setText(email);
        edTelefono.setText(telefono);
        System.out.println("alala8");
        btCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ContactEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                ContactEditionActivity.this.finish();
            }
        });
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edNombre.getText().toString().isEmpty() || edApellido.getText().toString().isEmpty() || edEmail.getText().toString().isEmpty() || edTelefono.getText().toString().isEmpty()) {
                    error();
                } else {
                    registrarContactoSQL();
                }
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ContactEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                ContactEditionActivity.this.finish();
            }
        });

    }


    private void registrarContactoSQL() {
        ConexionSQLite conn= new ConexionSQLite(this, "bd_contactos",null,1);
        generarid();
        SQLiteDatabase db=conn.getWritableDatabase();

        //insert into contact(id,nombre,apellido,email,telefono) values ();
        String insert="INSERT INTO "+Utilidades.TABLA_CONTACTO
                +" ( "+ Utilidades.CAMPO_ID+","+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_APELLIDO+","+Utilidades.CAMPO_EMAIL+","+Utilidades.CAMPO_TELEFONO+" ) "+
                "VALUES ( "+id+",'"+edNombre.getText().toString()+"','"+edApellido.getText().toString()+"','"+edEmail.getText().toString()+"',"+edTelefono.getText().toString()+")";
        System.out.println(insert.toString());
        db.execSQL(insert);
        db.close();
        System.out.println(id +edNombre.getText().toString()+edApellido.getText().toString()+edEmail.getText().toString()+Integer.valueOf(edTelefono.getText().toString()));
        app.addContact( id, edNombre.getText().toString(),edApellido.getText().toString(),edEmail.getText().toString(),Integer.valueOf(edTelefono.getText().toString()));
        ContactEditionActivity.this.setResult(Activity.RESULT_OK);
        ContactEditionActivity.this.finish();
    }

    public void generarid(){

        id++;
    }
        private void error(){
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Campo vacío/s.");
            builder.setPositiveButton("Reintentar", null);
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContactEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                    ContactEditionActivity.this.finish();
                }
            });
            builder.create().show();
        }


}