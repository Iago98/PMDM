package com.example.todo4;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;


    //    @Override
//    public void onResume() {
//        super.onResume();
//        adaptadorItems.clear();
//        items.clear();
//        fechas.clear();
//        int size;
//
//        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
//        size = Integer.valueOf(prefs.getString("tamañoItems","0"));
//
//
//        for(int z = 0; z<size; z++){
//            String item = prefs.getString("item" + z,"");
//            String fecha= prefs.getString("fecha"+z,"");
//
//            items.add(item);
//            fechas.add(fecha);
//
//        }
//        ArrayList<String> caducados=compararFechas();
//
//        if(caducados.size()==0){
//
//        }else {
//            itemsCaducadosBorrar(caducados);
//        }
//
//
//    }
//    @Override
//    public void onPause()
//    {
//        super.onPause();
//
//        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();
//
//        edit.putString("tamañoItems",String.valueOf(items.size()));
//
//
//        for(int z=0;z<items.size();z++){
//            edit.putString(("item"+String.valueOf(z)), String.valueOf(items.get(z)));
//            edit.putString(("fecha"+String.valueOf(z)), String.valueOf(fechas.get(z)));
//
//        }
//
//        edit.commit();
//        adaptadorItems.clear();
//        items.clear();
//        fechas.clear();
//
//
//    }
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

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        boolean toret = false;
        ListaTodo list = (ListaTodo) this.getApplication();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        int pos = info.position;
        switch (menuItem.getItemId()) {
            case R.id.opEliminar:
                list.eliminar(pos);
                MainActivity.this.adaptadorItems.notifyDataSetChanged();
                toret = true;
                break;
            case R.id.opModificar:

                MainActivity.this.modificar(pos);
                toret = true;
                break;
        }
        return toret;
    }

//                @Override
//                public void onResume () {
//                super.onResume();
//                itemsAdapter.clear();
//                items.clear();
//                fechas.clear();
//                int size;
//
//                SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
//                size = Integer.valueOf(prefs.getString("tamañoItems", "0"));
//
//
//                for (int z = 0; z < size; z++) {
//                    String item = prefs.getString("item" + z, "");
//                    String fecha = prefs.getString("fecha" + z, "");
//
//                    items.add(item);
//                    fechas.add(fecha);
//
//                }
//                ArrayList<String> caducados = compararFechas();
//
//                if (caducados.size() == 0) {
//
//                } else {
//                    itemsCaducadosBorrar(caducados);
//                }
//
//
//            }
//            @Override
//            public void onPause ()
//            {
//                super.onPause();
//
//                SharedPreferences.Editor edit = this.getPreferences(Context.MODE_PRIVATE).edit();
//
//                edit.putString("tamañoItems", String.valueOf(items.size()));
//
//
//                for (int z = 0; z < items.size(); z++) {
//                    edit.putString(("item" + String.valueOf(z)), String.valueOf(items.get(z)));
//                    edit.putString(("fecha" + String.valueOf(z)), String.valueOf(fechas.get(z)));
//
//                }
//
//                edit.commit();
//                itemsAdapter.clear();
//                items.clear();
//                fechas.clear();
//
//
//            }


    private void modificar(int i) {
        Intent subActividad = new Intent(MainActivity.this, TodoEditionActivity.class);
        subActividad.putExtra("pos", i);
        MainActivity.this.startActivityForResult(subActividad, CODIGO_EDICION_ITEM);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        final ListaTodo app = (ListaTodo) this.getApplication();
        ListView lvLista = (ListView) this.findViewById(R.id.lvItems);


        // Lista
        this.adaptadorItems = new ArrayAdapter<Todo>(this, android.R.layout.simple_selectable_list_item, app.getTodoList());
        lvLista.setAdapter(this.adaptadorItems);


        Button btAdd = (Button) this.findViewById(R.id.btAdd);

        this.registerForContextMenu(lvLista);

        lvLista.setLongClickable(true);
        lvLista.setAdapter(this.adaptadorItems);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent(MainActivity.this, TodoEditionActivity.class);
                subActividad.putExtra("pos", -1);
                MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
            }
        });
    }

    private void añadir() {
        Intent subActividad = new Intent(MainActivity.this, TodoEditionActivity.class);
        subActividad.putExtra("pos", -1);
        MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
    }


    //decision resultado
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


//            private Calendar recuperarFechaActual () {
//
//                Calendar fecha = new GregorianCalendar(Locale.getDefault());
//                return fecha;
//            }


    int year;
    int month;
    int day;


//    private void seleccionarFechaModificada(final String texto,final int position) {
//        final DatePickerDialog dlg = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                MainActivity.this.year = year;
//                MainActivity.this.month = month;
//                MainActivity.this.day = dayOfMonth;
//                System.out.println(MainActivity.this.year);
//
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Calendar fecha = new GregorianCalendar(year, month, dayOfMonth);
//                String strDate = dateFormat.format(fecha.getTime());
//                fechas.set(position,strDate);
//                items.set(position,texto);
//                MainActivity.this.itemsAdapter.notifyDataSetChanged();
//
//                //MainActivity.this.itemsAdapter.add(texto);
//
//
//            }
//
//        },
//                2019, 0, 1
//        );
//
//        dlg.show();
//
//
//    }


    private ArrayAdapter<Todo> adaptadorItems;


}
