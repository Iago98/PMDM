package com.example.pfc.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfc.R;
import com.example.pfc.core.ItemAdapter;
import com.example.pfc.core.RegistroRestaurante;

import com.example.pfc.core.UTILES;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class MenuComun extends AppCompatActivity {

    String login, contra;
    private Context context;

    ArrayList<RegistroRestaurante> restaurantesLista = new ArrayList<>();
    ArrayList<RegistroRestaurante> restaurantesEntrantes = new ArrayList<>();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comun);
        SharedPreferences prefs = getSharedPreferences("les", Context.MODE_PRIVATE );
        String longitude= prefs.getString("Longitude","");
        String latitude= prefs.getString("Latitude","");
        Bundle extras = getIntent().getExtras();
        login = extras.getString("log");
        contra = extras.getString("contra");
        context = MenuComun.this.getApplicationContext();



        ComunConect comunConect = new ComunConect();
        comunConect.execute(login,contra,latitude,longitude);
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        this.getMenuInflater().inflate(R.menu.actions_menu, menu);
        return true;
    }
    public  void lanzarMenus(String login,Context context){

        Intent subActividad = new Intent(context, ShowMenus.class);
        subActividad.putExtra("log", login);
        subActividad.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(subActividad);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean toret = false;

        switch (menuItem.getItemId()) {
            case R.id.opCerrarSesion:
                SharedPreferences.Editor edit = getSharedPreferences("mispref", Context.MODE_PRIVATE).edit();
                edit.putString("login", "");
                edit.putString("contra", "");
                edit.commit();
                finish();
            break;
            case R.id.opEliminarCuenta:

                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cuidado");
                builder.setMessage("¿Está seguro de que desea eliminar la cuenta?");
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor edita = getSharedPreferences("mispref", Context.MODE_PRIVATE).edit();
                        edita.putString("login", "");
                        edita.putString("contra", "");
                        edita.commit();
                        EliminarCuenta eliminarCuenta = new EliminarCuenta();
                        eliminarCuenta.execute(login);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar",null);
                builder.create().show();


        }
        return toret;
    }
    class ComunConect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            InputStream is = null;
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("login", strings[0]));
                nameValuePairs.add(new BasicNameValuePair("contra", strings[1]));
                nameValuePairs.add(new BasicNameValuePair("latitude", strings[2]));
                nameValuePairs.add(new BasicNameValuePair("longitude", strings[3]));
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" + UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/comunMenu");
                System.out.println("http://" +UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/comunMenu");
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
            }

            //leer respuesta del servlet
            String line;
            String result="";
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);


                while((line=reader.readLine())!=null){
                    result=result+line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TypeReference<ArrayList<RegistroRestaurante>> ref = new TypeReference<ArrayList<RegistroRestaurante>>() {};
            try {

                restaurantesEntrantes= new ObjectMapper().readValue(result, ref);
                System.out.println(restaurantesEntrantes.size()+"aqui va el tamaño del arraylist de restaurantes");
                restaurantesLista=restaurantesEntrantes;
                ListView lv = (ListView)findViewById(R.id.listView);

                ItemAdapter adapter = new ItemAdapter(MenuComun.this, restaurantesLista,context);

                lv.setAdapter(adapter);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    class EliminarCuenta extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            InputStream is = null;
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("login", strings[0]));
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" + UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/eliminaComun");
                System.out.println("http://" +UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/eliminaComun");
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
                String  line = reader.readLine();

                System.out.println(line);


                if (line.equalsIgnoreCase("true")) {
                    System.out.println("Entro en true");
                    return true;
                } else if (line.equalsIgnoreCase("false")) {
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
            if(result){
                Context context = getApplicationContext();
                CharSequence text = "Usuario Eliminado";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else {
                Context context = getApplicationContext();
                CharSequence text = "Error";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

    }


}