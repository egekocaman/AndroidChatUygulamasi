package com.example.wpchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {

    private String alinanKullaniciId;

    private CircleImageView kullaniciProfilresmi;
    private TextView kullaniciProfilAdi,kullaniciProfilDurumu;
   

    //Firebase
    private DatabaseReference KullaniciYolu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        alinanKullaniciId=getIntent().getExtras().get("tıklanan_kullanici_Id_goster").toString();

        //Tanımlamalar
        kullaniciProfilresmi=findViewById(R.id.profil_resmi_ziyaret);
        kullaniciProfilAdi=findViewById(R.id.kullanici_adi_ziyaret);
        kullaniciProfilDurumu=findViewById(R.id.profil_durumu_ziyaret);


        //Firebase
        KullaniciYolu= FirebaseDatabase.getInstance().getReference().child("Kullanicilar");

        kullaniciBilgisiAl();
    }

    private void kullaniciBilgisiAl() {

        KullaniciYolu.child(alinanKullaniciId).addValueEventListener(new ValueEventListener() { //verileri çekiyorduk
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {  //snapshotda ilgili veriyi temsil ediyor

                if ((snapshot.exists())&&(snapshot.hasChild("resim"))) //veritabanında resim diye bir şey varsa çek
                {
                    //Veri tabanından verileri çekip değişkenlere aktarma
                    String kullaniciResmi= snapshot.child("resim").getValue().toString();
                    String kullaniciAdi= snapshot.child("ad").getValue().toString();
                    String kullaniciDurumu= snapshot.child("durum").getValue().toString();

                    //Verileri Kontrollere aktarma
                    kullaniciProfilAdi.setText(kullaniciAdi);
                    kullaniciProfilDurumu.setText(kullaniciDurumu);

                }
                else
                {
                    //Veri tabanından verileri çekip değişkenlere aktarma
                    String kullaniciAdi= snapshot.child("ad").getValue().toString();
                    String kullaniciDurumu= snapshot.child("durum").getValue().toString();

                    //Verileri Kontrollere aktarma
                    kullaniciProfilAdi.setText(kullaniciAdi);
                    kullaniciProfilDurumu.setText(kullaniciDurumu);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}