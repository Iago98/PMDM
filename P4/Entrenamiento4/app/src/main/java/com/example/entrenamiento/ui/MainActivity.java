package com.example.entrenamiento.ui;

import android.app.Activity;
import android.app.AlertDialog;


import android.content.Intent;
import android.content.SharedPreferences;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.text.DecimalFormat;

import android.widget.Button;

import java.text.DecimalFormatSymbols;

import androidx.appcompat.app.AppCompatActivity;


import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Context;
import android.widget.ArrayAdapter;

import android.os.Bundle;


import com.example.entrenamiento.core.Entrenamiento;
import com.example.entrenamiento.core.ListaEntrenamiento;


public class MainActivity extends AppCompatActivity {
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;
    DecimalFormatSymbols format;
    DecimalFormat formato = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListaEntrenamiento app = (ListaEntrenamiento) this.getApplication();


        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent(MainActivity.this, EntrenamientoEditionActivity.class);
                subActividad.putExtra("pos", -1);
                MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
            }
        });

        ListView lvLista = (ListView) this.findViewById(R.id.lvItems);
        this.registerForContextMenu(lvLista);


        this.adaptadorEntrenamientos = new ArrayAdapter<Entrenamiento>(this, android.R.layout.simple_selectable_list_item, app.getEntrenamientoList());
        lvLista.setAdapter(this.adaptadorEntrenamientos);
        int size;

        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        size = Integer.valueOf(prefs.getString("tamañoItems", "0"));


        for (int z = 0; z < size; z++) {
            String nombre = prefs.getString("nombre" + z, "");
            String distancia = prefs.getString("distancia" + z, "");
            String tiempo = prefs.getString("tiempo" + z, "");

            app.addEntrenamiento(nombre, Float.valueOf(distancia), Float.valueOf(tiempo));
        }


    }

    //decision resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODIGO_ADICION_ITEM && resultCode == Activity.RESULT_OK) {
            this.adaptadorEntrenamientos.notifyDataSetChanged();

        }
        if (requestCode == CODIGO_EDICION_ITEM && resultCode == Activity.RESULT_OK) {
            this.adaptadorEntrenamientos.notifyDataSetChanged();
        }
        return;
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor edit = this.getPreferences(Context.MODE_PRIVATE).edit();

        final ListaEntrenamiento app = (ListaEntrenamiento) this.getApplication();
        edit.putString("tamañoItems", String.valueOf(app.getEntrenamientoList().size()));

        for (int z = 0; z < app.getEntrenamientoList().size(); z++) {
            edit.putString(("nombre" + String.valueOf(z)), String.valueOf(app.getEntrenamientoList().get(z).getNombreEntrenamiento()));
            edit.putString(("distancia" + String.valueOf(z)), String.valueOf(app.getEntrenamientoList().get(z).getDistanciaKilometros()));
            edit.putString(("tiempo" + String.valueOf(z)), String.valueOf(app.getEntrenamientoList().get(z).getTiempoMinutos()));

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
            case R.id.opStats:
                stats();
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
        ListaEntrenamiento list = (ListaEntrenamiento) this.getApplication();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        int pos = info.position;
        switch (menuItem.getItemId()) {
            case R.id.opEliminar:
                toret = true;
                list.eliminar(pos);
                MainActivity.this.adaptadorEntrenamientos.notifyDataSetChanged();

                break;
            case R.id.opModificar:

                MainActivity.this.modificar(pos);
                toret = true;
                break;
        }
        return toret;
    }

    private void añadir() {
        Intent subActividad = new Intent(MainActivity.this, EntrenamientoEditionActivity.class);
        subActividad.putExtra("pos", -1);
        MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
    }

    private void modificar(int i) {
        Intent subActividad = new Intent(MainActivity.this, EntrenamientoEditionActivity.class);
        subActividad.putExtra("pos", i);
        MainActivity.this.startActivityForResult(subActividad, CODIGO_EDICION_ITEM);
    }


    private void stats() {
        float totalKm = 0;
        float totalMinKm = 0;
        String a = "NaN";
        final ListaEntrenamiento app = (ListaEntrenamiento) this.getApplicationContext();
        DecimalFormat df = new DecimalFormat("#.00");
        for (int i = 0; i < app.getEntrenamientoList().size(); i++) {
            totalKm = totalKm + app.getEntrenamientoList().get(i).getDistanciaKilometros();

            totalMinKm = totalMinKm + app.getEntrenamientoList().get(i).calcularMinutosKilometro();
        }
        totalMinKm = totalMinKm / app.getEntrenamientoList().size();
        if (String.valueOf(totalMinKm).equals(a)) {
            totalMinKm = 0;
        }
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stats");
        builder.setMessage("Km totales: " + df.format(totalKm) + "\nMedia de: " + df.format(totalMinKm) + " Min/Km.");
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }


    private ArrayAdapter<Entrenamiento> adaptadorEntrenamientos;
}
