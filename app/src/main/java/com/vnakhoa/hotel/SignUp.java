package com.vnakhoa.hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.google.android.material.textfield.TextInputLayout;
import com.vnakhoa.hotel.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.vnakhoa.hotel.LoginActivity.CONNECTION_TIMEOUT;
import static com.vnakhoa.hotel.LoginActivity.READ_TIMEOUT;

public class SignUp extends AppCompatActivity {
    TextInputLayout txtPassword,txtName,txtUser,txtComfirm;
    Button btnLogin;
    ProgressBar progressBar;
    TextView txtSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addControls();
        addEvent();
    }

    private void addEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validete(txtName) | !validete(txtUser) | !validete(txtPassword) | !validete(txtComfirm)){
                    return;
                }
                String name = txtName.getEditText().getText().toString();
                String username = txtUser.getEditText().getText().toString();
                String password = txtPassword.getEditText().getText().toString();

                User user = new User(null,name,username,password,null);
                SignTask signTask = new SignTask();
                signTask.execute(user);
            }
        });

    }

    private boolean validete(TextInputLayout textInputLayout) {
        boolean check = true ;
        String text = textInputLayout.getEditText().getText().toString();
        if (text.isEmpty()) {
            textInputLayout.setError(textInputLayout.getHint()+" can't be empty");
            return false;
        }
        if (textInputLayout == txtComfirm) {
            if (!text.equals(txtPassword.getEditText().getText().toString())){
                textInputLayout.setError("Please "+ textInputLayout.getHint());
                return false;
            }
        }
        textInputLayout.setError("");
        return check;
    }

    private void addControls() {
        btnLogin = findViewById(R.id.btnSignup);
        txtPassword = findViewById(R.id.txtPassword);
        txtUser = findViewById(R.id.txtUserName);
        progressBar = findViewById(R.id.progressBar);
        txtSignin = findViewById(R.id.txtSignup);
        txtComfirm = findViewById(R.id.txtconfirm);
        txtName = findViewById(R.id.txtName);
    }

    class SignTask extends AsyncTask<User,Void,User> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user.toString() != null){
                if (user.getError() == null)
                {
                    Intent intent = new Intent();
                    intent.putExtra("User",user);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else {

                    Toast.makeText(SignUp.this,user.getError(),Toast.LENGTH_LONG).show();
                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected User doInBackground(User... users) {
            User user = users[0];
            try {
                URL url=new URL(new Service().URLSERVER+"api/create");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                Uri.Builder builders = new Uri.Builder()
                        .appendQueryParameter("fullname", user.getFullName())
                        .appendQueryParameter("username", user.getUserName())
                        .appendQueryParameter("password", user.getPassWord());
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
                    User user1 = new User();
                    if(jsonObject.has("error")) {
                        user1.setError(jsonObject.getString("error"));
                        return user1;
                    }
                    if(jsonObject.has("_id")) {
                        user1.setId(jsonObject.getString("_id"));
                    }
                    if(jsonObject.has("fullname")) {
                        user1.setFullName(jsonObject.getString("fullname"));
                    }
                    if(jsonObject.has("username")) {
                        user1.setUserName(jsonObject.getString("username"));
                    }
                    if(jsonObject.has("password")) {
                        user1.setPassWord(jsonObject.getString("password"));
                    }
                    Log.d("JSON_Login",json);
                    br.close();
                    isr.close();
                    connection.disconnect();
                    return user1;
                }
            }
            catch (Exception ex){
                Log.e("Loi",ex.toString());
            }
            return null;
        }
    }
}
