package com.example.entrenamiento.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.text.DecimalFormat;

import android.widget.Button;

import java.text.DecimalFormatSymbols;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.os.Bundle;


import com.example.entrenamiento.core.Entrenamiento;


public class MainActivity extends AppCompatActivity {

    DecimalFormatSymbols format;
    DecimalFormat formato = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.entrenamiento = new ArrayList<Entrenamiento>();
        this.item = new ArrayList<String>();
        Button btañadir = (Button) this.findViewById(R.id.btAdd);
        btañadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.onAdd();
            }
        });

        ListView lista = (ListView) this.findViewById(R.id.entrenamientos);
        this.registerForContextMenu(lista);

       this.ItemAdapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_selectable_list_item, this.item);
        lista.setAdapter(ItemAdapter);


    }
    @Override
    public void onPause()
    {
        super.onPause();

        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();

        edit.putString("tamaño",String.valueOf(entrenamiento.size()));

        for(int z=0;z<entrenamiento.size();z++){
            edit.putString(("nombre"+String.valueOf(z)), String.valueOf(entrenamiento.get(z).getNombreEntrenamiento()));
            edit.putString(("distancia"+String.valueOf(z)), String.valueOf(entrenamiento.get(z).getDistanciaKilometros()));
            edit.putString(("tiempo"+String.valueOf(z)), String.valueOf(entrenamiento.get(z).getTiempoMinutos()));

        }

        edit.commit();
        ItemAdapter.clear();
        item.clear();


    }

    @Override
    public void onResume() {
        super.onResume();

        ItemAdapter.clear();
        item.clear();
        entrenamiento.clear();
        int size;

        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        size = Integer.valueOf(prefs.getString("tamaño","0"));


        for(int z = 0; z<size; z++){
            String nombre = prefs.getString("nombre" + z,"");
            String distancia = prefs.getString("distancia" +z, "");
            String tiempo = prefs.getString("tiempo" + z,"");
            Entrenamiento ent = new Entrenamiento();
            ent.setNombreEntrenamiento(nombre);
            ent.setDistanciaKilometros(Float.valueOf(distancia));
            ent.setTiempoMinutos(Float.valueOf(tiempo));
            MainActivity.this.entrenamiento.add(ent);
            MainActivity.this.ItemAdapter.add(ent.toString());
        }
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
            case R.id.opStats:
                stats();
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

        }

        return toret;
    }
    private void eliminar( final int pos){
        if (pos >= 0) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Borrado");
            builder.setMessage("Seguro que quieres borrar el elemento: '" + MainActivity.this.item.get(pos) + "'?");
            builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.item.remove(pos);
                    MainActivity.this.entrenamiento.remove(pos);
                    MainActivity.this.ItemAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.create().show();
        }


    }
    private void onAdd() {
        final Entrenamiento training = new Entrenamiento();
        Context context = MainActivity.this.getApplicationContext();
        LinearLayout ly = new LinearLayout(context);
        ly.setOrientation(LinearLayout.VERTICAL);
        final EditText nombre = new EditText(context);
        nombre.setHint("Nombre");
        ly.addView(nombre);
        final EditText distancia = new EditText(context);
        distancia.setHint("Distancia en Km");
        distancia.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        ly.addView(distancia);
        final EditText tiempo = new EditText(context);
        tiempo.setHint("Tiempo En Min");
        tiempo.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        ly.addView(tiempo);


        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Entrenamiento Nuevo");
        builder.setMessage("Datos a introducir: ");
        builder.setView(ly);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (distancia.getText().toString().isEmpty() || tiempo.getText().toString().isEmpty()||nombre.getText().toString().isEmpty()) {
                    error();
                } else {
                    training.setDistanciaKilometros(Float.valueOf(distancia.getText().toString()));
                    training.setTiempoMinutos(Float.valueOf(tiempo.getText().toString()));
                    training.setNombreEntrenamiento(nombre.getText().toString());
                    MainActivity.this.entrenamiento.add(training);
                    MainActivity.this.ItemAdapter.add(training.toString());

                }

            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }



    private void error() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío/s.");
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.onAdd();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void stats() {
        float totalKm = 0;
        float totalMinKm = 0;
        String a="NaN";
        DecimalFormat df = new DecimalFormat("#.00");
        for (int i = 0; i < entrenamiento.size(); i++) {
            totalKm = totalKm + entrenamiento.get(i).getDistanciaKilometros();

            totalMinKm = totalMinKm + entrenamiento.get(i).calcularMinutosKilometro();
        }
        totalMinKm = totalMinKm / entrenamiento.size();
        if( String.valueOf(totalMinKm).equals(a)){
            totalMinKm=0;
        }
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stats");
        builder.setMessage("Km totales: " + df.format(totalKm) + "\nMedia de: " + df.format(totalMinKm)+ " Min/Km.");
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }


    ArrayList<String> item;
    ArrayAdapter<String> ItemAdapter;
    ArrayList<Entrenamiento> entrenamiento;
}
