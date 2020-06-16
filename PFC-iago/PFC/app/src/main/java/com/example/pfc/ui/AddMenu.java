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
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfc.R;
import com.example.pfc.core.RecycleViewAdapter;
import com.example.pfc.core.UTILES;

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


public class AddMenu extends AppCompatActivity {
    private int color;
    int id = -1;
    String line = "";
    String titulo;
    String login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_menu);

         SharedPreferences prefs = getSharedPreferences("mispref", Context.MODE_PRIVATE);


        final Button btAceptar = (Button) this.findViewById(R.id.aceptar);
        final Button btCancelar = (Button) this.findViewById(R.id.cancelar);
        final EditText edTitulo = (EditText) this.findViewById(R.id.titulo);
        final EditText edDescripcion = (EditText) this.findViewById(R.id.descripcion);

        final Intent transitionIntent = this.getIntent();
        login = transitionIntent.getExtras().getString("login", "");
        System.out.println("aqiooooooooooooooooooooooooooooo"+login);
        final int pos = transitionIntent.getExtras().getInt("pos");
        if (pos == -100) {
            login = transitionIntent.getExtras().getString("login", "");
            id = transitionIntent.getExtras().getInt("id", 0);
             titulo = transitionIntent.getExtras().getString("titulo", "ERROR");
            final String desc = transitionIntent.getExtras().getString("desc", "ERROR");
            edTitulo.setText(titulo);
            edDescripcion.setText(desc);
        }
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMenu.this.setResult(Activity.RESULT_CANCELED);
                AddMenu.this.finish();
            }
        });


        btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClass add = new AddClass();
                if(!edTitulo.getText().toString().trim().isEmpty()&&!edDescripcion.getText().toString().trim().isEmpty()){

                    if (id == -1) {
                        //si el menu es nuevo
                        titulo = edTitulo.getText().toString();
                        String descrip = edDescripcion.getText().toString();
                        add.execute(titulo, descrip,login,"-1");
                        AddMenu.this.finish();

                    }else {
                        login = prefs.getString("login", "");
                        String titulo = edTitulo.getText().toString();
                        String descrip = edDescripcion.getText().toString();
                        add.execute(titulo, descrip, login, String.valueOf(AddMenu.this.id));
                        //aqui modifica
                        AddMenu.this.finish();

                    }
                }else{
                    error();
                }

            }
        });


    }
    private void error() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío/s.");
        builder.setPositiveButton("Reintentar", null);
        builder.create().show();
    }
    class AddClass extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            InputStream is = null;
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("titulo", strings[0]));
                nameValuePairs.add(new BasicNameValuePair("descripcion", strings[1]));
                nameValuePairs.add(new BasicNameValuePair("login", strings[2]));
                nameValuePairs.add(new BasicNameValuePair("id", strings[3]));
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" + UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/addmenu");
                System.out.println("http://" +UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/addmenu");
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
                final Intent retData = new Intent();
                retData.putExtra("name", titulo);
                retData.putExtra("color", color);
                AddMenu.this.setResult(Activity.RESULT_OK, retData);
                AddMenu.this.finish();
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Error";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

    }
}
