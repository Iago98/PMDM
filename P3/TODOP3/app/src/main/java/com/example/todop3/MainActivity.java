package com.example.todop3;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();
        itemsAdapter.clear();
        items.clear();
        fechas.clear();
        int size;

        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        size = Integer.valueOf(prefs.getString("tamañoItems","0"));


        for(int z = 0; z<size; z++){
            String item = prefs.getString("item" + z,"");
            String fecha= prefs.getString("fecha"+z,"");

            items.add(item);
            fechas.add(fecha);

        }
        ArrayList<String> caducados=compararFechas();

        if(caducados.size()==0){

        }else {
            itemsCaducadosBorrar(caducados);
        }


    }
    @Override
    public void onPause()
    {
        super.onPause();

        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();

        edit.putString("tamañoItems",String.valueOf(items.size()));


        for(int z=0;z<items.size();z++){
            edit.putString(("item"+String.valueOf(z)), String.valueOf(items.get(z)));
            edit.putString(("fecha"+String.valueOf(z)), String.valueOf(fechas.get(z)));

        }

        edit.commit();
        itemsAdapter.clear();
        items.clear();
        fechas.clear();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );

        this.getMenuInflater().inflate( R.menu.actions_menu, menu );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        boolean toret = false;

        switch( menuItem.getItemId() ) {
            case R.id.opAñadir:
                onAdd();
                toret = true;
                break;

        }

        return toret;
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo cmi)
    {
        this.getMenuInflater().inflate(R.menu.context_menu, contextMenu);



    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        boolean toret = false;

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        int pos = info.position;
        switch( menuItem.getItemId() ) {
            case R.id.opEliminar:
                MainActivity.this.eliminar(pos);
                toret=true;
                break;
            case R.id.opModificar:
                MainActivity.this.modificar(pos);
                toret=true;
                break;

        }

        return toret;
    }
    private void modificar(final int position){

        final EditText et = new EditText(MainActivity.this);

        String texto = MainActivity.this.items.get(position).toString();
        String fecha = MainActivity.this.fechas.get((position)).toString();
        System.out.println("la fecha que voy a mostrar es " + fecha);
        et.setText(texto);
        if (position >= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Modificar");
            builder.setMessage("Esta actividad cadula el: " + fecha);
            builder.setView(et);
            builder.setNegativeButton("Cancelar", null);
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String text = et.getText().toString();
                    if(text.equals("")){
                        error();
                    }else{
                        seleccionarFechaModificada(text,position);
                    }
                }

            });
            builder.create().show();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);


        this.items = new ArrayList<String>();



        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);
        this.registerForContextMenu(lvItems);

        lvItems.setLongClickable(true);


        this.itemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
        this.itemsAdapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_selectable_list_item, this.items);
        lvItems.setAdapter(this.itemsAdapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();
            }
        });
    }
    private void eliminar(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Cuidado");
        builder.setMessage("¿Seguro que quieres eliminar la actividad?");
        builder.setPositiveButton("Sí, hazlo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.items.remove(pos);
                MainActivity.this.fechas.remove(pos);
                MainActivity.this.itemsAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No, cancela", null);
        builder.create().show();

    }
    private void onAdd() {
        final EditText edText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("To Do...");
        builder.setMessage("¿Que deseas añadir para recordar?");
        builder.setView(edText);
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String text = edText.getText().toString();
                if(text.equals("")){
                    error();
                }else{
                    seleccionarFecha(text);
                }



            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private Calendar recuperarFechaActual() {

        Calendar fecha = new GregorianCalendar(Locale.getDefault());
        return fecha;
    }
    private void error() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío.");
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.onAdd();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void itemsCaducadosBorrar(ArrayList<String> listaCaducada) {
        final ArrayList<String> caducados= listaCaducada;
        final ArrayList selectedItems = new ArrayList();
        final ArrayList<String> seeeelected = new ArrayList<String>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList<String> itemCaducados= new ArrayList<>();

        final boolean[] array = new boolean[caducados.size()];
        for(int x=0;x<caducados.size();x++){
            itemCaducados.add(items.get(Integer.parseInt(caducados.get(x))));
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


                    String fechass= fechas.get(Integer.valueOf(caducados.get(b)));
                    String algo= items.get(Integer.valueOf(caducados.get(b)));
                    for (int s=0;s<items.size();s++){
                        if(items.get(s).equals(algo)&&fechass.equals(fechas.get(s))){
                            MainActivity.this.items.remove( items.get(s));
                            MainActivity.this.fechas.remove( fechas.get(s));
                        }else {

                        }
                    }
                    System.out.println("ESTOS SON LOS CADUCADSOS "+algo);



                }
                MainActivity.this.itemsAdapter.notifyDataSetChanged();


            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private ArrayList<String> compararFechas() {
        int valor = 5;
        int index = 0;
        final TextView tv = new TextView(this);
        int mensajes = 0;
        ArrayList<String> posicionCaducada = new ArrayList<String>();

        for (int x = 0; x < fechas.size(); x++) {
            String fechaArray = fechas.get(x);
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

    int year;
    int month;
    int day;

    private void seleccionarFechaModificada(final String texto,final int position) {
        final DatePickerDialog dlg = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                MainActivity.this.year = year;
                MainActivity.this.month = month;
                MainActivity.this.day = dayOfMonth;
                System.out.println(MainActivity.this.year);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar fecha = new GregorianCalendar(year, month, dayOfMonth);
                String strDate = dateFormat.format(fecha.getTime());
                fechas.set(position,strDate);
                items.set(position,texto);
                MainActivity.this.itemsAdapter.notifyDataSetChanged();

                //MainActivity.this.itemsAdapter.add(texto);


            }

        },
                2019, 0, 1
        );

        dlg.show();


    }

    private void seleccionarFecha(final String texto) {
        final DatePickerDialog dlg = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                MainActivity.this.year = year;
                MainActivity.this.month = month;
                MainActivity.this.day = dayOfMonth;
                System.out.println(MainActivity.this.year);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar fecha = new GregorianCalendar(year, month, dayOfMonth);
                String strDate = dateFormat.format(fecha.getTime());
                System.out.println("fecha del seleccionar fecha " + strDate);
                fechas.add(strDate);
                MainActivity.this.itemsAdapter.add(texto);


            }

        },
                2019, 0, 1
        );

        dlg.show();


    }

    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    private ArrayList<String> fechas = new ArrayList<String>();

}