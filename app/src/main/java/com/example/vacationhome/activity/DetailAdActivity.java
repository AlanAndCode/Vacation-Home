package com.example.vacationhome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vacationhome.R;
import com.example.vacationhome.helper.FirebaseHelper;
import com.example.vacationhome.model.Ad;
import com.example.vacationhome.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailAdActivity extends AppCompatActivity {
    private ImageView image_ad;
    private TextView text_title_ad;
    private TextView text_description;
    private EditText edit_Bedrooms;
    private EditText edit_Bathroom;
    private EditText edit_garage;

    private Ad ad;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ad);

        iniciaComponentes();
        ad = (Ad) getIntent().getSerializableExtra("ad");
        recuperaAnunciante();
        configDados();
    }
    public void ligar(View view){
        if(user != null){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.getPhone()));
            startActivity(intent);
        }else {
            Toast.makeText(this, "Carregando informações, aguarde...", Toast.LENGTH_SHORT).show();
        }
    }

    private void recuperaAnunciante(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(ad.getIdUser());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados(){
        if(ad != null){
            Picasso.get().load(ad.getUrlImage()).into(image_ad);
            text_title_ad.setText(ad.getTitle());
            text_description.setText(ad.getDescription());
            edit_Bedrooms.setText(ad.getBedrooms());
            edit_Bathroom.setText(ad.getBathroom());
            edit_garage.setText(ad.getGarage());
        }else {
            Toast.makeText(this, "Não foi possível recuperar as informações.", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciaComponentes(){
        TextView text_title = findViewById(R.id.text_title);
        text_title.setText("Detail your ad");

        text_title_ad = findViewById(R.id. text_title_ad);
        text_description = findViewById(R.id.text_description);
        edit_Bedrooms = findViewById(R.id.edit_Bedrooms);
        edit_Bathroom = findViewById(R.id.edit_Bathroom);
        edit_garage = findViewById(R.id.edit_garage);
        image_ad = findViewById(R.id.image_ad);


    }
}