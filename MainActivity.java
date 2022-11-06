package com.example.wpchatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private SekmeErisimAdapter mysekmeErisimAdapter;

    //Firebase
    private FirebaseUser mevcutKullanici;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=findViewById(R.id.ana_sayfa_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WPChatApp");

    myViewPager=findViewById(R.id.ana_sekmeler_pager);
    mysekmeErisimAdapter=new SekmeErisimAdapter(getSupportFragmentManager());
    myViewPager.setAdapter(mysekmeErisimAdapter);

    myTabLayout=findViewById(R.id.ana_sekmeler);
    myTabLayout.setupWithViewPager(myViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mevcutKullanici ==null)
        {
            KullaniciyiLoginActivityeGonder();
        }
    }

    private void KullaniciyiLoginActivityeGonder()
    {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
}