package com.example.wpchatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Kisiler;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KisilerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KisilerFragment extends Fragment {

    private View KisilerView;

    private RecyclerView kisilerListem;

    //Firebase
    private DatabaseReference SohbetlerYolu,KullanıcılarYolu;
    private FirebaseAuth mYetki;


    private String aktifKullaniciId;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KisilerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KisilerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KisilerFragment newInstance(String param1, String param2) {
        KisilerFragment fragment = new KisilerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        KisilerView= inflater.inflate(R.layout.fragment_kisiler, container, false);

        //Recycler
        kisilerListem= KisilerView.findViewById(R.id.kisiler_listesi);
        kisilerListem.setLayoutManager(new LinearLayoutManager(getContext()));

        //Firebase

        mYetki=FirebaseAuth.getInstance();

        aktifKullaniciId=mYetki.getCurrentUser().getUid();
        SohbetlerYolu= FirebaseDatabase.getInstance().getReference().child("Sohbetler").child(aktifKullaniciId);
        KullanıcılarYolu= FirebaseDatabase.getInstance().getReference().child("Kullanicilar");


        return KisilerView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions secenekler = new FirebaseRecyclerOptions.Builder<Kisiler>()
                .setQuery(SohbetlerYolu,Kisiler.class)
                .build();

        //Adapter
        FirebaseRecyclerAdapter<Kisiler,KisilerViewHolder>adapter = new FirebaseRecyclerAdapter<Kisiler, KisilerViewHolder>(secenekler) {
            @Override
            protected void onBindViewHolder(@NonNull KisilerViewHolder holder, int position, @NonNull Kisiler model) {

                String tıklananSatırKullaniciIdsi = getRef(position).getKey();

                KullanıcılarYolu.child(tıklananSatırKullaniciIdsi).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild("resim"))
                        {
                            //Verileri Firebeaseden çekme
                            String profilResmi = snapshot.child("resim").getValue().toString();
                            String kullaniciAdi =snapshot.child("ad").getValue().toString();
                            String kullaniciDurumu =snapshot.child("durum").getValue().toString();

                            //Kontrollere veri aktarımı
                            holder.kullaniciAdi.setText(kullaniciAdi);
                            holder.kullaniciDurumu.setText(kullaniciDurumu);

                        }

                        else
                        {
                            //Verileri Firebeaseden çekme
                            String kullaniciAdi =snapshot.child("ad").getValue().toString();
                            String kullaniciDurumu =snapshot.child("durum").getValue().toString();

                            //Kontrollere veri aktarımı
                            holder.kullaniciAdi.setText(kullaniciAdi);
                            holder.kullaniciDurumu.setText(kullaniciDurumu);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public KisilerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kullanici_gosterme_layout,parent,false);
                KisilerViewHolder viewHolder = new KisilerViewHolder(view);

                return viewHolder;
            }
        };

        kisilerListem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    public static class KisilerViewHolder extends RecyclerView.ViewHolder{

        TextView kullaniciAdi,kullaniciDurumu;
        CircleImageView profilResmi;


        public KisilerViewHolder(@NonNull View itemView) {
            super(itemView);

            kullaniciAdi=itemView.findViewById(R.id.kullanici_profil_adi);
            kullaniciDurumu=itemView.findViewById(R.id.kullanici_durumu);
            profilResmi=itemView.findViewById(R.id.kullanicilar_profil_resmi);
        }
    }
}