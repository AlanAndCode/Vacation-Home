package com.example.vacationhome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.vacationhome.R;
import com.example.vacationhome.model.Filtro;

public class FilterActivity extends AppCompatActivity {

    private TextView text_Bedroom;
    private TextView text_Bathroom;
    private TextView text_garage;

    private SeekBar sb_Bedroom;
    private SeekBar sb_garage;
    private SeekBar sb_Bathroom;

    private int qtd_bedroom;
    private int qtd_bathroom;
    private int qtd_garage;

    private Filtro filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            filter = (Filtro) bundle.getSerializable("filter");
            if(filter != null){
                configFiltros();
            }
        }

        configCliques();

        configSb();

    }
    private void configFiltros(){
        sb_Bedroom.setProgress(filter.getQtdBedroom());
        sb_Bathroom.setProgress(filter.getQtdBathroom());
        sb_garage.setProgress(filter.getQtdGarage());

        text_Bedroom.setText(filter.getQtdBathroom() + " quartos ou mais");
        text_Bathroom.setText(filter.getQtdBathroom() + " banheiros ou mais");
        text_garage.setText(filter.getQtdGarage() + " garagem ou mais");

        qtd_bedroom = filter.getQtdBedroom();
        qtd_bathroom = filter.getQtdBathroom();
        qtd_garage = filter.getQtdGarage();
    }

    public void filtrar(View view){

        if(filter == null) filter = new Filtro();


        if(qtd_bedroom >= 0) filter.setQtdBedroom(qtd_bedroom);
        if(qtd_bathroom >= 0) filter.setQtdBathroom(qtd_bathroom);
        if(qtd_garage >= 0) filter.setQtdGarage(qtd_garage);

        if(qtd_bedroom  >= 0 || qtd_bathroom >= 0 || qtd_garage  >= 0){
            Intent intent = new Intent();
            intent.putExtra("filtro", filter);
            setResult(RESULT_OK, intent);
        }

        finish();

    }


    private void configSb(){
        sb_Bedroom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text_Bedroom.setText(i + " Bedrooms or more");
                qtd_bedroom = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_Bathroom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text_Bathroom.setText(i + " Bathrooms or more");
                qtd_bathroom = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_garage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text_garage.setText(i + " garage or more");
                qtd_garage = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void limparfilter(View view){
        qtd_bedroom = 0;
        qtd_bathroom = 0;
        qtd_garage = 0;

        finish();
    }

    private void configCliques(){
        findViewById(R.id.bt_back).setOnClickListener(view -> finish());
    }

    private void iniciaComponentes(){
        TextView text_titlele = findViewById(R.id.text_title);
        text_titlele.setText("Filter");

        text_Bedroom = findViewById(R.id.text_Bedroom);
        text_Bathroom = findViewById(R.id.text_Bathroom);
        text_garage = findViewById(R.id.text_garage);

        sb_Bedroom = findViewById(R.id.sb_Bedroom);
        sb_Bathroom = findViewById(R.id.sb_Bathroom);
        sb_garage = findViewById(R.id.sb_garage);
    }

}