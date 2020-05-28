package com.example.pfc.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfc.R;
import com.example.pfc.core.UTILES;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

public class Registro extends AppCompatActivity {
    TextInputEditText edLogin, edContra, edNombre;
    TextView intro;
    Switch sw;
    Button btRegistro,btUbi;
    Boolean estado= false;
    String line = "";
    String string="";
    String[] parts = null;
    String isLogin ="";
    String typeLogin="";
    String latLong="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_layout);
        sw = (Switch) findViewById(R.id.switch2);

        btRegistro = (Button) this.findViewById(R.id.button4);
        btUbi = (Button) this.findViewById(R.id.btUbi);
        edLogin = (TextInputEditText) this.findViewById(R.id.textInputLayoutLoggin);
        edContra = (TextInputEditText) this.findViewById(R.id.textInputLayoutContra);
        edNombre = (TextInputEditText) this.findViewById(R.id.textInputLayoutNombre);
        SharedPreferences prefs = getSharedPreferences("mispref",Context.MODE_PRIVATE );

        intro = (TextView) this.findViewById(R.id.textViewIntro);

        btUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, MapsActivity.class);
                Registro.this.startActivity(intent);
                System.out.println("hola?");
            }
        });

        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            if(!edLogin.getText().toString().equals("")||!edContra.getText().toString().equals("")||!edNombre.getText().toString().equals("")){

                if(estado){
                    Regs log = new Regs();
                    latLong= prefs.getString("lat/lng","");
                    log.execute("Restaurante",edLogin.getText().toString(),edContra.getText().toString(),edNombre.getText().toString(),latLong);
                }else{
                    Regs log = new Regs();
                    log.execute("Comun",edLogin.getText().toString(),edContra.getText().toString(),edNombre.getText().toString(),"false");
                }

            }else{
                error();
            }


            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    intro.setText(R.string.registrarseRest);
                    estado=false;
                    btUbi.setVisibility(View.VISIBLE);
                } else {
                    intro.setText(R.string.registrarseCom);
                    estado=true;
                    btUbi.setVisibility(View.GONE);
                }
            }
        });


    }
    class Regs extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            InputStream is = null;
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("tipo", strings[0]));
                nameValuePairs.add(new BasicNameValuePair("login", strings[1]));
                nameValuePairs.add(new BasicNameValuePair("nombre", strings[2]));
                nameValuePairs.add(new BasicNameValuePair("contra", strings[3]));
                nameValuePairs.add(new BasicNameValuePair("ubicacion", strings[4]));
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" + UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/registro");
                System.out.println("http://" +UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/registro");
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
                System.out.println(line);
                System.out.println("Esto es " + line);
                if (line.equalsIgnoreCase("true")){
                    System.out.println("Entro en true");
                    return true;
                } else if (line.equalsIgnoreCase("false")){
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
            if (result) {
                Context context = getApplicationContext();
                CharSequence text = "Registrado con Exito";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                builder.setTitle("Error al registrar");
                builder.setMessage("El nombre de usuario"+edLogin.getText().toString() +"ya existe");
                builder.setPositiveButton("Reintentar", null);
                edLogin.setText("");
                builder.create().show();
            }
        }

    }
    private void error(){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo/s vacío/s.");
        builder.setPositiveButton("Reintentar", null);
        builder.create().show();
    }

}
