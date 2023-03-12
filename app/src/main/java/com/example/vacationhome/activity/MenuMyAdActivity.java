package com.example.vacationhome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vacationhome.R;
import com.example.vacationhome.adapter.AdapterAd;
import com.example.vacationhome.helper.FirebaseHelper;
import com.example.vacationhome.model.Ad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuMyAdActivity extends AppCompatActivity implements AdapterAd.OnClick {
    private List<Ad> anuncioList = new ArrayList<>();
private ProgressBar progressBar;
private TextView text_load;
private AdapterAd adapterAd;

    private SwipeableRecyclerView rv_ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_my_ad);


        iniciaComponentes();

        configRv();
        configCliques();
    }
    @Override
    protected void onStart() {
        super.onStart();

        recuperarAnuncios();
    }
    private void configRv(){
       rv_ad.setLayoutManager(new LinearLayoutManager(this));
       rv_ad.setHasFixedSize(true);
        adapterAd = new AdapterAd(anuncioList, this);
       rv_ad.setAdapter(adapterAd);


        rv_ad.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                showDialogDelete(position);
            }
        });

    }

    private void showDialogDelete(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete ad");
        builder.setMessage("Press yes to confirm or no to cancel.");
        builder.setNegativeButton("No", ((dialogInterface, i) -> {
            dialogInterface.dismiss();
            adapterAd.notifyDataSetChanged();
        }));
        builder.setPositiveButton("Yes", ((dialogInterface, i) -> {
            anuncioList.get(pos).deleteAD();
            adapterAd.notifyItemRemoved(pos);
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configCliques(){
        findViewById(R.id.bt_add).setOnClickListener(view ->
                startActivity(new Intent(this, FormAdActivity.class)));
        findViewById(R.id.bt_back).setOnClickListener(view -> finish());
    }
    private void recuperarAnuncios(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("Anuncios")
                .child(FirebaseHelper.getIdFirebase());
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                anuncioList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        Ad anuncio = snap.getValue(Ad.class);
                        anuncioList.add(anuncio);
                    }
                    text_load.setText("");
                }else{
                    text_load.setText("no ad registered");
                }
progressBar.setVisibility(View.GONE);
                Collections.reverse(anuncioList);
                adapterAd.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciaComponentes(){
        TextView text_title = findViewById(R.id.text_title);
        text_title.setText("My Ads");

        progressBar = findViewById(R.id.progressBar);
        text_load = findViewById(R.id.text_load);
        rv_ad = findViewById(R.id.rv_ad);
    }


    @Override
    public void OnClickListener(Ad ad) {
        Intent intent = new Intent(this, FormAdActivity.class);
        intent.putExtra("Ad", ad);
        startActivity(intent);
    }
}