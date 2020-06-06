package com.example.pfc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfc.R;
import com.example.pfc.core.Menu;
import com.example.pfc.core.RecycleViewAdapter;
import com.example.pfc.core.UTILES;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class MenuRestaurante extends AppCompatActivity {
    String login, contra, nombre;
    private ArrayList<Menu> listaMenu = new ArrayList<>();
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Menu> menus= new ArrayList<>();
    public static final String TRANSITION_FAB = "fab_transition";
    public static final String TRANSITION_INITIAL = "initial_transition";
    public static final String TRANSITION_NAME = "name_transition";
    public static final String TRANSITION_DELETE_BUTTON = "delete_button_transition";
    public static final String EXTRA_UPDATE = "update";
    public static final String EXTRA_DELETE = "delete";
    public static final String EXTRA_COLOR = "color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        loadActivity();

    }
    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }
    private void loadActivity() {

        Bundle extras = getIntent().getExtras();
        login = extras.getString("log");
        contra = extras.getString("contra");
        listaMenu.clear();
        initCards();
        if (adapter == null) {
            adapter = new RecycleViewAdapter(this, listaMenu);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<View, String> pair = Pair.create(v.findViewById(R.id.fab), TRANSITION_FAB);
                ActivityOptionsCompat options;
                Activity act = MenuRestaurante.this;
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair);
                Intent transitionIntent = new Intent(act, AddMenu.class);
                transitionIntent.putExtra("pos", -1);
                transitionIntent.putExtra("login", login);
                act.startActivityForResult(transitionIntent, adapter.getItemCount(), options.toBundle());
            }
        });
    }

    private void initCards() {



        Request req = new Request();
        req.execute(login,contra);
        //creamos un menu y lo añadimos a la lista de lista menu
        //listaMenu.add(entrenamiento);








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
                System.out.println(menus.size()+"aqui va el tamaño del arraylist");
                listaMenu=menus;
                adapter = new RecycleViewAdapter(MenuRestaurante.this, listaMenu);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
