package com.example.contactossqlite;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;
    static ConexionSQLite conn;


    @Override
    public void onStart() {
        super.onStart();
        adaptadorItems.clear();
        conn= new ConexionSQLite(this, "bd_contactos",null,1);
        ListaContactos app = (ListaContactos) this.getApplication();
         final ListView lvLista = (ListView) this.findViewById(R.id.lvItems);

        this.adaptadorDB = new SimpleCursorAdapter(
                this,
                R.layout.lvlcontacto,
                null,
                new String[] { Utilidades.CAMPO_NOMBRE, Utilidades.CAMPO_APELLIDO,Utilidades.CAMPO_EMAIL,Utilidades.CAMPO_TELEFONO },
                new int[] { R.id.tablaNombre, R.id.tablaApellido,R.id.tablaEmail,R.id.tablaTelef}
        );
        ArrayList<Contacto> listaa= recuperarListaContactos();
        for(int x=0;x<listaa.size();x++){
            System.out.println("aqui rec lista"+listaa.get(x).getId()+listaa.get(x).getNombre()+listaa.get(x).getApellido()+listaa.get(x).getEmail()+listaa.get(x).getTelefono());
            app.addContact(listaa.get(x).getId(),listaa.get(x).getNombre(),listaa.get(x).getApellido(),listaa.get(x).getEmail(),listaa.get(x).getTelefono());

        }
        lvLista.setAdapter( this.adaptadorDB );
      this.actualizaContacto();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        Button btBuscar = (Button) this.findViewById(R.id.btBuscar);
        ListView lvLista = (ListView) this.findViewById(R.id.lvItems);

        final ListaContactos app = (ListaContactos) this.getApplication();


        this.adaptadorItems = new ArrayAdapter<Contacto>(this, android.R.layout.simple_selectable_list_item, app.getListaContacto());
        lvLista.setAdapter(this.adaptadorItems);
        this.registerForContextMenu(lvLista);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                añadir();
            }
        });
        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent(MainActivity.this, ConsultarContacto.class);
                subActividad.putExtra("pos", -1);
                MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
            }
        });









    }
    public ArrayList<Contacto> recuperarListaContactos(){
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM "+Utilidades.TABLA_CONTACTO,null);
        ArrayList<Contacto> listaCon = new ArrayList<Contacto>();

        while (cursor.moveToNext()){
            Contacto contc= new Contacto(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
            System.out.println("RECUPERANDO"+cursor.getInt(0)+ "AQUI NOM"+cursor.getString(1) +"AQUI APP"+cursor.getString(2)+ "AQUI E"+cursor.getString(3) +cursor.getInt(4));
            System.out.println("PETARA");
            listaCon.add(contc);
        }
        cursor.close();
        return listaCon;
    }
    private void añadir(){
        Intent subActividad = new Intent(MainActivity.this, ContactEditionActivity.class);
        subActividad.putExtra("pos", -1);
        MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        this.getMenuInflater().inflate(R.menu.actions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean toret = false;

        switch (menuItem.getItemId()) {
            case R.id.opAñadir:
                añadir();
                toret = true;
                break;

        }

        return toret;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo cmi) {
        this.getMenuInflater().inflate(R.menu.context_menu, contextMenu);


    }
    public void actualizaContacto(){
        this.adaptadorDB.changeCursor( this.conn.getContacto() );
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        boolean toret = false;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        int pos = info.position;
        System.out.println("posicionnn"+pos);
        switch (menuItem.getItemId()) {
            case R.id.opEliminar:

                eliminarIdBD(pos);

                toret = true;
                break;
            case R.id.opModificar:
                modificar(pos);
                toret = true;
                break;
        }
        return toret;
    }
    private void modificar(int i) {

        Intent subActividad = new Intent(MainActivity.this, ContactEditionActivity.class);
        subActividad.putExtra("pos", i);
        MainActivity.this.startActivityForResult(subActividad, CODIGO_EDICION_ITEM);
    }


    private void eliminarIdBD (final int pos){
       final ListaContactos list = (ListaContactos) this.getApplication();

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Borrar");
            builder.setMessage("Seguro que quieres borrar el elemento ?");
            builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SQLiteDatabase db=conn.getWritableDatabase();
                    int id= list.eliminar(pos);
                    String[] parametros = {String.valueOf(id)};
                    db.delete(Utilidades.TABLA_CONTACTO,Utilidades.CAMPO_ID+"=?",parametros);
                    db.close();
                    actualizaContacto();
                    MainActivity.this.adaptadorItems.notifyDataSetChanged();
                    adaptadorDB.notifyDataSetChanged();

                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.create().show();
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODIGO_ADICION_ITEM && resultCode == Activity.RESULT_OK) {
            this.adaptadorItems.notifyDataSetChanged();

        }
        if (requestCode == CODIGO_EDICION_ITEM && resultCode == Activity.RESULT_OK) {
            this.adaptadorItems.notifyDataSetChanged();
        }
        return;
    }

    private ArrayAdapter<Contacto> adaptadorItems;
    private SimpleCursorAdapter adaptadorDB;
}
