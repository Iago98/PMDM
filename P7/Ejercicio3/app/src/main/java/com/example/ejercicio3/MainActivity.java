package com.example.ejercicio3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    Button btAdd;
    EditText editText;
    String send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         btAdd = (Button) this.findViewById(R.id.button);
         editText=(EditText) this.findViewById(R.id.editText);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send="https://www."+editText.getText().toString();
                Intent subActividad = new Intent(MainActivity.this, Search.class);
                subActividad.putExtra("url", send);
                MainActivity.this.startActivityForResult(subActividad, 100);
            }
        });

    }
}
