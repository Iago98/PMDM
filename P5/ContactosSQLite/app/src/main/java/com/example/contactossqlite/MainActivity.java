package com.example.contactossqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;

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
        ConexionSQLite conn= new ConexionSQLite(this, "bd_contactos",null,1);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent(MainActivity.this, ContactEditionActivity.class);
                subActividad.putExtra("pos", -1);
                MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
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
    //decision resultado
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CODIGO_ADICION_ITEM && resultCode == Activity.RESULT_OK) {
//            this.adaptadorItems.notifyDataSetChanged();
//
//        }
//        if (requestCode == CODIGO_EDICION_ITEM && resultCode == Activity.RESULT_OK) {
//            this.adaptadorItems.notifyDataSetChanged();
//        }
//        return;
//    }

    private ArrayAdapter<Contacto> adaptadorItems;

}
