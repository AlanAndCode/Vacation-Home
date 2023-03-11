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
import android.widget.TextView;
import android.widget.Toast;

import com.example.vacationhome.R;
import com.example.vacationhome.helper.FirebaseHelper;
import com.example.vacationhome.model.Anuncio;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

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

    private ImageView edit_image;
    private String caminhoImagem;
    private Bitmap imagem;

    private Anuncio anuncio;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ad);

        iniciaComponentes();

        configCliques();
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

    private void SaveImage(){
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("anuncios")
                .child(anuncio.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            String urlImagem = task.getResult().toString();

           anuncio.setUrlImage(task.getResult().toString());
            anuncio.SaveAD();

            finish();

        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

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



                       if(anuncio == null) anuncio = new Anuncio();
                        anuncio.setTitle(title);
                        anuncio.setDescription(description);
                        anuncio.setBedrooms(Bedrooms);
                        anuncio.setBathroom(Bathroom);
                        anuncio.setGarage(garage);
                        anuncio.setStatus(edit_check.isChecked());

                        if(caminhoImagem == null){
                            Toast.makeText(this, "Select any Image", Toast.LENGTH_SHORT).show();

                        }else{
                            SaveImage();
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
        edit_image = findViewById(R.id.edit_image);
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