package com.example.vacationhome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vacationhome.R;
import com.example.vacationhome.model.Ad;
import com.example.vacationhome.model.User;
import com.squareup.picasso.Picasso;

public class DetailAdActivity extends AppCompatActivity {
    private ImageView edit_image;
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
        ad = (Ad) getIntent().getSerializableExtra("Ad");
        configDados();
    }

    private void configDados(){

        if(ad != null){
            Picasso.get().load(ad.getUrlImage()).into(edit_image);
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
        text_title_ad = findViewById(R.id. text_title_ad);
        text_description = findViewById(R.id.text_description);
        edit_Bedrooms = findViewById(R.id.edit_Bedrooms);
        edit_Bathroom = findViewById(R.id.edit_Bathroom);
        edit_garage = findViewById(R.id.edit_Garage);
        edit_image = findViewById(R.id.edit_image);

        TextView text_title = findViewById(R.id.text_title);
        text_title.setText("Detail your ad");


    }
}