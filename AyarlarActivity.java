package com.example.wpchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AyarlarActivity extends AppCompatActivity {

    private Button HesapAyarlariniGuncelleme;
    private EditText kullaniciAdi,kullaniciDurumu;
    private CircleImageView kullaniciProfilResmi;

    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference veriYolu;

    private String mevcutKullaniciId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        //Firebase
        mYetki=FirebaseAuth.getInstance();
        veriYolu= FirebaseDatabase.getInstance().getReference();

        mevcutKullaniciId=mYetki.getCurrentUser().getUid();  //mevcut kullanıcının mevcut uid sini alıp değişkene aktardım



        //Kontrol Tanımlamaları
        HesapAyarlariniGuncelleme=findViewById(R.id.ayarları_guncelle_buttonu);
        kullaniciAdi=findViewById(R.id.kullanici_adi_ayarla);
        kullaniciDurumu=findViewById(R.id.profil_durumu_ayarla);
        kullaniciProfilResmi=findViewById(R.id.profil_resmi_ayarla);

        HesapAyarlariniGuncelleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AyarlariGuncelle();
            }
        });

        kullaniciAdi.setVisibility(View.INVISIBLE);

        KullaniciBilgisiAl();



    }

    private void KullaniciBilgisiAl() {

        veriYolu.child("Kullanicilar").child(mevcutKullaniciId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if ((snapshot.exists())&&(snapshot.hasChild("ad")&&(snapshot.hasChild("resim"))))
                        {
                            String kullaniciAdiniAl = snapshot.child("ad").getValue().toString();
                            String kullaniciDurumunuAl = snapshot.child("durum").getValue().toString();
                            String kullaniciResminiAl = snapshot.child("resim").getValue().toString();

                            kullaniciAdi.setText(kullaniciAdiniAl);
                            kullaniciDurumu.setText(kullaniciDurumunuAl);

                        }
                        else if ((snapshot.exists())&&(snapshot.hasChild("ad")))  //resim yoksa
                        {
                            String kullaniciAdiniAl = snapshot.child("ad").getValue().toString();
                            String kullaniciDurumunuAl = snapshot.child("durum").getValue().toString();


                            kullaniciAdi.setText(kullaniciAdiniAl);
                            kullaniciDurumu.setText(kullaniciDurumunuAl);

                        }

                        else
                        {
                            kullaniciAdi.setVisibility(View.VISIBLE);
                            Toast.makeText(AyarlarActivity.this, "Lütfen profil bilgilerinizi ayarlayın!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void AyarlariGuncelle() {

        String kullaniciAdiAyarla = kullaniciAdi.getText().toString();
        String kullaniciDurumuAyarla = kullaniciDurumu.getText().toString();

        if (TextUtils.isEmpty(kullaniciAdiAyarla))
        {
            Toast.makeText(this, "Lütfen adınızı yazın!", Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(kullaniciDurumuAyarla))
        {
            Toast.makeText(this, "Lütfen durumunuzu yazın!", Toast.LENGTH_LONG).show();
        }

        else
        {
            HashMap<String,String> profilHaritasi = new HashMap<>();
            profilHaritasi.put("uid", mevcutKullaniciId);
            profilHaritasi.put("ad", kullaniciAdiAyarla);
            profilHaritasi.put("durum", kullaniciDurumuAyarla);

            veriYolu.child("Kullanicilar").child(mevcutKullaniciId).setValue(profilHaritasi)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(AyarlarActivity.this, "profiliniz başarılı bir şekilde güncellendi...", Toast.LENGTH_SHORT).show();
                                Intent anaSayfa = new Intent(AyarlarActivity.this,MainActivity.class);
                                anaSayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(anaSayfa);
                                finish();
                            }
                            else
                            {
                                String mesaj = task.getException().toString();
                                Toast.makeText(AyarlarActivity.this, "Hata: "+mesaj, Toast.LENGTH_SHORT).show(); //mesajı gösterir
                            }

                        }
                    });

        }

    }
}