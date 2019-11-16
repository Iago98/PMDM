package com.example.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends Activity {
    /**
     * @Override public void onResume(){
     * super.onResume();
     * Str
     * <p>
     * compararFechas();
     * <p>
     * <p>
     * <p>
     * }
     */
    @Override
    public void onResume() {
        super.onResume();


        compararFechas();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.items = new ArrayList<String>();

        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);


        lvItems.setLongClickable(true);


        this.itemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
        lvItems.setAdapter(this.itemsAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.items.set(position, et.getText().toString());
                            MainActivity.this.itemsAdapter.notifyDataSetChanged();
                        }

                    });
                    builder.create().show();
                }

            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                if (pos >= 0) {
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
                return true;
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();
            }
        });
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
                seleccionarFecha(text);


            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private Calendar recuperarFechaActual() {

        Calendar fecha = new GregorianCalendar(Locale.getDefault());
        return fecha;
    }

    private void compararFechas() {
        int valor = 5;
        final TextView tv= new TextView(this);
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
                    String texto = MainActivity.this.items.get(x).toString();
                    System.out.println("El teeexto que quiero agregar es: "+texto);
                    tv.setText(texto);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Atención");
                    builder.setMessage("La siguiente actividad está caducada.¿Que deseas hacer?");
                    builder.setView(tv);

                    builder.create().show();
                }

            } catch (ParseException e) {
                Log.v("printf", "ERROR!: can not parsing! ");
            }

        }


    }

    int year;
    int month;
    int day;

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
                2018, 0, 1
        );

        dlg.show();


    }

    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    private ArrayList<String> fechas = new ArrayList<String>();

}
