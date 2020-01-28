package com.example.entrenamiento.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.entrenamiento.core.ListaEntrenamiento;

public class EntrenamientoEditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento_activity);

        final ListaEntrenamiento app = (ListaEntrenamiento) this.getApplication();
        final Button btGuardar = (Button) this.findViewById(R.id.btGuardar);
        final Button btCancelar = (Button) this.findViewById(R.id.btCancelar);
        final EditText edTexto = (EditText) this.findViewById(R.id.edTexto);
        final EditText edKm = (EditText) this.findViewById(R.id.edDistancia);
        final EditText edTiempo = (EditText) this.findViewById(R.id.edTiempo);


        Intent datosEnviados = this.getIntent();

        final int pos = datosEnviados.getExtras().getInt("pos");
        String texto = "";
        String distancia = "";
        String tiempo = "";

        if (pos >= 0) {
            texto = app.getEntrenamientoList().get(pos).getNombreEntrenamiento();
            distancia = String.valueOf(app.getEntrenamientoList().get(pos).getDistanciaKilometros());
            tiempo = String.valueOf(app.getEntrenamientoList().get(pos).getTiempoMinutos());
        }

        edTexto.setText(texto);
        edKm.setText(distancia);
        edTiempo.setText(tiempo);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EntrenamientoEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                EntrenamientoEditionActivity.this.finish();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (edTexto.getText().toString().isEmpty() || edKm.getText().toString().isEmpty() || edTiempo.getText().toString().isEmpty()) {
                    error();
                } else {
                    final String texto = edTexto.getText().toString();
                    final float distancia = Float.valueOf(edKm.getText().toString());
                    final float tiempo = Float.valueOf(edTiempo.getText().toString());

                    if (pos >= 0) {

                        System.out.println("aqui la pos editionact"+pos);
                        app.modifyEntrenamiento(pos, texto, distancia, tiempo);

                    } else {

                        app.addEntrenamiento(texto, distancia, tiempo);

                    }
                    EntrenamientoEditionActivity.this.setResult(Activity.RESULT_OK);
                    EntrenamientoEditionActivity.this.finish();
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
        edKm.addTextChangedListener(new TextWatcher() {
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
        edTiempo.addTextChangedListener(new TextWatcher() {
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


    }

    private void error() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío/s.");
        builder.setPositiveButton("Reintentar", null);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EntrenamientoEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                EntrenamientoEditionActivity.this.finish();
            }
        });
        builder.create().show();
    }
}
