package com.example.vacationhome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vacationhome.R;
import com.example.vacationhome.activity.auth.LoginActivity;
import com.example.vacationhome.adapter.AdapterAd;
import com.example.vacationhome.helper.FirebaseHelper;
import com.example.vacationhome.model.Ad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterAd.OnClick {

    private RecyclerView rv_ad;
private ImageButton bt_menu;
private AdapterAd adapterAd;
    private TextView text_info;
    private ProgressBar progressBar;
    private List<Ad> anuncioList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponentes();
        configRv();
        configCliques();
        recuperarAnuncios();

    }



    private void configRv() {
        rv_ad.setLayoutManager(new LinearLayoutManager(this));
        rv_ad.setHasFixedSize(true);
        adapterAd = new AdapterAd(anuncioList, this);
        rv_ad.setAdapter(adapterAd);
    }

    private void recuperarAnuncios(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("Anuncios")
        .child(FirebaseHelper.getIdFirebase());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        anuncioList.clear();
                        if(snapshot.exists()){

                        for (DataSnapshot snap : snapshot.getChildren()){
                        Ad anuncio = snap.getValue(Ad.class);
                        anuncioList.add(anuncio);
}
                            text_info.setText("");
                        }else {
                            text_info.setText("no registered ad");
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
private void configCliques(){
        bt_menu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, bt_menu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if(menuItem.getItemId() == R.id.bt_menu){
startActivity(new Intent(this, FilterActivity.class));
                }else if((menuItem.getItemId() == R.id.menu_my_ad)){
                    if(FirebaseHelper.getAutenticado()){
                        startActivity(new Intent(this, MenuMyAdActivity.class));
                    }else {
                        showDialogLogin();
                    }
                }else {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MyAccountActivity.class));
                    } else {
                        showDialogLogin();
                    }
                }

                return true;
            });
popupMenu.show();
        });
}

private void showDialogLogin(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Autentication");
    builder.setMessage("you are not yet logged into the APP");
    builder.setCancelable(false);
    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
    builder.setPositiveButton("yes", (dialog, which) -> {
       startActivity(new Intent(this, LoginActivity.class));
    });

    AlertDialog dialog = builder.create();
    dialog.show();
}

private void iniciaComponentes(){
    bt_menu = findViewById(R.id.bt_menu);
    rv_ad= findViewById(R.id.rv_ad);
    text_info = findViewById(R.id.text_info);
    progressBar = findViewById(R.id.progressBar);
}

    @Override
    public void OnClickListener(Ad ad) {
        Intent intent = new Intent(this, DetailAdActivity.class);
        intent.putExtra("ad", ad);
        startActivity(intent);
    }
}