package com.vnakhoa.hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vnakhoa.hotel.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText txtUser,txtPass;
    Button btnLogin;
    ProgressBar progressBar;
    TextView txtSignup;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvent();
    }

    private void addEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyLogin();
            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void xulyLogin() {
        String user = txtUser.getText().toString().trim();
        String pass = txtPass.getText().toString().trim();
        if (user.isEmpty() || pass.isEmpty())
        {
            Toast.makeText(LoginActivity.this,"Username or password are empty",Toast.LENGTH_SHORT).show();
        }
        else
        {
            SginTask sginTask = new SginTask();
            sginTask.execute(user,pass);
        }
    }

    private void addControls() {
        btnLogin = findViewById(R.id.btnLogin);
        txtPass = findViewById(R.id.txtPassword);
        txtUser = findViewById(R.id.txtUser);
        progressBar = findViewById(R.id.progressBar);
        txtSignup = findViewById(R.id.txtSignup);
    }

    class SginTask extends AsyncTask<String,Void,User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            if (user.getError() == null)
            {
                Intent intent = new Intent();
                intent.putExtra("User",user);
                setResult(RESULT_OK,intent);
                finish();

            }
            else {

                Toast.makeText(LoginActivity.this,user.getError(),Toast.LENGTH_LONG).show();
                btnLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected User doInBackground(String... strings) {
            User user = new User();
            try {
                URL url=new URL(new Service().URLSERVER+"api/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                Uri.Builder builders = new Uri.Builder()
                        .appendQueryParameter("username", strings[0])
                        .appendQueryParameter("password", strings[1]);
                String query = builders.build().getEncodedQuery();
                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                connection.connect();

                int response_code = connection.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader isr=new InputStreamReader(connection.getInputStream(),"UTF-8");
                    BufferedReader br=new BufferedReader(isr);
                    String line=br.readLine();
                    StringBuilder builder=new StringBuilder();
                    while (line!=null)
                    {
                        builder.append(line);
                        line=br.readLine();
                    }
                    String json=builder.toString();
                    JSONObject jsonObject = new JSONObject(json);

                    if(jsonObject.has("error")) {
                        user.setError(jsonObject.getString("error"));
                    }
                    if(jsonObject.has("id")) {
                        user.setId(jsonObject.getString("id"));
                    }
                    if(jsonObject.has("fullname")) {
                        user.setFullName(jsonObject.getString("fullname"));
                    }
                    if(jsonObject.has("username")) {
                        user.setUserName(jsonObject.getString("username"));
                    }
                    if(jsonObject.has("password")) {
                        user.setPassWord(jsonObject.getString("password"));
                    }
                    Log.d("JSON_Login",json);
                    br.close();
                    isr.close();
                    connection.disconnect();
                    return user;
                }
            }
            catch (Exception ex){
                Log.e("Loi",ex.toString());
            }
            return null;
        }
    }
}
