package com.resuadam.listacompra.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.resuadam.listacompra.R;


public class Actividad2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.layout_actividad2 );

        final Button btGuardar = (Button) this.findViewById( R.id.btGuardar );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar );
        final EditText edCantidad = (EditText) this.findViewById( R.id.edCantidad );
        final EditText edNombre = (EditText) this.findViewById( R.id.edNombre );

        Intent datosEnviados = this.getIntent();

        edNombre.setText( datosEnviados.getExtras().getString(( "nombre" ) ) );
        edCantidad.setText( Integer.toString( datosEnviados.getExtras().getInt(( "cantidad" ) ) ) );

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actividad2.this.setResult( Activity.RESULT_CANCELED );
                Actividad2.this.finish();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent datosRetornar = new Intent();

                datosRetornar.putExtra( "nombre", edNombre.getText().toString() );
                datosRetornar.putExtra( "cantidad", Integer.parseInt( edCantidad.getText().toString() ) );
                datosRetornar.putExtra( "pos", Actividad2.this.getIntent().getExtras().getInt( "pos" ) );
                Actividad2.this.setResult( Activity.RESULT_OK, datosRetornar );
                Actividad2.this.finish();
            }
        });
        btGuardar.setEnabled( false );

        edNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardar.setEnabled( edNombre.getText().toString().trim().length() > 0 );
            }
        });

        edCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int cantidad = 0;

                try {
                    cantidad = Integer.parseInt( edCantidad.getText().toString() );
                } catch(NumberFormatException exc) {
                    Log.w( "ItemEditionActivity", "edCantidad no puede ser convertido a número" );
                }

                btGuardar.setEnabled( cantidad > 0 );
            }
        });
    }
}