package com.example.wpchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Kisiler;
import de.hdodenhof.circleimageview.CircleImageView;

public class ArkadasBulActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView ArkadasBulRecyclerListesi;

    //Firebase
    private DatabaseReference KullaniciYolu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arkadas_bul);

        //Recycler
        ArkadasBulRecyclerListesi =findViewById(R.id.arkadas_bul_recycler_listesi);
        ArkadasBulRecyclerListesi.setLayoutManager(new LinearLayoutManager(this));

        //Toolbar
        mToolbar=findViewById(R.id.arkadas_bul_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Gösterim ayarları
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Arkadaş Bul");

        //Firebase Tanımlama
        KullaniciYolu= FirebaseDatabase.getInstance().getReference().child("Kullanicilar");




    }

    @Override
    protected void onStart() {
        super.onStart();

        //Başladığında


        //Sorgu-Seçenekleri
        FirebaseRecyclerOptions<Kisiler> secenekler =
                new FirebaseRecyclerOptions.Builder<Kisiler>()
                        .setQuery(KullaniciYolu, Kisiler.class)
                        .build();



        FirebaseRecyclerAdapter<Kisiler,ArkadasBulViewHolder>adapter =new FirebaseRecyclerAdapter<Kisiler, ArkadasBulViewHolder>(secenekler) {
            @Override
            protected void onBindViewHolder(@NonNull ArkadasBulViewHolder holder, int position, @NonNull Kisiler model) { //pozisyon hangi satırdaysa bilgileri getiriyor

                holder.kullaniciAdi.setText(model.getAd());
                holder.kullaniciDurumu.setText(model.getDurum());

                //Tıklandığında
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String tıklanan_kullanici_Id_goster = getRef(holder.getAdapterPosition()).getKey();

                        Intent profilAkitivte = new Intent(ArkadasBulActivity.this,ProfilActivity.class);
                        profilAkitivte.putExtra("tıklanan_kullanici_Id_goster",tıklanan_kullanici_Id_goster);

                        startActivity(profilAkitivte);


                    }
                });







            }

            @NonNull
            @Override
            public ArkadasBulViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kullanici_gosterme_layout,parent,false);
                ArkadasBulViewHolder viewHolder = new ArkadasBulViewHolder(view);

                return viewHolder;
            }
        };

        ArkadasBulRecyclerListesi.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();






    }

    public static class ArkadasBulViewHolder extends RecyclerView.ViewHolder
    {
        TextView kullaniciAdi,kullaniciDurumu;
        CircleImageView profilResmi;

        public ArkadasBulViewHolder(@NonNull View itemView) {
            super(itemView);

            //Tanımlamalar
            kullaniciAdi=itemView.findViewById(R.id.kullanici_profil_adi);
            kullaniciDurumu=itemView.findViewById(R.id.kullanici_durumu);
            profilResmi=itemView.findViewById(R.id.kullanicilar_profil_resmi);
        }
    }
}