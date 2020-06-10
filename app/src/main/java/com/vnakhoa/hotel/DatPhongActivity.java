package com.vnakhoa.hotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vnakhoa.hotel.model.ContactBooking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class DatPhongActivity extends AppCompatActivity {
    ListView lvPhong;
    ArrayList<String> dsroom ;
    ArrayAdapter<String> roomArrayAdapter;
    TextView txtDateD,txtDateT;
    RadioButton rbtnMale,rbtnFemale;
    Button btnBoooking;
    String iddm,room;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
    EditText txtFName,txtID,txtEmail;
    AutoCompleteTextView txtQG;
    String []arrQG;
    ArrayAdapter<String> adapterQG;
    private Socket socket;
    {
        try {
            socket = IO.socket( new Service().URLSERVER);
        }catch (URISyntaxException ex){
            Log.e("LOI_Connect",ex.toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_phong);

        setTitle("Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControls();
        addEvents();
        socket.on("ans",answer);
        socket.on("send-room", listRoom);
        socket.on("notification",notification);
        socket.on("error-in-room", roomError);
        socket.connect();
    }

    public Emitter.Listener roomError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Toast.makeText(DatPhongActivity.this,"Het Luot Dat Phong! Vui Long Quay Lai Sau 5 Phut",Toast.LENGTH_LONG).show();
            finish();
        }
    };

    public Emitter.Listener notification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                String in = new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(txtDateD.getText().toString()));
                String out = new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(txtDateT.getText().toString()));
                JSONObject data = new JSONObject();
                data.put("datein",in);
                data.put("dateout",out);
                data.put("iddm",iddm);
                socket.emit("check-room",data);
            }catch (Exception ex){
                Log.e("loi date",ex.toString());
            }
        }
    };

    public Emitter.Listener answer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Boolean check = (Boolean) args[0];
                    try {
                        if (check == true) {
                            Toast.makeText(DatPhongActivity.this,"Booking Successfull",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DatPhongActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DatPhongActivity.this,"Booking Fail",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {

                    }
                }
            });
        }
    };

    public Emitter.Listener listRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = (JSONArray) args[0];
                        dsroom.clear();
                        Log.d("JSon room", jsonArray.toString());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                dsroom.add(jsonArray.getString(i));
                            }
                            roomArrayAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(DatPhongActivity.this,"Het Phong! Vui Long Chon Ngay Khac",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Log.e("LOI", ex.toString());
                    }
                }
            });
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    private void addEvents() {
        btnBoooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   String fullmane = txtFName.getText().toString().trim();
                   String gt = "";
                   if (rbtnMale.isChecked()) gt = rbtnMale.getText().toString();
                   if (rbtnFemale.isChecked()) gt = rbtnFemale.getText().toString();
                   String idCard = txtID.getText().toString().trim();
                   String email = txtEmail.getText().toString().trim();
                   String qg = txtQG.getText().toString().trim();
                   String datein = new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(txtDateD.getText().toString()));
                   String dateout = new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(txtDateT.getText().toString()));

                   if (fullmane.isEmpty() || email.isEmpty() || idCard.isEmpty() || qg.isEmpty())
                   {
                       Toast.makeText(DatPhongActivity.this,"Please enter full information",Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                       ContactBooking customer = new ContactBooking(fullmane,gt,idCard,email,qg,datein,dateout,room,iddm);
                       socket.emit("check",customer.toJson());
                   }
               }catch (Exception ex){

               }
            }
        });

        txtDateT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyDate(txtDateT);
            }
        });
        txtDateD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyDate(txtDateD);
            }
        });
        lvPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                room = dsroom.get(position);
            }
        });

    }

    private void xulyDate(final TextView textView) {
        try {
            calendar.setTime(spf.parse(textView.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                textView.setText(spf.format(calendar.getTime()));
                if (textView.equals(txtDateT))
                {
                    try {
                        String in = new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(txtDateD.getText().toString()));
                        String out = new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(txtDateT.getText().toString()));
                        JSONObject data = new JSONObject();
                        data.put("datein",in);
                        data.put("dateout",out);
                        data.put("iddm",iddm);
                        socket.emit("check-room",data);
                    }catch (Exception ex){
                        Log.e("loi date",ex.toString());
                    }
                }
            }
        };
        DatePickerDialog date = new DatePickerDialog(
                DatPhongActivity.this,
                callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        date.show();
    }

    private void addControls() {
        lvPhong = findViewById(R.id.lvphong);
        txtDateD = findViewById(R.id.txtDateD);
        txtDateT = findViewById(R.id.txtDateT);
        rbtnMale = findViewById(R.id.rbtnMale);
        rbtnMale.setChecked(true);
        rbtnFemale = findViewById(R.id.rbtnFemale);
        btnBoooking = findViewById(R.id.btnBooking);
        txtFName = findViewById(R.id.txtFName);
        txtEmail = findViewById(R.id.txtEmail);
        txtID = findViewById(R.id.txtID);
        txtQG = findViewById(R.id.txtQG);
        arrQG = getResources().getStringArray(R.array.QG);
        adapterQG = new ArrayAdapter<String>(
                DatPhongActivity.this,
                android.R.layout.simple_list_item_1,
                arrQG
        );
        txtQG.setAdapter(adapterQG);
        dsroom = new ArrayList<>();
        roomArrayAdapter = new ArrayAdapter<String>(
                DatPhongActivity.this,
                android.R.layout.simple_list_item_1,
                dsroom
                );
        lvPhong.setAdapter(roomArrayAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra("ID"))
        {
            iddm = intent.getStringExtra("ID");
            socket.emit("send-id-room",iddm);
            String in = intent.getStringExtra("Date_Dat");
            String out = intent.getStringExtra("Date_Tra");
            txtDateD.setText(in);
            txtDateT.setText(out);
            try {
                JSONObject data = new JSONObject();
                data.put("datein",new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(in)));
                data.put("dateout",new SimpleDateFormat("yyyy-MM-dd").format(spf.parse(out)));
                data.put("iddm",iddm);
                socket.emit("check-room",data);
            }catch (Exception ex){
                Log.e("loi date",ex.toString());
            }
        }
    }
}
