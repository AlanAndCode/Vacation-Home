package com.example.vacationhome.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.vacationhome.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.text_CC).setOnClickListener(view ->
                startActivity(new Intent(this, CCActivity.class)));
    }
}