package com.example.wpchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private SekmeErisimAdapter mysekmeErisimAdapter;

    //Firebase
    private FirebaseUser mevcutKullanici;
    private FirebaseAuth mYetki;
    private DatabaseReference kullanicilarReference;


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

    //Firebase
        mYetki=FirebaseAuth.getInstance();
        mevcutKullanici=mYetki.getCurrentUser(); //mYetkinin mevcut kullanıcısını al
        kullanicilarReference= FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mevcutKullanici ==null)  //mevcut kullanıcı boşsa bizi logine gönder
        {
            KullaniciyiLoginActivityeGonder();
        }

        else
        {
            KullanicininVarliginiDogrula();
        }
    }

    private void KullanicininVarliginiDogrula() {

        String mevcutKullaniciId = mYetki.getCurrentUser().getUid();

        kullanicilarReference.child("Kullanicilar").child(mevcutKullaniciId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            if ((snapshot.child("ad").exists()))
            {
                Toast.makeText(MainActivity.this, "Hoşgeldiniz...", Toast.LENGTH_LONG).show();
            }

            else
            {
                Intent ayarlar = new Intent(MainActivity.this,AyarlarActivity.class);
                ayarlar.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  //geri tuşuna bastığında anasayfaya gitmesin ayarları yapsın
                startActivity(ayarlar);
                finish();
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void KullaniciyiLoginActivityeGonder() //oluşturudğumz method
    {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //yeni görev ve görev temizleme(geriye bastığında anasayfaya geri dönmesin giriş yapsın)
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.secenekler_menu,menu); //menu mü bağlamış oldum
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) //seçeneklerdeki öğeler seçildiğinde
    {
        super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.ana_arkadas_bulma_secenegi)
        {

        }

        if (item.getItemId()==R.id.ana_ayarlar_secenegi)
        {
            Intent ayar=new Intent(MainActivity.this,AyarlarActivity.class);
            startActivity(ayar);
        }

        if (item.getItemId()==R.id.ana_cikis_secenegi)
        {
            mYetki.signOut();
            Intent giris=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(giris);
        }

        if (item.getItemId()==R.id.ana_grup_olustur_secenegi)
        {
            yeniGrupTalebi();
        }

        return true;
    }

    private void yeniGrupTalebi() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Grup Adı Girin");

        final EditText grupAdiAlani = new EditText(MainActivity.this);
        grupAdiAlani.setHint("Örnek:Sohbet Masası,Kafadarlar");
        builder.setView(grupAdiAlani);

        builder.setPositiveButton("Oluştur", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String grupAdi = grupAdiAlani.getText().toString();

                if (TextUtils.isEmpty(grupAdi)) //grup adı boşsa uyarı versin
                {
                    Toast.makeText(MainActivity.this, "Grup adı boş bırakılamaz!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    YeniGrupOlustur(grupAdi);  //stringi ilk değişken olarak yeni grup oluştura ekle
                }

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });


        builder.show();

    }

    private void YeniGrupOlustur(String grupAdi) {

        kullanicilarReference.child("Gruplar").child(grupAdi).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())  //görev başarıyla yapıldıysa
                        {
                            Toast.makeText(MainActivity.this, grupAdi+" adlı grup başarıyla oluşturuldu.", Toast.LENGTH_LONG).show();

                        }

                    }
                });


    }
}