package com.example.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        this.items = new ArrayList<String>();

        Button btAdd = (Button) this.findViewById( R.id.btAdd );
        ListView lvItems = (ListView) this.findViewById( R.id.lvItems );


        lvItems.setLongClickable( true );


        this.itemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
       lvItems.setAdapter( this.itemsAdapter );

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText et = new EditText(MainActivity.this);
                final TextView tv = new TextView(MainActivity.this);

                String texto = MainActivity.this.items.get(position).toString();
                String fecha = MainActivity.this.fechas.get(position).toString();

                et.setText(texto);
                if (position>=0){
                    AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Modificar");
                    builder.setMessage("Texto a modificar");
                    builder.setView(tv);
                    builder.setView(et);
                    builder.setNegativeButton("Cancelar",null);
                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.items.set(position,et.getText().toString());
                        }

                    });  builder.create().show();
                }

            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                if ( pos >= 0 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Cuidado");
                    builder.setMessage("¿Seguro que quieres eliminar la actividad?");
                    builder.setPositiveButton("Sí, hazlo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.items.remove( pos );
                            MainActivity.this.fechas.remove( pos );
                            MainActivity.this.itemsAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("No, cancela",null);
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
        final EditText edText = new EditText( this );

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("To Do...");
        builder.setMessage( "¿Que deseas añadir para recordar?" );
        builder.setView( edText );
        builder.setPositiveButton( "Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String text = edText.getText().toString();
                seleccionarFecha();

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private String recuperarFecha() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);
        return fecha;
    }
    int year;
    int month;
    int day;
    private void seleccionarFecha(){
        final DatePickerDialog dlg = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                MainActivity.this.year=year;
                MainActivity.this.month = month;
                MainActivity.this.day = dayOfMonth;
                Date fecha= new Date( year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(fecha);
                System.out.println(strDate);


            }

        },
                2018,1,2
        );

        dlg.show();

    }

    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    private ArrayList<String> fechas;

}
