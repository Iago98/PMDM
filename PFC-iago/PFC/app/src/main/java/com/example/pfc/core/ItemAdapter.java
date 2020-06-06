package com.example.pfc.core;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfc.R;
import com.example.pfc.ui.MenuComun;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<RegistroRestaurante> items;
    DecimalFormat formato1 = new DecimalFormat("#.00");


    public ItemAdapter(Activity activity, ArrayList<RegistroRestaurante> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi = contentView;
        if (contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.row_comun, null);
        }
        RegistroRestaurante item = items.get(position);
        Button menus = (Button) vi.findViewById(R.id.button3);
        menus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestMenuComun req= new RequestMenuComun();
                req.execute(item.getLogin()+"noPass");

            }
        });
        TextView nombre = (TextView) vi.findViewById(R.id.textView5);
        nombre.setText(item.getNombreHosteleria());
        TextView distancia = (TextView) vi.findViewById(R.id.textView2);
        distancia.setText(activity.getString(R.string.distancia)+" " + String.valueOf(formato1.format(item.getDistancia()))+" Km.");
        return vi;
    }
}