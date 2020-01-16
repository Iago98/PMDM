package com.resuadam.listacompra;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.resuadam.listacompra.ui.Actividad2;

import java.util.ArrayList;


public class MainActivity extends Activity {
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        MainActivity.this.items = new ArrayList<>();

        Button btAdd = (Button) this.findViewById( R.id.btAdd );
        ListView lvItems = (ListView) this.findViewById( R.id.lvItems );

        lvItems.setLongClickable( true );
        this.itemsAdapter = new ArrayAdapter<>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
        lvItems.setAdapter( this.itemsAdapter );


        //Inserta
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent actividad2 = new Intent( MainActivity.this, Actividad2.class );

                actividad2.putExtra( "nombre", "" );
                actividad2.putExtra( "cantidad", 1 );
                MainActivity.this.startActivityForResult( actividad2, CODIGO_ADICION_ITEM );
            }
        });

        //Modifica
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent actividad2 = new Intent( MainActivity.this, Actividad2.class );
                Item item = MainActivity.this.itemsAdapter.getItem(i);


                actividad2.putExtra( "nombre", item.getNombre() );
                actividad2.putExtra( "cantidad", item.getNum() );
                actividad2.putExtra( "pos", i );
                MainActivity.this.startActivityForResult( actividad2, CODIGO_EDICION_ITEM );

                return true;
            }
        });



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if ( requestCode == CODIGO_ADICION_ITEM
                && resultCode == Activity.RESULT_OK )
        {
            Item item = new Item( data.getExtras().getString( "nombre").toString() );
            item.setNum( data.getExtras().getInt( "cantidad" ) );
            this.itemsAdapter.add(item);
            this.updateStatus();
        }

        if ( requestCode == CODIGO_EDICION_ITEM
                && resultCode == Activity.RESULT_OK )
        {
            int pos = data.getExtras().getInt( "pos" );
            Item item = new Item( data.getExtras().getString( "nombre").toString() );

            item.setNum( data.getExtras().getInt( "cantidad" ) );
            this.items.set(pos, item);
            this.itemsAdapter.notifyDataSetChanged();
        }

        return;
    }

//    private void onAdd() {
//        final EditText edText = new EditText( this );
//
//        AlertDialog.Builder builder = new AlertDialog.Builder( this );
//        builder.setTitle("A comprar...");
//        builder.setMessage( "Nombre" );
//        builder.setView( edText );
//        builder.setPositiveButton( "+", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                final String text = edText.getText().toString();
//
//                MainActivity.this.itemsAdapter.add( text );
//                MainActivity.this.updateStatus();
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.create().show();
//    }

    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById( R.id.lblNum );
        txtNum.setText( Integer.toString( this.itemsAdapter.getCount() ) );
    }

    private ArrayAdapter<Item> itemsAdapter;
    private ArrayList<Item> items;
}