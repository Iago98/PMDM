package com.example.pfc.ui;

import android.Manifest;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfc.R;
import com.example.pfc.core.ItemAdapter;
import com.example.pfc.core.MyLocation;
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

    public  void lanzarMenus(String login,Context context){

        Intent subActividad = new Intent(context, ShowMenus.class);
        subActividad.putExtra("log", login);
        subActividad.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(subActividad);

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


}