package com.example.ejercicio2;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WebAppInterface extends AppCompatActivity {
    Context context;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        this.context = c;
    }

    /** Show a toast from the web page - muestra un mensaje desde la interfaz web
     */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText( context, toast, Toast.LENGTH_LONG ).show();
    }
    public void guardar(String cant,String compra) {
        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();
        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        String tamaño="";

        tamaño = prefs.getString("tamaño","0");
        edit.putString("tamaño",tamaño+1);
        edit.putString("cantidad",cant);
        edit.putString("compra",compra);
        edit.commit();
    }
}
