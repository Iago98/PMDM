package com.example.pfc.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfc.R;
import com.example.pfc.core.Menu;
import com.example.pfc.core.UTILES;
import com.example.pfc.core.menuAdapter;
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

public class ShowMenus extends AppCompatActivity {
    String login;
    private ArrayList<Menu> listaMenu = new ArrayList<>();
    private ArrayList<Menu> menus = new ArrayList<>();


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comun);
        Bundle extras = getIntent().getExtras();
        login = extras.getString("log");

        Request req = new Request();
        req.execute(login," ");

    }
    class Request extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            InputStream is = null;
            try{
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("login", strings[0]));
                nameValuePairs.add(new BasicNameValuePair("contra", strings[1]));
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" + UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/requestmenu");
                System.out.println("http://" +UTILES.IP_PREDEFINIDO+":"+UTILES.PUERTO_OUT +"/connection/requestmenu");
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
            TypeReference<ArrayList<Menu>> ref = new TypeReference<ArrayList<Menu>>() {};
            try {

                menus= new ObjectMapper().readValue(result, ref);
                if(menus.size()==0){
                    Context context = getApplicationContext();
                    CharSequence text = "Este restaurante no tiene menús";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    finish();
                }
                System.out.println(menus.size()+"aqui va el tamaño del arraylist");
                listaMenu=menus;
                ListView lv = (ListView)findViewById(R.id.listView);
                menuAdapter adapter = new menuAdapter(ShowMenus.this, listaMenu);
                lv.setAdapter(adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
