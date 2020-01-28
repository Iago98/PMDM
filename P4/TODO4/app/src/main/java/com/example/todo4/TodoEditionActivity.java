package com.example.todo4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TodoEditionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edition);

        final ListaTodo app = (ListaTodo) this.getApplication();
        final Button btGuardar = (Button) this.findViewById(R.id.btGuardar);
        final Button btCancelar = (Button) this.findViewById(R.id.btCancelar);
        final Button btFecha = (Button) this.findViewById(R.id.btFecha);
        final EditText edTexto = (EditText) this.findViewById(R.id.edTexto);


        Intent datosEnviados = this.getIntent();

        btFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendarOn = Calendar.getInstance(TimeZone.getDefault());
                int yearr = calendarOn.get(Calendar.YEAR);
                int monthh = calendarOn.get(Calendar.MONTH);
                int dayy = calendarOn.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dlg = new DatePickerDialog(TodoEditionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        TodoEditionActivity.this.year = year;
                        TodoEditionActivity.this.month = month;
                        TodoEditionActivity.this.day = dayOfMonth;
                        final Button btFecha = (Button) TodoEditionActivity.this.findViewById(R.id.btFecha);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar fechas = new GregorianCalendar(year, month, dayOfMonth);
                        String strDate = dateFormat.format(fechas.getTime());
                        btFecha.setText(strDate);


                    }

                },
                        yearr, monthh, dayy
                );

                dlg.show();

            }
        });


        final int pos = datosEnviados.getExtras().getInt("pos");
        String texto = "";
        String fecha = "";
        if (pos >= 0) {
            texto = app.getTodoList().get(pos).getTexto();
            fecha = app.getTodoList().get(pos).getFecha();
        }
        edTexto.setText(texto);
        btFecha.setText(fecha);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TodoEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                TodoEditionActivity.this.finish();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (edTexto.getText().toString().isEmpty() || btFecha.getText().toString().isEmpty()) {
                    error();
                } else {
                    final String texto = edTexto.getText().toString();
                    final String fecha = btFecha.getText().toString();
                    if (pos >= 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar fechas = new GregorianCalendar(year, month, day);
                        String strDate = dateFormat.format(fechas.getTime());
                        app.modifyTodo(pos, texto, strDate);

                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar fechas = new GregorianCalendar(year, month, day);
                        String strDate = dateFormat.format(fechas.getTime());
                        System.out.println("fecha" + strDate);
                        app.addTodo(strDate, texto);
                    }
                    TodoEditionActivity.this.setResult(Activity.RESULT_OK);
                    TodoEditionActivity.this.finish();
                }
            }
        });
        btGuardar.setEnabled(false);

        edTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardar.setEnabled(edTexto.getText().toString().trim().length() > 0);
            }
        });
        btFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardar.setEnabled(btFecha.getText().toString().trim().length() > 0);
            }
        });


    }


    private int year;
    private int month;
    private int day;


    private void error() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío/s.");
        builder.setPositiveButton("Reintentar", null);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TodoEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                TodoEditionActivity.this.finish();
            }
        });
        builder.create().show();
    }


}