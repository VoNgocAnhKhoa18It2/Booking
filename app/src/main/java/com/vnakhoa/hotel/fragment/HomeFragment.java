package com.vnakhoa.hotel.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.vnakhoa.hotel.DatPhongActivity;
import com.vnakhoa.hotel.R;
import com.vnakhoa.hotel.adapter.AdapterPhong;
import com.vnakhoa.hotel.adapter.AdapterSpinner;
import com.vnakhoa.hotel.adapter.SlideAdapter;
import com.vnakhoa.hotel.model.Phong;
import com.vnakhoa.hotel.task.RoomTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    private ArrayList<Integer> listImg;
    private TextView txtTra,txtDat;
    private Button btnKT;
    private Spinner srPhong;
    Phong phong;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
    String dat,tra;
    TextView txtGioiThieu;
    RecyclerView rcPhong;
    ArrayList<Phong> dsphong;
    AdapterPhong adapterPhong;

    AdapterSpinner adapterSpiner;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout indicator = view.findViewById(R.id.indicator);
        srPhong = view.findViewById(R.id.srPhong);
        txtDat = view.findViewById(R.id.txtDat);
        txtTra = view.findViewById(R.id.txtTra);
        btnKT = view.findViewById(R.id.btnKiemTra);
        rcPhong = view.findViewById(R.id.rcPhong);
        txtGioiThieu = view.findViewById(R.id.txtGioiThieu);
        txtGioiThieu.setText(getString(R.string.gioithieu));

        PhongTask phongTask = new PhongTask();
        phongTask.execute();

        SlideAdapter slideAdapter = new SlideAdapter(getActivity(), listImg);
        viewPager.setAdapter(slideAdapter);
        indicator.setupWithViewPager(viewPager,true);

        addEvent();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listImg = new ArrayList<>();
        listImg.add(R.drawable.banner_1);
        listImg.add(R.drawable.banner_2);
        listImg.add(R.drawable.banner_3);

        dsphong = new ArrayList<>();


    }

    private void addEvent() {
        txtDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyDate(txtDat);
            }
        });
        txtTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyDate(txtTra);
            }
        });
        btnKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phong = (Phong) srPhong.getSelectedItem();
                dat = txtDat.getText().toString().trim().equals("dd/mm/yyyy") ? spf.format(calendar.getTime()) : txtDat.getText().toString().trim();
                tra = txtTra.getText().toString().trim().equals("dd/mm/yyyy") ? spf.format(calendar.getTime()) : txtTra.getText().toString().trim();

                Intent intent = new Intent(getContext(), DatPhongActivity.class);
                intent.putExtra("ID",phong.getIdPhong());
                intent.putExtra("Date_Dat",dat);
                intent.putExtra("Date_Tra",tra);
                startActivity(intent);

            }
        });
    }

    private void xulyDate(final TextView textView) {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                textView.setText(spf.format(calendar.getTime()));
            }
        };
        DatePickerDialog date = new DatePickerDialog(
                getContext(),
                callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        date.show();
    }

    class PhongTask extends RoomTask
    {
        @Override
        protected void onPostExecute(ArrayList<Phong> phongs) {
            super.onPostExecute(phongs);
            adapterPhong = new AdapterPhong(getContext(),phongs);
            rcPhong.setAdapter(adapterPhong);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            rcPhong.setLayoutManager(manager);
            adapterSpiner = new AdapterSpinner(getContext(),phongs);
            srPhong.setAdapter(adapterSpiner);
        }
    }

}
