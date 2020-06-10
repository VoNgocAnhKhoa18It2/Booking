package com.vnakhoa.hotel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.vnakhoa.hotel.R;
import com.vnakhoa.hotel.RoomCtActivity;
import com.vnakhoa.hotel.model.Phong;

import java.util.ArrayList;
import java.util.List;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.RoomHolder>{

    public Activity context;
    ArrayList<Phong> list;
    Phong phong;

    public AdapterRoom(Activity context, ArrayList<Phong> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.roon_item,parent,false);
        RoomHolder roomHolder = new RoomHolder(view);
        return roomHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomHolder holder, final int position) {
        phong = list.get(position);
        holder.imgRoom.setImageBitmap(phong.getImg());
        holder.txtNameRoom.setText(phong.getTenPhong());
        holder.txtCT.setText(Html.fromHtml(phong.getnDungGT()));
        holder.btnShow.setOnClickListener(new View.OnClickListener() {
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

    class RoomHolder extends RecyclerView.ViewHolder
    {
        ImageView imgRoom;
        TextView txtNameRoom,txtCT;
        Button btnShow;


        public RoomHolder(@NonNull View itemView) {
            super(itemView);
            txtCT = itemView.findViewById(R.id.txtCT);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            txtNameRoom = itemView.findViewById(R.id.txtNameRoom);
            btnShow = itemView.findViewById(R.id.btnShow);

        }
    }

}
