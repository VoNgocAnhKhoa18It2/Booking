package com.vnakhoa.hotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.vnakhoa.hotel.fragment.HomeFragment;
import com.vnakhoa.hotel.fragment.RoomFragment;
import com.vnakhoa.hotel.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView view;
    ArrayList<Integer> arrImgUser;
    Random random = new Random();
    Menu nav_Menu ;
    User user ;
    int n = random.nextInt(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
    }

    private void addControls() {
        drawerLayout = findViewById(R.id.dwlayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        arrImgUser = new ArrayList<>();
        arrImgUser.add(R.drawable.bruno);
        arrImgUser.add(R.drawable.bale);
        arrImgUser.add(R.drawable.ronadol);
        arrImgUser.add(R.drawable.messi);
        arrImgUser.add(R.drawable.pogba);
        arrImgUser.add(R.drawable.khoa);

        view = findViewById(R.id.nvgView);


        nav_Menu = view.getMenu();
        nav_Menu.findItem(R.id.logout).setVisible(false);

        view.setNavigationItemSelectedListener(MainActivity.this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout,new HomeFragment());
        ft.commit();

        view.setCheckedItem(R.id.home);

    }

    public void headerNav (NavigationView nav,User user)
    {
        View view = nav.getHeaderView(0);
        ImageView imgUser = view.findViewById(R.id.imgUser);
        TextView txtUser = view.findViewById(R.id.txtUser);
        TextView txtFullName = view.findViewById(R.id.txtFullName);

        imgUser.setImageResource(arrImgUser.get(n));
        txtUser.setText(user.getUserName());
        txtFullName.setText(user.getFullName());

        nav_Menu = nav.getMenu();
        nav_Menu.findItem(R.id.signin).setVisible(false);
        nav_Menu.findItem(R.id.signup).setVisible(false);
        nav_Menu.findItem(R.id.logout).setVisible(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                user = (User) data.getSerializableExtra("User");
                headerNav(view,user);
                SharedPreferences preferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ID",user.getId());
                editor.putString("UserName",user.getUserName());
                editor.putString("FullName",user.getFullName());
                editor.putString("Password",user.getPassWord());
                editor.commit();
                setTitle("Home");
                Toast.makeText(MainActivity.this,"Wellcome "+ user.getFullName(),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        if (preferences != null)
        {
            String id = preferences.getString("ID",null);
            String username = preferences.getString("UserName",null);
            String pass = preferences.getString("Password",null);
            String fullname = preferences.getString("FullName",null);
            user = new User(id,fullname,username,pass,null);

            if (username != null)
            {
                headerNav(view,user);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId())
        {
            case R.id.signin:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.home:
                ft.replace(R.id.fragment_layout,new HomeFragment());
                ft.commit();
                break;
            case R.id.phong:
                ft.replace(R.id.fragment_layout,new RoomFragment());
                ft.commit();
                break;
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent logout = new Intent(getApplicationContext(),LoginActivity.class);
                startActivityForResult(logout,1);
                break;
            case R.id.signup:
                Intent signup = new Intent(MainActivity.this,SignUp.class);
                startActivityForResult(signup,1);
                break;
            default:
                ft.replace(R.id.fragment_layout,new HomeFragment());
                ft.commit();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        setTitle(item.getTitle());
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawe = findViewById(R.id.dwlayout);
        if (drawe.isDrawerOpen(GravityCompat.START))
        {
            drawe.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }
}
