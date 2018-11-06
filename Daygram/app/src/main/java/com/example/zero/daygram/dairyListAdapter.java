package com.example.zero.daygram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class dairyListAdapter extends ArrayAdapter {
    List<View> items;
    public dairyListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        items=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return items.get(position);
    }
}
