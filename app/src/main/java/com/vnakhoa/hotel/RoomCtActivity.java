package com.vnakhoa.hotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.vnakhoa.hotel.adapter.SlideAdapter;
import com.vnakhoa.hotel.model.Phong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RoomCtActivity extends AppCompatActivity {

    private ViewPager vpRoom;
    private ArrayList<Integer> listImg;
    SlideAdapter slideAdapter;
    String dat,tra;
    private TabLayout indicatorRoom;
    TextView txtChiTiet,txtNgayDat,txtNgayTra;
    Button btnKT;
    Phong phong;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_ct);

        addControls();
        addEvents();
    }

    private void addEvents() {
        txtNgayDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyDate(txtNgayDat);
            }
        });
        txtNgayTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyDate(txtNgayTra);
            }
        });
        btnKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dat = txtNgayDat.getText().toString().trim().equals("dd-mm-yyyy") ? spf.format(calendar.getTime()) : txtNgayDat.getText().toString().trim();
                tra = txtNgayTra.getText().toString().trim().equals("dd-mm-yyyy") ? spf.format(calendar.getTime()) : txtNgayTra.getText().toString().trim();
                Intent intent = new Intent(RoomCtActivity.this, DatPhongActivity.class);
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
                RoomCtActivity.this,
                callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        date.show();
    }

    private void addControls() {
        vpRoom = findViewById(R.id.vpRoom);
        listImg = new ArrayList<>();
        slideAdapter = new SlideAdapter(RoomCtActivity.this,listImg);
        vpRoom.setAdapter(slideAdapter);
        addData();
        indicatorRoom = findViewById(R.id.indicatorRoom);
        indicatorRoom.setupWithViewPager(vpRoom,true);
        txtChiTiet = findViewById(R.id.txtChiTiet);
        txtNgayDat = findViewById(R.id.txtNgayDat);
        txtNgayTra = findViewById(R.id.txtNgayTra);
        btnKT = findViewById(R.id.btnKT);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RoomCtActivity.SlideTime(),4000,6000);
        nDung();
    }

    private void addData() {
            int[] arr = {
                    R.drawable.typeroom1,R.drawable.typeroom1,
                    R.drawable.typeroom11,R.drawable.typeroom2,
                    R.drawable.typeroom12,R.drawable.typeroom3,
                    R.drawable.room1,R.drawable.room6,
                    R.drawable.room2, R.drawable.room3,
                    R.drawable.room4, R.drawable.room5
            };

            for (int i = 0;i < 5;i++)
            {
                int n = new Random().nextInt(arr.length);
                if (listImg.contains(arr[n]))
                {
                    i=i-1;
                }
                else
                {
                    listImg.add(arr[n]);
                }
            }
            slideAdapter.notifyDataSetChanged();
    }

    public void nDung()
    {
        Intent intent = getIntent();
        if (intent.hasExtra("Room"))
        {
            phong = (Phong) intent.getSerializableExtra("Room");
            txtChiTiet.setText(Html.fromHtml(phong.getnDung()));
            this.setTitle(phong.getTenPhong());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class SlideTime extends TimerTask
    {
        @Override
        public void run() {
            RoomCtActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (vpRoom.getCurrentItem()<listImg.size()-1)
                    {
                        vpRoom.setCurrentItem(vpRoom.getCurrentItem()+1);
                    }
                    else
                    {
                        vpRoom.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
