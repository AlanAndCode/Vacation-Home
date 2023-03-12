package com.example.vacationhome.model;

import com.example.vacationhome.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Ad implements Serializable {

    private String id;
    private String idUser;
    private String title;
    private String description;
    private String Bedrooms;
    private String Bathroom;
    private String garage;
    private boolean status;
    private String urlImage;

    public Ad() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }


    public void save(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("Anuncios")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        reference.setValue(this);

        DatabaseReference adPublic = FirebaseHelper.getDatabaseReference()
                .child("Anuncios_publicos")
                .child(this.getId());
        adPublic.setValue(this);


    }
    public void deleteAD(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("Anuncios")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        reference.removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                StorageReference storageReference = FirebaseHelper.getStorageReference()
                        .child("imagens")
                        .child("anuncios")
                        .child(this.getId() + ".jpeg");
                storageReference.delete();
            }
        });

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBedrooms() {
        return Bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        Bedrooms = bedrooms;
    }

    public String getBathroom() {
        return Bathroom;
    }

    public void setBathroom(String bathroom) {
        Bathroom = bathroom;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
