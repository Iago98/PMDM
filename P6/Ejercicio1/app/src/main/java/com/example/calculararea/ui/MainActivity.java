package com.example.calculararea.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculararea.core.Connectivity;


public class MainActivity extends AppCompatActivity {
TextView wifiEna;
TextView typeConn;
TextView typeConn2;
Switch boton;
    Connectivity conn = new Connectivity(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        setContentView(R.layout.activity_main);
        boton =(Switch) this.findViewById(R.id.simpleSwitch);
        if(wifiManager.isWifiEnabled()){
            boton.setChecked(true);
        }else{
            boton.setChecked(false);
        }

        boton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if(boton.isChecked()){wifiManager.setWifiEnabled(true);
                }else{
                    wifiManager.setWifiEnabled(false);
                }



            }

        });


        conexion();



    }

    private void conexion(){

        boolean isCon=conn.isConnected();
        boolean isWif=conn.isWiFi();
        typeConn2 = (TextView) this.findViewById(R.id.wifiAbajo);
        typeConn = (TextView) this.findViewById(R.id.datos);
        wifiEna = (TextView) this.findViewById(R.id.wifiArriba);
        if (isCon) {
            if (isWif) {
                wifiEna.setBackgroundResource(R.drawable.wifi_1);
                typeConn.setBackgroundResource(R.drawable.wifi_1_abajo);
                typeConn2.setBackgroundResource(R.drawable.datos_0);
            }else if(!isWif){
                    typeConn.setBackgroundResource(R.drawable.wifi_0_abajo);
                    typeConn2.setBackgroundResource(R.drawable.datos_1);
            }else if(!isCon){
                wifiEna.setBackgroundResource(R.drawable.wifi_1);
                typeConn.setBackgroundResource(R.drawable.wifi_0_abajo);
                typeConn2.setBackgroundResource(R.drawable.datos_0);

            }
        }

    }




}
