package com.example.gson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button bton;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) this.findViewById(R.id.editText);
        lvItems = (ListView) this.findViewById(R.id.lvItems);
        bton = (Button) this.findViewById(R.id.button);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        this.registerForContextMenu(lvItems);
        this.adapter = new ArrayAdapter<>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                PostalCodesVO.getPostalCodes()
        );
        lvItems.setAdapter(this.adapter);


        bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HttpRequest.peticion(editText.getText().toString());
                    seleccionar();
                }catch (Exception e){

                }


            }
        });


    }
    private void selector(String var){
          List<PostalCode> postalCodes= new ArrayList<>();
        for(int x=0;x<PostalCodesVO.getPostalCodes().size();x++){

            if(PostalCodesVO.getPostalCodes().get(x).getCountryCode().equals(var)){
                postalCodes.add(PostalCodesVO.getPostalCodes().get(x));
            }
        }
        PostalCodesVO.setPostalCodes(postalCodes);

        MainActivity.this.adapter.notifyDataSetChanged();

    }

    private void seleccionar(){
        final int[] pais = new int[1];
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final String[] algo = {""};
        final CharSequence items[] = JsonOb.getArray();
        adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pais[0] =which;
                algo[0] =items[pais[0]].toString();
            }
        });
        adb.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            System.out.println("aquiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+algo[0]);
            selector(algo[0]);
            System.out.println("borrados"+PostalCodesVO.getPostalCodes().toString());
                MainActivity.this.adapter.notifyDataSetChanged();



            }
        });
        adb.setNegativeButton("Cancel", null);
        adb.setTitle("Which one?");
        adb.show();
    }
    private ArrayAdapter<PostalCode> adapter;
}

