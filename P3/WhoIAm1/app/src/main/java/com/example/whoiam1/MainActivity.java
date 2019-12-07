package com.example.whoiam1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatosPersonalesExtendidos datosPersnlsExt = new DatosPersonalesExtendidos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edNombre = (EditText) this.findViewById(R.id.edNombre);
        final EditText edEmail = (EditText) this.findViewById(R.id.edEmail);
        final EditText edDireccion = (EditText) this.findViewById(R.id.edDireccion);
        final EditText edEdad = (EditText) this.findViewById(R.id.edEdad);
        final EditText edApellido = (EditText) this.findViewById(R.id.edApellido);

        edNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersnlsExt.setNombre(edNombre.getText().toString());
            }
        });
        edEdad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersnlsExt.setEdad(edEdad.getText().toString());
            }
        });

        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersnlsExt.setEmail(edEmail.getText().toString());
            }
        });
        edDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersnlsExt.setDireccion(edDireccion.getText().toString());
            }
        });

        edApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersnlsExt.setApellido1(edApellido.getText().toString());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        final EditText edNombre = (EditText) this.findViewById( R.id.edNombre );
        final EditText edEmail = (EditText) this.findViewById( R.id.edEmail );
        final EditText edDireccion = (EditText) this.findViewById( R.id.edDireccion );
        final EditText edApellido = (EditText) this.findViewById( R.id.edApellido );

        final EditText edad = (EditText) this.findViewById( R.id.edEdad );

        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );

        edNombre.setText(prefs.getString("nombre",""));
        edApellido.setText(prefs.getString("apellido",""));
        edDireccion.setText(prefs.getString("direccion",""));
        edEmail.setText(prefs.getString("email",""));
        edad.setText(prefs.getString("eedad",""));





    }
    @Override
    public void onPause()
    {
        super.onPause();

        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();
        edit.putString("nombre", datosPersnlsExt.getNombre());
        edit.putString("apellido", datosPersnlsExt.getApellido1());
        edit.putString("eedad", datosPersnlsExt.getEdad());
        edit.putString("direccion",datosPersnlsExt.getDireccion());
        edit.putString("email", datosPersnlsExt.getEmail());
        edit.commit();


    }
}
