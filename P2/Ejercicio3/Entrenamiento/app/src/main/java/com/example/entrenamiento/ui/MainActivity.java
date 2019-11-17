package com.example.entrenamiento.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;

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
        this.entrenamientos = new ArrayList<Entrenamiento>();
        this.item = new ArrayList<String>();
        Button btañadir = (Button) this.findViewById(R.id.btAdd);
        btañadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.onAdd();
            }
        });
        Button btstats = (Button) this.findViewById(R.id.btStats);
        btstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stats();
            }
        });
        ListView lista = (ListView) this.findViewById(R.id.entrenamientos);

        lista.setLongClickable(true);
        this.ItemAdapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_selectable_list_item, this.item);
        lista.setAdapter(ItemAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                if (pos >= 0) {
                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Información del entrenamiento");
                    String texto = "Nombre: " + MainActivity.this.entrenamientos.get(pos).getNombreEntrenamiento() + "\nDistancia: " + MainActivity.this.entrenamientos.get(pos).getDistanciaKilometros() + " Km\nTiempo: " + MainActivity.this.entrenamientos.get(pos).getTiempoMinutos() + " min\nVelocidad media: " + formato.format(MainActivity.this.entrenamientos.get(pos).calcularvelocidadMedia()) + " Km/h\nMinutos por Km: " + formato.format(MainActivity.this.entrenamientos.get(pos).calcularMinutosKilometro());
                    builder.setMessage(texto);
                    builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            modificar(pos);
                        }
                    });
                    builder.setNegativeButton("Atrás", null);
                    builder.create().show();
                }
            }
        });
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int pos, long id) {
                if (pos >= 0) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Borrado");
                    builder.setMessage("Seguro que quieres borrar el elemento: '" + MainActivity.this.item.get(pos) + "'?");
                    builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.item.remove(pos);
                            MainActivity.this.entrenamientos.remove(pos);
                            MainActivity.this.ItemAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.create().show();
                }
                return true;
            }
        });
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
        distancia.setHint("Distancia");
        distancia.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        ly.addView(distancia);
        final EditText tiempo = new EditText(context);
        tiempo.setHint("Tiempo");
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
                    MainActivity.this.entrenamientos.add(training);
                    MainActivity.this.ItemAdapter.add(training.toString());

                }

            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void modificar(final int position) {

        Context context = MainActivity.this.getApplicationContext();

        LinearLayout ly = new LinearLayout(context);

        ly.setOrientation(LinearLayout.VERTICAL);

        format = new DecimalFormatSymbols();
        format.setDecimalSeparator('.');
        formato = new DecimalFormat("#0.00", format);

        final EditText nombre = new EditText(context);
        nombre.setHint("Entrenamiento");
        nombre.setText(entrenamientos.get(position).getNombreEntrenamiento());
        ly.addView(nombre);
        final EditText distancia = new EditText(context);
        distancia.setHint("Distancia en Km");
        distancia.getInputType();
        distancia.setText(formato.format(entrenamientos.get(position).getDistanciaKilometros()));
        distancia.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        ly.addView(distancia);

        final EditText tiempo = new EditText(context);
        tiempo.setHint("Tiempo en min");
        tiempo.getInputType();
        tiempo.setText(formato.format(entrenamientos.get(position).getTiempoMinutos()));
        tiempo.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        ly.addView(tiempo);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Modificar");
        builder.setView(ly);
        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (distancia.getText().toString().isEmpty() || tiempo.getText().toString().isEmpty()||nombre.getText().toString().isEmpty()) {
                    error();
                } else {
                    entrenamientos.get(position).setDistanciaKilometros(Float.valueOf(distancia.getText().toString()));
                    entrenamientos.get(position).setTiempoMinutos(Float.valueOf(tiempo.getText().toString()));
                    entrenamientos.get(position).setNombreEntrenamiento((nombre.getText().toString()));
                    MainActivity.this.item.set(position, entrenamientos.get(position).toString());
                    MainActivity.this.ItemAdapter.notifyDataSetChanged();
                }

            }
        });
        builder.create().show();
    }

    private void error() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío/s.");
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
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
        for (int i = 0; i < entrenamientos.size(); i++) {
            totalKm = totalKm + entrenamientos.get(i).getDistanciaKilometros();
            totalMinKm = totalMinKm + entrenamientos.get(i).calcularMinutosKilometro();
        }
        totalMinKm = totalMinKm / entrenamientos.size();
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stats");
        builder.setMessage("Km totales: " + formato.format(totalKm) + "\nMedia de: " + formato.format(totalMinKm) + " Min/Km.");
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
    ArrayList<String> item;
    ArrayAdapter<String> ItemAdapter;
    ArrayList<Entrenamiento> entrenamientos;
}
