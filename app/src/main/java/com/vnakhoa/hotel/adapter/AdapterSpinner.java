package com.vnakhoa.hotel.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.vnakhoa.hotel.R;
import com.vnakhoa.hotel.model.Phong;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.graphics.Color.rgb;

public class AdapterSpinner extends ArrayAdapter<Phong> {

    Context context;
    ArrayList<Phong> list;

    public AdapterSpinner(@NonNull Context context, ArrayList<Phong> list) {
        super(context, 0,list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phong,parent,false);
        Phong phong = list.get(position);
        ImageView imgPhong = view.findViewById(R.id.imagePhong);
        TextView txtName = view.findViewById(R.id.txtTen);
        TextView txtGia = view.findViewById(R.id.txtGia);
        CardView item = view.findViewById(R.id.item);

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(200,200);
        imgPhong.setLayoutParams(parms);
        imgPhong.setImageBitmap(phong.getImg());
        txtGia.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(phong.getGia()) +".000 VND");
        txtName.setText(phong.getTenPhong());
        txtName.setTextSize(15);
        txtGia.setTextSize(12);
        item.setRadius(0);
        item.setContentPadding(0,5,0,5);
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phong,parent,false);
        Phong phong = list.get(position);
        ImageView imgPhong = view.findViewById(R.id.imagePhong);
        TextView txtName = view.findViewById(R.id.txtTen);
        TextView txtGia = view.findViewById(R.id.txtGia);

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(200,200);
        imgPhong.setLayoutParams(parms);
        imgPhong.setImageBitmap(phong.getImg());
        txtName.setText(phong.getTenPhong());
        txtName.setTextSize(15);
        txtName.setPadding(0,22,0,0);
        txtGia.setVisibility(View.INVISIBLE);
        return view;
    }


}
