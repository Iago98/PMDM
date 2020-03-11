package com.example.ejercicio2;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
static ArrayList<Compra> lista = new ArrayList<>();


    @JavascriptInterface
    public static ArrayList<Compra> guardar(String cant,String compra) {
        if(cant.equals("")||cant.equals("")){

        }else {

            Compra com = new Compra();
            com.setCant(cant);
            com.setCompra(compra);
            lista.add(com);
        }
        return lista;
    }

    @JavascriptInterface
    public static String[] compra(){
        ArrayList<Compra> list=ListaCompra.getListaCompra();
        String[] array= null;
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        for (int x=0;x<list.size();x++){
            array[x]=list.get(x).getCompra();
            System.out.println("me estan llamando a compr devuelvo: "+array[x].toString());
        }

        return array;
    }

    @JavascriptInterface
    public static String[] cant(){
        ArrayList<Compra> list=ListaCompra.getListaCompra();
        String[] array= null;
        for (int x=0;x<list.size();x++){
            array[x]=list.get(x).getCant();
            System.out.println("me estan llamando a cant devuelvo"+array[x].toString());
        }

        return array;
    }
}
