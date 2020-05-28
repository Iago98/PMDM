package com.example.pfc.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfc.R;
import com.example.pfc.core.UTILES;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextInputEditText edLogin,edContra;
    String line = "";
    String string="";
    String[] parts = null;
    String isLogin ="";
    String typeLogin="";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button btLogin = (Button) this.findViewById(R.id.button) ;
       final Button btRegistro = (Button) this.findViewById(R.id.button2) ;
        edLogin= (TextInputEditText) this.findViewById(R.id.textInputLayout21) ;
        edContra= (TextInputEditText) this.findViewById(R.id.textInputLayout31) ;
       btLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            if(edLogin.getText().toString().isEmpty()||edContra.getText().toString().isEmpty()){
                error();
            }else{
                Login log = new Login();
                log.execute(edLogin.getText().toString(),edContra.getText().toString());
            }

           }
       });

        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent subActividad = new Intent(MainActivity.this, Registro.class);
                subActividad.putExtra("log", edLogin.getText().toString());
                subActividad.putExtra("contra", edContra.getText().toString());
                MainActivity.this.startActivity(subActividad);


            }
        });

    }
    private void error(){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío/s.");
        builder.setPositiveButton("Reintentar", null);
        builder.create().show();
    }
    class Login extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... strings) {
            InputStream is = null;
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("login", strings[0]));
                nameValuePairs.add(new BasicNameValuePair("contrasenha", strings[1]));
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" +UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/login");
                System.out.println("http://" +UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/login");
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
            }

            //leer respuesta del servlet
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                line= reader.readLine();
                string=line;
                parts = string.split("-");
                System.out.println(line);
                System.out.println("Esto es " + line);
                isLogin = parts[0];
                typeLogin = parts[1];
                if (isLogin.equalsIgnoreCase("true")){
                    System.out.println("Entro en true");
                    return true;
                } else if (isLogin.equalsIgnoreCase("false")){
                    System.out.println("Entro en false");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result&&typeLogin.equals("cliente")) {
                Context context = getApplicationContext();
                CharSequence text = "true y cliente";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else if(result&&typeLogin.equals("restaurante")){
                Context context = getApplicationContext();
                CharSequence text = "true y restaurante";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Error al iniciar sesión");
                builder.setMessage("El nombre de usuario o contraseña no son correctos.");
                builder.setPositiveButton("Aceptar", null);
                builder.create().show();
            }
        }

    }
}
