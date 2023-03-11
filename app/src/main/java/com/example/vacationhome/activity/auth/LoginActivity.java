package com.example.vacationhome.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vacationhome.R;
import com.example.vacationhome.activity.MainActivity;
import com.example.vacationhome.helper.FirebaseHelper;
import com.example.vacationhome.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_email;
    private EditText edit_pass;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configCliques();
        iniciaComponentes();
    }

    private void configCliques(){
        findViewById(R.id.text_CC).setOnClickListener(view ->
                startActivity(new Intent(this, CCActivity.class)));
        findViewById(R.id.text_FP).setOnClickListener(view ->
                startActivity(new Intent(this, ForgotPassActivity.class)));
    }

    public void validaDados(View view){
        String email = edit_email.getText().toString();
        String pass = edit_pass.getText().toString();


            if (!email.isEmpty()) {
                    if (!pass.isEmpty()) {
                        progressBar.setVisibility(View.VISIBLE);
                        User user = new User();
                        user.setEmail(email);
                        user.setPass(pass);



                        logar(email, pass);


                    } else {
                        edit_pass.requestFocus();
                        edit_pass.setError("Write your password");
                    }
            } else {
                edit_email.requestFocus();
                edit_email.setError("Write your email");
            }


    }


    private void logar(String email,  String pass){
        FirebaseHelper.getAuth().signInWithEmailAndPassword(
                email, pass
        ).addOnCompleteListener(task -> {

            if(task.isSuccessful()){

                finish();
                startActivity(new Intent(this, MainActivity.class));
            }else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void iniciaComponentes(){
edit_email = findViewById(R.id.edit_email);
edit_pass = findViewById(R.id.edit_pass);
        progressBar = findViewById(R.id.progressBar);
    }

}