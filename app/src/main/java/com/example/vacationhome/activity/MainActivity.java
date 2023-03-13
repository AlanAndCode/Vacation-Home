package com.example.vacationhome.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.vacationhome.model.Filtro;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterAd.OnClick {
    private final int REQUEST_FILTRO = 100;
    private RecyclerView rv_ad;
private ImageButton bt_menu;
private AdapterAd adapterAd;
    private TextView text_info;
    private ProgressBar progressBar;
    private List<Ad> anuncioList = new ArrayList<>();

    private Filtro filtro;

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
    private void recuperaAnunciosFiltro() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("Anuncios_publicos");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                anuncioList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Ad ad = snap.getValue(Ad.class);

                        int bedroom = Integer.parseInt(ad.getBedrooms());
                        int bathroom = Integer.parseInt(ad.getBathroom());
                        int garage = Integer.parseInt(ad.getGarage());

                        if (bedroom >= filtro.getQtdBedroom() &&
                                bathroom >= filtro.getQtdBathroom() &&
                                garage >= filtro.getQtdGarage()) {
                            anuncioList.add(ad);
                        }

                    }
                }

                if (anuncioList.size() == 0) {
                    text_info.setText("Nenhum anúncio encontrado.");
                } else {
                    text_info.setText("");
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

                if(menuItem.getItemId() == R.id.menu_filter){
                    Intent intent = new Intent(this, FilterActivity.class);
                    intent.putExtra("filtro", filtro);
                    startActivityForResult(intent, REQUEST_FILTRO);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILTRO) {
                filtro = (Filtro) data.getSerializableExtra("filtro");
                recuperaAnunciosFiltro();
            }
        }else{
            recuperarAnuncios();
        }
    }

    @Override
    public void OnClickListener(Ad ad) {
        Intent intent = new Intent(this, DetailAdActivity.class);
        intent.putExtra("ad", ad);
        startActivity(intent);
    }
}