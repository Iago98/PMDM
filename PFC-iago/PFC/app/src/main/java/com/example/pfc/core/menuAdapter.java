package com.example.pfc.core;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.pfc.R;
import java.util.ArrayList;

public class menuAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Menu> items;


    public menuAdapter(Activity activity, ArrayList<Menu> items) {
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
            vi = inflater.inflate(R.layout.row_menus, null);
        }
        Menu item = items.get(position);
        TextView titulo = (TextView) vi.findViewById(R.id.textView5);
        titulo.setText(item.getTitulo());
        TextView descripcion = (TextView) vi.findViewById(R.id.textView2);
        descripcion.setText(item.getDescripcion());
        return vi;
    }
}