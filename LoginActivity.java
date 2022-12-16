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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button girisButonu ;
    private EditText KullaniciMail, KullaniciSifre;
    private TextView YeniHesapAlma, SifreUnutmaBaglanti;

    //Firebase

    private FirebaseAuth mYetki;

    //Progress
    ProgressDialog girisDialog;


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

        //Progress
        girisDialog= new ProgressDialog(this);

        //Firebase
        mYetki=FirebaseAuth.getInstance();



        YeniHesapAlma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent kayitAktivityIntent=new Intent(LoginActivity.this,KayitActivity.class);
                startActivity(kayitAktivityIntent);
            }
        });

        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                KullaniciyaGirisİzniVer(); //adlı method
            }
        });

    }

    private void KullaniciyaGirisİzniVer() // alt+enter ile oluşturduğum method
    {
      String email = KullaniciMail.getText().toString();
      String sifre = KullaniciSifre.getText().toString();

      if (TextUtils.isEmpty(email))  //email de herhangi bir text yoksa
      {
          Toast.makeText(this, "Email boş olamaz!", Toast.LENGTH_SHORT).show();
      }
        if (TextUtils.isEmpty(sifre))  //sifre de herhangi bir text yoksa
        {
            Toast.makeText(this, "Şifre boş olamaz!", Toast.LENGTH_SHORT).show();
        }

        else //bunları doğru ise giriş yapsın
        {
            //Progress
            girisDialog.setTitle("Giriş yapılıyor");
            girisDialog.setMessage("Lütfen bekleyin...");
            girisDialog.setCanceledOnTouchOutside(true); //dışarda yer olursa iptal eder
            girisDialog.show();

            //Giriş
           mYetki.signInWithEmailAndPassword(email,sifre)
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {

                           if (task.isSuccessful()) //eğer sign in görevi başarılıysa YÖNLENDİRME
                           {
                             Intent anaSayfa = new Intent(LoginActivity.this,MainActivity.class);
                             anaSayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //anasayfadan geri tuşuna bastığımız zaman logine gitmesin
                             finish();

                             startActivity(anaSayfa); //LoginActivity.thisi  alıcak Main.class a gonderecek
                               Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                               girisDialog.dismiss();
                           }

                           else
                           {
                               String mesaj = task.getException().toString();
                               Toast.makeText(LoginActivity.this, "Hata: "+mesaj+" Bilgileri kontrol edin", Toast.LENGTH_SHORT).show();
                               girisDialog.dismiss();
                           }

                       }
                   });
        }

    }


    private void KullaniciyiAnaAktivitiyeGonder()
    {
        Intent AnaAktiviteIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(AnaAktiviteIntent);
    }
}