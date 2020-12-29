package com.androidmads.navdraweractivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidmads.navdraweractivity.R;

public class ImageAdapter extends BaseAdapter {
    Context context;
    String countryList[];
    int flags[];
    LayoutInflater inflter;

    public ImageAdapter(Context applicationContext, String[] countryList, int[] flags) {
        this.context = context;
        this.countryList = countryList;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.listview, null);
        TextView country = (TextView) view.findViewById(R.id.name);
        ImageView icon = (ImageView) view.findViewById(R.id.image);
       // ImageView icona = (ImageView) view.findViewById(R.id.icona);
        //icona.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
        country.setText(countryList[i]);
        icon.setImageResource(flags[i]);
        return view;
    }
}
