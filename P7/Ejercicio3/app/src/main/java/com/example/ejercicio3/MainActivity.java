package com.example.ejercicio3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    Button btAdd;
    Button back;
    String texto;
    String texxt;
    EditText editText;
    WebView wvView;
    private GestureDetector mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back = (Button) this.findViewById(R.id.back);
         btAdd = (Button) this.findViewById(R.id.button);
        editText=(EditText) this.findViewById(R.id.editText);
        wvView = (WebView) this.findViewById( R.id.wvView );






        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wvView.goBack();

                                    }
                                });
        btAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(editText.getText().toString());
                        texto=editText.getText().toString();
                        texxt=editText.getText().toString();
                        String part1="";
                        String texto2="";
                        String texto3="";

                        String url="";

                        try {
                            String[] parts = texto.split("/");
                             part1 = parts[0];



                        }catch (Exception e){

                        }
                        try {
                            String[] parts2 = texxt.split("\\.");
                            texto2 = parts2[0];
                            texto3= parts2[2];



                        }catch (Exception e){

                        }


                        if(part1.equalsIgnoreCase("https:")||part1.equalsIgnoreCase("http:")) {
                            url = editText.getText().toString();
                        }else if(texto2.equalsIgnoreCase("www")){
                            if(!texto3.equalsIgnoreCase("es")&&!texto3.equalsIgnoreCase("com")){
                                url="https://www.google.com/search?q="+editText.getText().toString();
                            }

                            url="https://"+editText.getText().toString();

                            } else{
                            url="https://www.google.com/search?q="+editText.getText().toString();
                        }

                        wvView.setWebViewClient(new WebViewClient());
                        wvView.loadUrl(url);
                        WebSettings wbSttngs = wvView.getSettings();
                        wvView.requestFocus();
                        wbSttngs.setJavaScriptEnabled(true);

                        wbSttngs.setDisplayZoomControls(true);
                    }
                });


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
