package com.example.wpchatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GrupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton MesajGonderButonu;
    private EditText kullaniciMesajıGirdisi;
    private ScrollView mScrollView;
    private TextView metinMesajlariniGoster;

    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference kullaniciYolu, grupAdiYolu,grupMesajAnahtariYolu;



    //Intent Değişkeni
    private String mevcutGrupAdi,aktifKullaniciId,aktifKullaniciAdi,aktifTarih, aktifZaman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_chat);

        //Intenti Al
        mevcutGrupAdi=getIntent().getExtras().get("grupAdı").toString();
        Toast.makeText(this, mevcutGrupAdi, Toast.LENGTH_LONG).show(); //Intenti alıp almadığımızı göstermek için

        //Firebase tanımlama
        mYetki=FirebaseAuth.getInstance();  //firebase ad auth dan bir örnek alsın
        aktifKullaniciId=mYetki.getCurrentUser().getUid(); //aktif kullanıcıyı al onunda uid
        kullaniciYolu= FirebaseDatabase.getInstance().getReference().child("Kullanicilar");
        grupAdiYolu= FirebaseDatabase.getInstance().getReference().child("Gruplar").child(mevcutGrupAdi);


        //Tanımlamalar
        mToolbar = findViewById(R.id.grup_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mevcutGrupAdi);

        MesajGonderButonu=findViewById(R.id.mesaj_gonderme_buttonu);
        kullaniciMesajıGirdisi=findViewById(R.id.grup_mesaji_girdisi);
        metinMesajlariniGoster=findViewById(R.id.grup_chat_metni_gosterme);
        mScrollView=findViewById(R.id.my_scroll_view);


        //Kullanici bilgisi alma
        kullaniciBilgisiAl();

        //Mesajı veri tabanına kayıt
        MesajGonderButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    MesajiVeriTabaninaKaydet();

                    kullaniciMesajıGirdisi.setText(""); //mesajı gönderdikten sonra ilgili editTexti boşaltsın

                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN); //otomatik olarak yukarı kayma

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        grupAdiYolu.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {  //çocuk eklendiğinde

                if (snapshot.exists())  //snapshot:veritabanındaki verileri temsil eder exists varsa grupları göstersin
                {
                    mesajlariGoster(snapshot);

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { //değiştiğinde

                if (snapshot.exists())  //snapshot:veritabanındaki verileri temsil eder exists varsa grupları göstersin
                {
                    mesajlariGoster(snapshot);

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { //kaldırıldığında

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { //hareket ettiğinde

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  //iptal olduğunda

            }
        });
    }

    private void mesajlariGoster(DataSnapshot snapshot) {

        Iterator iterator = snapshot.getChildren().iterator(); //Iterator:yenileyici satır satır yenileyerek işlem yapmamızı sağlar

        while (iterator.hasNext())
        {
            String sohbetTarihi = (String) ((DataSnapshot)iterator.next()).getValue();
            String sohbetMesaji = (String) ((DataSnapshot)iterator.next()).getValue();
            String sohbetAdi = (String) ((DataSnapshot)iterator.next()).getValue();
            String sohbetZamani = (String) ((DataSnapshot)iterator.next()).getValue();

            metinMesajlariniGoster.append(sohbetAdi +" :\n"+ sohbetMesaji +"\n"+sohbetZamani+"   "+sohbetTarihi+"\n\n\n");

            mScrollView.fullScroll(ScrollView.FOCUS_DOWN); //otomatik olarak yukarı kayma
        }
    }

    private void MesajiVeriTabaninaKaydet() {

        String mesaj=kullaniciMesajıGirdisi.getText().toString();
        String mesajAnahtari=grupAdiYolu.push().getKey();

        if (TextUtils.isEmpty(mesaj))
        {
            Toast.makeText(this, "Mesaj alanı boş olamaz ", Toast.LENGTH_LONG).show();
        }

        else
        {
            Calendar tarihİcinTak = Calendar.getInstance();
            SimpleDateFormat aktifTarihFormati = new SimpleDateFormat("MM dd, yyyy");
            aktifTarih=aktifTarihFormati.format(tarihİcinTak.getTime());

            Calendar zamanİcinTak = Calendar.getInstance();
            SimpleDateFormat aktifZamanFormati =new SimpleDateFormat("hh:mm:ss a");
            aktifZaman=aktifZamanFormati.format(zamanİcinTak.getTime());

            HashMap<String,Object>grupMesajAnahtari = new HashMap<>();
            grupAdiYolu.updateChildren(grupMesajAnahtari);

            grupMesajAnahtariYolu = grupAdiYolu.child(mesajAnahtari);  //grupların adının altına eklenecek mesajlar anahtar ile gidicek

            HashMap<String,Object> mesajBilgisiMap = new HashMap<>();

            mesajBilgisiMap.put("ad", aktifKullaniciAdi);  //kim mesaj atıyorsa onu göndersin
            mesajBilgisiMap.put("mesaj", mesaj);
            mesajBilgisiMap.put("tarih", aktifTarih);
            mesajBilgisiMap.put("zaman", aktifZaman);

            grupMesajAnahtariYolu.updateChildren(mesajBilgisiMap); //güncelledik mesaj bilgisi map ile
        }
    }

    private void kullaniciBilgisiAl() {

        kullaniciYolu.child(aktifKullaniciId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    aktifKullaniciAdi=snapshot.child("ad").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}