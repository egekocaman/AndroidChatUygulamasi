package com.example.wpchatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GrupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton MesajGonderButonu;
    private EditText kullaniciMesajıGirdisi;
    private ScrollView mScrollView;
    private TextView metinMesajlariniGoster;

    //Intent Değişkeni
    private String mevcutGrupAdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_chat);

        //Intenti Al
        mevcutGrupAdi=getIntent().getExtras().get("grupAdı").toString();
        Toast.makeText(this, mevcutGrupAdi, Toast.LENGTH_LONG).show(); //Intenti alıp almadığımızı göstermek için

        //Tanımlamalar
        mToolbar = findViewById(R.id.grup_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mevcutGrupAdi);

        MesajGonderButonu=findViewById(R.id.mesaj_gonderme_buttonu);
        kullaniciMesajıGirdisi=findViewById(R.id.grup_mesaji_girdisi);
        metinMesajlariniGoster=findViewById(R.id.grup_chat_metni_gosterme);
        mScrollView=findViewById(R.id.my_scroll_view);



    }
}