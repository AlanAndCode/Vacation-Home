package com.example.vacationhome.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vacationhome.R;
import com.example.vacationhome.helper.FirebaseHelper;
import com.example.vacationhome.model.Ad;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


public class FormAdActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERIA = 100;
    private EditText edit_title;
    private EditText edit_description;
    private EditText edit_Bedrooms;
    private EditText edit_Bathroom;
    private EditText edit_garage;
    private CheckBox edit_check;

    private ProgressBar progressBar;

    private ImageView edit_image;
    private String caminhoImagem;
    private Bitmap imagem;

    private Ad ad;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ad);

         iniciaComponentes();

         Bundle bundle = getIntent().getExtras();
         if(bundle != null){
             ad = (Ad) bundle.getSerializable("Ad");
             configDados();
         }

         configCliques();
    }

    private void configDados(){
        Picasso.get().load(ad.getUrlImage()).into(edit_image);
        edit_title.setText(ad.getTitle());
        edit_description.setText(ad.getDescription());
        edit_Bedrooms.setText(ad.getBedrooms());
        edit_Bathroom.setText(ad.getBathroom());
        edit_garage.setText(ad.getGarage());
        edit_check.setChecked(ad.isStatus());
    }

    private void configCliques(){
        findViewById(R.id.bt_save).setOnClickListener(view -> validaDados());
        findViewById(R.id.bt_back).setOnClickListener(view -> finish());
    }

    public void verifYPermissionGallery(View view) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirgaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAdActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});

    }
    private void showDialogPermissao(PermissionListener permissionListener, String[] permissions){
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão Denied")
                .setDeniedMessage("Try again and get Permission for System")
                .setDeniedCloseButtonText("nao")
                .setGotoSettingButtonText("sim")
                .setPermissions(permissions)
                .check();
    }

    private void abrirgaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERIA);
    }

    private void saveImage(){

         progressBar.setVisibility(View.VISIBLE);

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("anuncios")
                .child(ad.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            String urlImage = task.getResult().toString();

           ad.setUrlImage(task.getResult().toString());
            ad.save();

            finish();

        })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
    private void validaDados(){


        String title = edit_title.getText().toString();
        String description = edit_description.getText().toString();
        String Bedrooms = edit_Bedrooms.getText().toString();
        String Bathroom = edit_Bathroom.getText().toString();
        String garage = edit_garage.getText().toString();

    if(!title.isEmpty()) {
        if (!description.isEmpty()) {
            if (!Bedrooms.isEmpty()) {
                if (!Bathroom.isEmpty()) {
                    if (!garage.isEmpty()) {





                       if(ad == null) ad = new Ad();
                        ad.setIdUser(FirebaseHelper.getIdFirebase());
                        ad.setTitle(title);
                        ad.setDescription(description);
                        ad.setBedrooms(Bedrooms);
                        ad.setBathroom(Bathroom);
                        ad.setGarage(garage);
                        ad.setStatus(edit_check.isChecked());

                        if(caminhoImagem != null) {
                        saveImage();
                        }else{
                            if(ad.getUrlImage() != null){
                                ad.save();
                            }else {
                                Toast.makeText(this, "Select any Image", Toast.LENGTH_SHORT).show();
                            }
                        }

                } else {
                        edit_garage.requestFocus();
                        edit_garage.setError("mandatory information");
                    }
                }else {
                    edit_Bathroom.requestFocus();
                    edit_Bathroom.setError("mandatory information");
                }
            }else {
                edit_Bedrooms.requestFocus();
                edit_Bedrooms.setError("mandatory information");
            }
        } else {
            edit_description.requestFocus();
            edit_description.setError("mandatory information");
        }
    }else{
        edit_title.requestFocus();
        edit_title.setError("mandatory information");
    }



}
    private void iniciaComponentes(){
        edit_title = findViewById(R.id.edit_title);
        edit_description = findViewById(R.id.edit_description);
        edit_Bedrooms = findViewById(R.id.edit_Bedrooms);
        edit_Bathroom = findViewById(R.id.edit_Bathroom);
        edit_garage = findViewById(R.id.edit_Garage);
        edit_check = findViewById(R.id.edit_check);
        edit_image = findViewById(R.id.image_ad);
        progressBar = findViewById(R.id.progressBar);

        TextView text_title = findViewById(R.id.text_title);
        text_title.setText("Create your ad");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_GALLERIA){
                Uri localImageSelected = data.getData();
                caminhoImagem = localImageSelected.toString();

                if(Build.VERSION.SDK_INT < 28){
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), localImageSelected);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), localImageSelected);
                    try {
                        imagem = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                edit_image.setImageBitmap(imagem);

            }
        }

    }

}