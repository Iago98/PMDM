package com.example.pfc.ui;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfc.R;

import com.example.pfc.core.UTILES;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Login extends AppCompatActivity {

    private CheckBox checkB;
    private static final int REQUEST_CODE_LOCATION_PERMISSION =1;
    private TextInputEditText edLogin, edContra;
    private String line = "";
    private String string = "";
    private String[] parts = null;
    private  String isLogin = "";
    private String typeLogin = "";
    private ImageView img;
    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences prefs = getSharedPreferences("mispref", Context.MODE_PRIVATE);
        String login = prefs.getString("login", "");
        String contra = prefs.getString("contra", "");
        String tipo = prefs.getString("tipo", "");
        System.out.println(tipo.toString() + "aqui el tipo");
        System.out.println(contra.toString() + "aqui el contra");
        System.out.println(login.toString() + "aqui el login");
        if (!login.equals("") && !contra.equals("") && tipo.equals("restaurante")) {
            System.out.println("paso por intent de inicio automatico de restaurante");
            Intent subActividad = new Intent(Login.this, MenuRestaurante.class);
            subActividad.putExtra("log", login);
            subActividad.putExtra("contra", contra);

            Login.this.startActivity(subActividad);
        } else if (!login.equals("") && !contra.equals("") && tipo.equals("comun")) {
            System.out.println("paso por intent de inicio automatico de comun");
            Intent subActividad = new Intent(Login.this, MenuComun.class);
            subActividad.putExtra("log", login);
            subActividad.putExtra("contra", contra);
            Login.this.startActivity(subActividad);
        }
        setContentView(R.layout.login);

        checkB = (CheckBox) findViewById(R.id.checkBox);
        img = (ImageView) findViewById(R.id.imageView2);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Button btLogin = (Button) this.findViewById(R.id.button);
        final Button btRegistro = (Button) this.findViewById(R.id.button2);
        edLogin = (TextInputEditText) this.findViewById(R.id.textInputLayout21);
        edContra = (TextInputEditText) this.findViewById(R.id.textInputLayout31);
        requestPermission();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Login.this,new String[]{ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);
        }else{
            getCurrentLocation();
        }
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edLogin.getText().toString().isEmpty() || edContra.getText().toString().isEmpty()) {
                    error();
                } else {

                    Login.Login2 log = new Login.Login2();
                    log.execute(edLogin.getText().toString(), edContra.getText().toString());
                }

            }
        });

        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent subActividad = new Intent(Login.this, Registro.class);
                subActividad.putExtra("log", edLogin.getText().toString());
                subActividad.putExtra("contra", edContra.getText().toString());
                Login.this.startActivity(subActividad);


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(Login.this).requestLocationUpdates(locationRequest, new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(Login.this)
                        .removeLocationUpdates(this);
                if(locationResult!=null &&locationResult.getLocations().size()>0){
                    int latestLocationIndex=locationResult.getLocations().size() -1;
                    double latitude=locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude=locationResult.getLocations().get(latestLocationIndex).getLongitude();

                    SharedPreferences.Editor edit = getSharedPreferences("les", Context.MODE_PRIVATE ).edit();
                    edit.putString("Longitude", longitude + "");
                    edit.putString("Latitude", latitude + "");
                    edit.commit();
                    System.out.println("SHARE PREFERENCE"+latitude+" y "+longitude);


                }

            }
        }, Looper.getMainLooper());
}

private void requestPermission(){

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
}

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Presiona de nuevo para salir", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void error() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Campo vacío/s.");
        builder.setPositiveButton("Reintentar", null);
        builder.create().show();
    }



    class Login2 extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            InputStream is = null;
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("login", strings[0]));
                nameValuePairs.add(new BasicNameValuePair("contrasenha", strings[1]));
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" + UTILES.IP_PREDEFINIDO + ":" + UTILES.PUERTO_OUT + "/connection/login");
                System.out.println("http://" + UTILES.IP_PREDEFINIDO + ":" + UTILES.PUERTO_OUT + "/connection/login");
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //leer respuesta del servlet
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                line = reader.readLine();
                string = line;
                parts = string.split("-");
                System.out.println(line);
                System.out.println("Esto es " + line);
                isLogin = parts[0];
                typeLogin = parts[1];
                if (isLogin.equalsIgnoreCase("true")) {
                    System.out.println("Entro en true");
                    return true;
                } else if (isLogin.equalsIgnoreCase("false")) {
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
            if (result && typeLogin.equals("cliente")) {
//                Context context = getApplicationContext();
//                CharSequence text = "true y cliente";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
                SharedPreferences.Editor edit = getSharedPreferences("mispref", Context.MODE_PRIVATE).edit();
                edit.putString("login", edLogin.getText().toString());
                if (checkB.isChecked()) {
                    edit.putString("contra", edContra.getText().toString());
                    edit.putString("tipo", "comun");
                    edit.commit();
                } else {
                    edit.putString("contra", "");
                    edit.putString("tipo", "");
                    edit.commit();
                }
                Intent subActividad = new Intent(Login.this, MenuComun.class);
                subActividad.putExtra("log", edLogin.getText().toString());
                subActividad.putExtra("contra", edContra.getText().toString());
                Login.this.startActivity(subActividad);
            } else if (result && typeLogin.equals("restaurante")) {
                SharedPreferences.Editor edit = getSharedPreferences("mispref", Context.MODE_PRIVATE).edit();
                edit.putString("login", edLogin.getText().toString());
                if (checkB.isChecked()) {

                    edit.putString("contra", edContra.getText().toString());
                    edit.putString("tipo", "restaurante");

                    edit.commit();
                } else {
                    edit.putString("contra", "");
                    edit.putString("tipo", "");
                    edit.commit();
                }
//                Context context = getApplicationContext();
//                CharSequence text = "true y restaurante";
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
                Intent subActividad = new Intent(Login.this, MenuRestaurante.class);
                subActividad.putExtra("log", edLogin.getText().toString());
                subActividad.putExtra("contra", edContra.getText().toString());

                Login.this.startActivity(subActividad);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Error al iniciar sesión");
                builder.setMessage("El nombre de usuario o contraseña no son correctos.");
                builder.setPositiveButton("Aceptar", null);
                builder.create().show();
            }
        }
    }
}
