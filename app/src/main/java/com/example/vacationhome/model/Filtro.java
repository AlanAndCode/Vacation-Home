package com.example.vacationhome.model;

import java.io.Serializable;

public class Filtro implements Serializable {

    private int qtdBedroom;
    private int qtdBathroom;
    private int qtdGarage;

    public int getQtdBedroom() {
        return qtdBedroom;
    }

    public void setQtdBedroom(int qtdBedroom) {
        this.qtdBedroom = qtdBedroom;
    }

    public int getQtdBathroom() {
        return qtdBathroom;
    }

    public void setQtdBathroom(int qtdBathroom) {
        this.qtdBathroom = qtdBathroom;
    }

    public int getQtdGarage() {
        return qtdGarage;
    }

    public void setQtdGarage(int qtdGarage) {
        this.qtdGarage = qtdGarage;
    }
}
