package com.example.pfc.core;


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pfc.ui.ShowMenus;
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

public class RequestMenuComun extends AsyncTask<String, Void, String> {
    private static ArrayList<Menu> listaMenu = new ArrayList<>();
    private RecycleViewAdapter adapter;
    static ArrayList<Menu> menus= new ArrayList<>();
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
            System.out.println(menus.size()+"aqui va el tamaño del arraylist");
            listaMenu=menus;
            ArrayMenus.setListaMenus(listaMenu);
            Intent inte= new Intent(this,ShowMenus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

