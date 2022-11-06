package com.example.wpchatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button girisButonu ;
    private EditText KullaniciMail, KullaniciSifre;
    private TextView YeniHesapAlma, SifreUnutmaBaglanti;

    private FirebaseUser mevcutKullanici;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Kontrol Tanımlamaları

        girisButonu=findViewById(R.id.giris_butonu);


        KullaniciMail=findViewById(R.id.giris_email);
        KullaniciSifre=findViewById(R.id.giris_sifre);

        YeniHesapAlma=findViewById(R.id.yeni_hesap_alma);
        SifreUnutmaBaglanti=findViewById(R.id.sifre_unutma_baglantisi);


        YeniHesapAlma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent kayitAktivityIntent=new Intent(LoginActivity.this,KayitActivity.class);
                startActivity(kayitAktivityIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    if (mevcutKullanici !=null)
    {
        KullaniciyiAnaAktivitiyeGonder();
    }


    }

    private void KullaniciyiAnaAktivitiyeGonder()
    {
        Intent AnaAktiviteIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(AnaAktiviteIntent);
    }
}