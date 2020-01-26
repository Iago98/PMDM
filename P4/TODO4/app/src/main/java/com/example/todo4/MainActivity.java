package com.example.todo4;


import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;


        @Override
    public void onResume() {
        super.onResume();
            final ListaTodo app = (ListaTodo) this.getApplication();


        ArrayList<String> caducados=compararFechas();

        if(caducados.size()==0){

        }else {
            itemsCaducadosBorrar(caducados);
        }


    }

    private ArrayList<String> compararFechas() {
        int valor = 5;
        int index = 0;
        final TextView tv = new TextView(this);
        int mensajes = 0;
        ArrayList<String> posicionCaducada = new ArrayList<String>();
        final ListaTodo app = (ListaTodo) this.getApplication();

        for (int x = 0; x < app.getTodoList().size(); x++) {
            String fechaArray = app.getTodoList().get(x).getFecha();
            System.out.println("La fecha" + x + " " + fechaArray);
            Calendar fechaRecuperada = recuperarFechaActual();
            Calendar fecha_calendar = Calendar.getInstance();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha_date = sdf.parse(fechaArray);
                fecha_calendar.setTime(fecha_date);
                System.out.println("La fecha q recupero es " + fechaRecuperada.getTime());
                int val = fechaRecuperada.compareTo(fecha_calendar);
                if (val == -1) {

                } else {

                    posicionCaducada.add(String.valueOf(x));


                }

            } catch (ParseException e) {
                Log.v("printf", "ERROR!: can not parsing! ");
            }

        }
        for (int i = 0; i < posicionCaducada.size(); i++) {
            System.out.println("estos indices son los que estan caducados" + i);

        }return posicionCaducada;
    }

    private void itemsCaducadosBorrar(ArrayList<String> listaCaducada) {
        final ArrayList<String> caducados= listaCaducada;
        final ArrayList selectedItems = new ArrayList();
        final ArrayList<String> seeeelected = new ArrayList<String>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList<String> itemCaducados= new ArrayList<>();
        final ListaTodo app = (ListaTodo) this.getApplication();

        final boolean[] array = new boolean[caducados.size()];
        for(int x=0;x<caducados.size();x++){
            itemCaducados.add(app.getTodoList().get(Integer.parseInt(caducados.get(x))).getTexto());
            array[x]=false;


        }
        String[] stockArr = itemCaducados.toArray(new String[itemCaducados.size()]);
        System.out.println("aqui va tu itemscaducados: "+itemCaducados.toString());
        builder.setMultiChoiceItems(stockArr,array, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean b) {
                if(b){
                    seeeelected.add(Integer.toString(which));

                }else if(seeeelected.contains(Integer.toString(which))){

                    seeeelected.remove(Integer.toString(Integer.valueOf(which)));
                }
                System.out.println("MARCADOS "+seeeelected.toString());

            }
        });
        builder.setPositiveButton("Eliminar Marcados", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("Items seleccionados para borrar"+which);
                for (int i=0;i<seeeelected.size();i++){




                    int b=Integer.valueOf(seeeelected.get(i));


                    String fechass= app.getTodoList().get(Integer.valueOf(caducados.get(b))).getFecha();
                    String algo= app.getTodoList().get(Integer.valueOf(caducados.get(b))).getTexto();
                    for (int s=0;s<app.getTodoList().size();s++){
                        if(app.getTodoList().get(s).getTexto().equals(algo)&&fechass.equals(app.getTodoList().get(s).getFecha())){
                            MainActivity.this.adaptadorItems.remove( app.getTodoList().get(s));
                        }else {

                        }
                    }
                    System.out.println("ESTOS SON LOS CADUCADSOS "+algo);



                }
                MainActivity.this.adaptadorItems.notifyDataSetChanged();


            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor edit = this.getPreferences(Context.MODE_PRIVATE).edit();

        final ListaTodo app = (ListaTodo) this.getApplication();
        edit.putString("tamañoItems", String.valueOf(app.getTodoList().size()));


        for (int z = 0; z < app.getTodoList().size(); z++) {
            edit.putString(("item" + String.valueOf(z)), String.valueOf(app.getTodoList().get(z).getTexto()));
            edit.putString(("fecha" + String.valueOf(z)), String.valueOf(app.getTodoList().get(z).getFecha()));

        }
        edit.commit();

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
        int size;
        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        size = Integer.valueOf(prefs.getString("tamañoItems","0"));


        for(int z = 0; z<size; z++){
            String item = prefs.getString("item" + z,"");
            String fecha= prefs.getString("fecha"+z,"");
            System.out.println("intento recuperar");
            app.addTodo(fecha,item);
        }
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


            private Calendar recuperarFechaActual () {

        Calendar fecha = new GregorianCalendar(Locale.getDefault());
        return fecha;
    }


    int year;
    int month;
    int day;





    private ArrayAdapter<Todo> adaptadorItems;


}
