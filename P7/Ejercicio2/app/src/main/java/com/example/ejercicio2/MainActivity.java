package com.example.ejercicio2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView wvView = (WebView) this.findViewById( R.id.wvView );
        this.configureWebView( wvView, "http://www.google.es", 10 );

    }

    @Override
    protected void onPause() {
        super.onPause();

        save();


    }

    @Override
    protected void onResume() {
        super.onResume();

        int size;

        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        size = prefs.getInt("tamaño",0);
        ArrayList<Compra> lista= new ArrayList<>();


        for(int z = 0; z<size; z++){
            String comp = prefs.getString("cantidad","");
            String cant = prefs.getString("compra", "");
            Compra obj = new Compra();
            obj.setCompra(comp);
            obj.setCant(cant);
            lista.add(obj);


        }
        ListaCompra.setListaCompra(lista);
    }


    private void save(){
        ArrayList<Compra> compra= new ArrayList<>();
        compra=WebAppInterface.guardar("","");
        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();
        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        Integer tamaño=0;
        for(int x=0;x<compra.size();x++) {
            System.out.println(compra.get(x).getCant().toString());
            System.out.println(compra.get(x).getCompra().toString());
            tamaño = prefs.getInt("tamaño", 0);
            edit.putInt("tamaño", tamaño+1);
            edit.putString("cantidad", compra.get(x).getCant());
            edit.putString("compra", compra.get(x).getCompra());
            edit.commit();
        }
    }

    private void configureWebView(WebView wvView, String url, int defaultFontSize)
    {
        WebSettings webSettings = wvView.getSettings();

        webSettings.setBuiltInZoomControls( true );
        webSettings.setDefaultFontSize( defaultFontSize );

        // Enable javascript and make android code available from it as the Android object.
        webSettings.setJavaScriptEnabled( true );
        wvView.addJavascriptInterface( new WebAppInterface( this ), "Android" );

        // URLs are handled by this WebView,instead of launching a browser
        wvView.setWebViewClient( new WebViewClient() );

        // Load from a URL - remember to give the app the internet permission
        // wvView.loadUrl( url );

        // Load a HTML file from the assets subdir
        StringBuilder builder = new StringBuilder();
        InputStream in = null;
        try {
            String line;

            in = this.getAssets().open( "tienda.html" );
            BufferedReader inf = new BufferedReader( new InputStreamReader( in ) );

            while( ( line = inf.readLine()) != null ) {
                builder.append( line );
            }
        } catch (IOException e) {
            builder.append( "<html><body><big>ERROR internal: loading asset</big></body></html>");
            Log.e( "main.configureWebView", "error loading asset 'calc.html'" );
        }
        finally {
            try {
                if ( in != null ) {
                    in.close();
                }
            } catch(IOException exc) {
                // ignored
            }
        }

        wvView.loadData( builder.toString(), "text/html", "utf-8" );
    }
}
