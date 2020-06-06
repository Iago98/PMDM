package com.example.pfc.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent subActividad = new Intent(MainActivity.this, Login.class);
        MainActivity.this.startActivity(subActividad);
    }


}
