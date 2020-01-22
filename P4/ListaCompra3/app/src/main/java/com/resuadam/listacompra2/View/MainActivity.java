package com.resuadam.listacompra2.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.resuadam.listacompra2.Core.Item;
import com.resuadam.listacompra2.Core.ListaCompra;
import com.resuadam.listacompra2.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListaCompra app = (ListaCompra) this.getApplication();
        ListView lvLista = (ListView) this.findViewById(R.id.lvLista);
        Button btInserta = (Button) this.findViewById(R.id.btInserta);

        // Lista
        this.adaptadorItems = new ArrayAdapter<Item>(this, android.R.layout.simple_selectable_list_item, app.getItemList());
        lvLista.setAdapter(this.adaptadorItems);

        // Inserta
        btInserta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent subActividad = new Intent(MainActivity.this, ItemEditionActivity.class);
                subActividad.putExtra("pos", -1);
                MainActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_ITEM);
            }
        });

        // Modifica
        lvLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent subActividad = new Intent(MainActivity.this, ItemEditionActivity.class);
                subActividad.putExtra("pos", i);
                MainActivity.this.startActivityForResult(subActividad, CODIGO_EDICION_ITEM);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODIGO_ADICION_ITEM && resultCode == Activity.RESULT_OK) {
            this.adaptadorItems.notifyDataSetChanged();
            this.updateStatus();
        }
        if (requestCode == CODIGO_EDICION_ITEM && resultCode == Activity.RESULT_OK) {
            this.adaptadorItems.notifyDataSetChanged();
        }
        return;
    }

    private void updateStatus() {
        TextView lblNum = (TextView) this.findViewById(R.id.lblNum);
        lblNum.setText(Integer.toString(this.adaptadorItems.getCount()));
    }

    private ArrayAdapter<Item> adaptadorItems;
}