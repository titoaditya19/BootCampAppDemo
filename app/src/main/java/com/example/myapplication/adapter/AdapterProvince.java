package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.Province;

import java.util.List;


public class AdapterProvince extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Province> item;

    public AdapterProvince(Activity activity, List<Province> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_address, null);

        TextView pendidikan = (TextView) convertView.findViewById(R.id.list_province);

        Province province;
        province = item.get(position);

        pendidikan.setText(province.getName());

        return convertView;
    }
}