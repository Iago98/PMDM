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



    @JavascriptInterface
    public static void guardar(String cant,String compra) {
         ArrayList<Compra> lista = ListaCompra.getListaCompra();
        for (int x=0;x<lista.size();x++){
           System.out.println("aqui esttttttttttttoooooooooooooooy"+lista.get(x).getCompra().toString());
        }
        if(cant.equals("")||cant.equals("")){

        }else {
            System.out.println("guardando"+cant+compra);
            Compra com = new Compra();
            com.setCant(cant);
            com.setCompra(compra);
            lista.add(com);
        }
      ListaCompra.setListaCompra(lista);
        System.out.println("tamaño de lista guardando"+lista.size());

    }

    @JavascriptInterface
    public static String compra(){
        ArrayList<Compra> list=ListaCompra.getListaCompra();
        String str= "";

        System.out.println("--------------------------------------------------------------------------------------------------------------");

        for (int x=0;x<list.size();x++){
            str+=list.get(x).getCompra()+"-";
            System.out.println("me estan llamando a compr devuelvo: "+str.toString());
        }

        return str;
    }
    @JavascriptInterface
    public static int num(){
        ArrayList<Compra> list=ListaCompra.getListaCompra();
        int num= list.size();
        System.out.println("el numero es: "+num);
        return num;
    }

    @JavascriptInterface
    public static void eliminarById(int id){
        ArrayList<Compra> list=ListaCompra.getListaCompra();
        System.out.println("elimino esto: "+list.get(id).toString());
        list.remove(id);
    }
    @JavascriptInterface
    public static String cant(){
        ArrayList<Compra> list=ListaCompra.getListaCompra();
        String str= "";
        String s="-";
        for (int x=0;x<list.size();x++){
            str+=list.get(x).getCant()+"-";
            System.out.println("me estan llamando a cant devuelvo"+str.toString());
        }

        return str;
    }
}
