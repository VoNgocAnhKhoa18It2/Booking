package com.vnakhoa.hotel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vnakhoa.hotel.R;
import com.vnakhoa.hotel.RoomCtActivity;
import com.vnakhoa.hotel.model.Phong;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterPhong extends RecyclerView.Adapter<AdapterPhong.PhongHolder> {

    Context context;
    ArrayList<Phong> list;
    Phong phong;

    public AdapterPhong(Context context, ArrayList<Phong> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PhongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_phong,parent,false);
        PhongHolder holder = new PhongHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhongHolder holder, int position) {
        phong = list.get(position);
        holder.img.setImageBitmap(phong.getImg());
        holder.txtTen.setText(phong.getTenPhong());
        holder.txtGia.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(phong.getGia()) +".000 VND");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phong room = new Phong();
                room.setIdPhong(phong.getIdPhong());
                room.setnDung(phong.getnDung());
                room.setTenPhong(phong.getTenPhong());
                Intent intent = new Intent(context, RoomCtActivity.class);
                intent.putExtra("Room", room);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PhongHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txtTen,txtGia;
        CardView item;

        public PhongHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imagePhong);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtTen = itemView.findViewById(R.id.txtTen);
            item = itemView.findViewById(R.id.item);
        }
    }
}
