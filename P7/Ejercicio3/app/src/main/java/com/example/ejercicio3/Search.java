package com.example.ejercicio3;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class Search extends AppCompatActivity {
    WebView wvView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent datosEnviados = this.getIntent();
        final String url = datosEnviados.getExtras().getString("url");

        wvView = (WebView) this.findViewById( R.id.wvView );

        wvView.setWebViewClient(new WebViewClient());
        wvView.loadUrl(url);
        WebSettings wbSttngs= wvView.getSettings();
        wbSttngs.setJavaScriptEnabled(true);
        wbSttngs.setBuiltInZoomControls( true );
    }

    @Override
    public void onBackPressed() {
        if(wvView.canGoBack()){
            wvView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
