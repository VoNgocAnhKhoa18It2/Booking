package com.vnakhoa.hotel.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vnakhoa.hotel.R;
import com.vnakhoa.hotel.adapter.AdapterPhong;
import com.vnakhoa.hotel.adapter.AdapterRoom;
import com.vnakhoa.hotel.model.Phong;
import com.vnakhoa.hotel.task.RoomTask;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment {

    private RecyclerView rcRoom;
    ArrayList<Phong> dsRoom;
    AdapterRoom adapterRoom;
    public RoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        rcRoom = view.findViewById(R.id.rcRoom);
        adapterRoom = new AdapterRoom(getActivity(),dsRoom);
        PhongAsy phongAsy = new PhongAsy();
        phongAsy.execute();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dsRoom = new ArrayList<>();
    }

    class PhongAsy extends RoomTask
    {
        @Override
        protected void onPostExecute(ArrayList<Phong> phongs) {
            super.onPostExecute(phongs);
            adapterRoom = new AdapterRoom(getActivity(),phongs);
            rcRoom.setAdapter(adapterRoom);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            rcRoom.setLayoutManager(manager);
        }
    }

}
