package com.vnakhoa.hotel.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vnakhoa.hotel.Service;
import com.vnakhoa.hotel.adapter.AdapterPhong;
import com.vnakhoa.hotel.model.Phong;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RoomTask extends AsyncTask<Void,Void, ArrayList<Phong>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Phong> phongs) {
        super.onPostExecute(phongs);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<Phong> doInBackground(Void... voids) {
        ArrayList<Phong> dsPhong = new ArrayList<>();
        String urlservice = new Service().URLSERVER+"api/room";
        try {
            URL url=new URL(urlservice);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json; charset=utf-8");

            InputStream is= connection.getInputStream();
            InputStreamReader isr=new InputStreamReader(is,"UTF-8");
            BufferedReader br=new BufferedReader(isr);
            String line=br.readLine();
            StringBuilder builder=new StringBuilder();
            while (line!=null)
            {
                builder.append(line);
                line=br.readLine();
            }
            String json=builder.toString();
            JSONArray jsonArray = new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject item=jsonArray.getJSONObject(i);
                Phong phong = new Phong();
                if (item.has("_id"))
                {
                    phong.setIdPhong(item.getString("_id"));
                }
                if (item.has("tendm"))
                {
                    phong.setTenPhong(item.getString("tendm"));
                }
                if (item.has("hinhanh"))
                {
                    phong.setUrlImg(new Service().URLSERVER+"assets/images/"+item.getString("hinhanh"));
                    url=new URL(phong.getUrlImg());
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    phong.setImg(bitmap);
                }
                if (item.has("gia"))
                {
                    phong.setGia(item.getInt("gia"));
                }
                if (item.has("noidung"))
                {
                    phong.setnDung(item.getString("noidung"));
                }
                if (item.has("ndbeg"))
                {
                    phong.setnDungGT(item.getString("ndbeg"));
                }
                dsPhong.add(phong);
            }
            Log.d("JSON_Phong",json);
        }
        catch (Exception e)
        {
            Log.e("Loi",e.toString());
        }
        return dsPhong;
    }
}
