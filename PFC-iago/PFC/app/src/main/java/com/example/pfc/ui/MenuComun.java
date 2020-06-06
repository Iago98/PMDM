package com.example.pfc.ui;

import android.Manifest;
import android.app.Activity;
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
import com.example.pfc.core.ArrayMenus;
import com.example.pfc.core.ItemAdapter;
import com.example.pfc.core.Menu;
import com.example.pfc.core.MyLocation;
import com.example.pfc.core.RecycleViewAdapter;
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
    int increment = 4;
    String login, contra;
    MyLocation myLocation = new MyLocation();
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 0;
    String line="";
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

        if (ContextCompat.checkSelfPermission(MenuComun.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(MenuComun.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MenuComun.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)||ActivityCompat.shouldShowRequestPermissionRationale(MenuComun.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MenuComun.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
                ActivityCompat.requestPermissions(MenuComun.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

        }

        ComunConect comunConect = new ComunConect();
        comunConect.execute(login,contra,latitude,longitude);
        //recuperarRestaurantes();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    myLocation.getLocation(getApplicationContext(), locationResult);

                    boolean r = myLocation.getLocation(getApplicationContext(),
                            locationResult);
                    System.out.println("entro 1");
                  } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


            }

            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myLocation.getLocation(getApplicationContext(), locationResult);

                    boolean r = myLocation.getLocation(getApplicationContext(),
                            locationResult);


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("entro 2");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {
            // TODO Auto-generated method stub
            double Longitude = location.getLongitude();
            double Latitude = location.getLatitude();

            try {
                SharedPreferences.Editor edit = getSharedPreferences("les", Context.MODE_PRIVATE ).edit();
                edit.putString("Longitude", Longitude + "");
                edit.putString("Latitude", Latitude + "");
                edit.commit();
                System.out.println("SHARE PREFERENCE"+Latitude+" y "+Longitude);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

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

                ItemAdapter adapter = new ItemAdapter(MenuComun.this, restaurantesLista);

                lv.setAdapter(adapter);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}