package com.example.wpchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KayitActivity extends AppCompatActivity {

    private Button KayitOlusturmaButtonu;
    private EditText KullaniciMail, KullaniciSifre;
    private TextView ZatenHesabımVar;

    //Firebase
    private DatabaseReference kokReference;
    private FirebaseAuth mYetki;

    private ProgressDialog yukleniyorDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate methodu Activity ilk açıldığında gerçekleşen olayları içinde barındırır
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        //Firebase
        mYetki=FirebaseAuth.getInstance(); //else den örnek alıyor
        kokReference= FirebaseDatabase.getInstance().getReference(); //bundan örnek al referans olarak


        //Kontrol Tanımlamaları

        KayitOlusturmaButtonu=findViewById(R.id.kayit_butonu);

        KullaniciMail=findViewById(R.id.kayit_email);
        KullaniciSifre=findViewById(R.id.kayit_sifre);

        ZatenHesabımVar=findViewById(R.id.zaten_hesap_var);

        yukleniyorDialog=new ProgressDialog(this);

        ZatenHesabımVar.setOnClickListener(new View.OnClickListener() { //zaten hesabım var a tıklandığında bunu yapsın
            @Override
            public void onClick(View view) {  // javascript fonksiyonunu bir dom element e tıklanarak çalıştırılmasını sağlar
                Intent loginActivityIntent=new Intent(KayitActivity.this,LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });

        KayitOlusturmaButtonu.setOnClickListener(new View.OnClickListener() { //kayıt oluruma butonuna tıklandığında
            @Override
            public void onClick(View view) {

                YeniHesapOlustur();
            }
        });
    }

    private void YeniHesapOlustur()
    {
        String email = KullaniciMail.getText().toString(); // string değişkeninde mail ve şifreye göre kayıt olur
        String sifre = KullaniciSifre.getText().toString();


        if (TextUtils.isEmpty(email)) //email metin öğerleri boşsa
        {
            Toast.makeText(this, "Email boş olamaz...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(sifre)) //email metin öğerleri boşsa
        {
            Toast.makeText(this, "Şifre boş olamaz...", Toast.LENGTH_SHORT).show();
        }
        else //Kullanıcı ikisinide doldurduysa Kayıt yapabilir
        {
          yukleniyorDialog.setTitle("yeni hesap oluşturuluyor"); //BAŞLIK
          yukleniyorDialog.setMessage("Lütfen bekleyin..."); //MESAJ
          yukleniyorDialog.setCanceledOnTouchOutside(true); //Dışarıya tıklandığında iptal et
          yukleniyorDialog.show(); //Göster


         mYetki.createUserWithEmailAndPassword(email,sifre)  //şifre ve email ile oluşsun
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() { //onComp... tamalandığında
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {  //Görev tamamlandığında çağrılır.

                 if (task.isSuccessful()) //Görev başarılıysa bana uyarı versin
                 {
                     String mevcutKullaniciId=mYetki.getCurrentUser().getUid();  //Firebase deki mevcut kullanicinin Uid al
                     kokReference.child("Kullanicilar").child(mevcutKullaniciId).setValue(""); //Firebase de alan oluşturacak mevcutkullanci onu boş olarak ayarlıycak


                     Intent anasayfa=new Intent(KayitActivity.this,MainActivity.class);
                     anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Geri tuşuna bastığımda kayıt sayfasına gitmemesi için
                     startActivity(anasayfa); //görev başarılıysa anasayfaya göndericek
                     finish();

                     Toast.makeText(KayitActivity.this, "Yeni hesap başarılı bir şekilde oluştu...", Toast.LENGTH_SHORT).show();
                     yukleniyorDialog.dismiss(); //loading bar sönsün kapansın
                 }
                 else
                 {
                     String mesaj=task.getException().toString();
                     Toast.makeText(KayitActivity.this, "Hata: "+ mesaj+" Bilgilerinizi Kontrol edin", Toast.LENGTH_SHORT).show();
                     yukleniyorDialog.dismiss(); //yükleniyor bar sönsün
                 }

                     }
                 });
        }

    }
}