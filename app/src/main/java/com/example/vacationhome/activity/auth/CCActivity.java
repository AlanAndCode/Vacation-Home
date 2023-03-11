package com.example.vacationhome.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vacationhome.R;
import com.example.vacationhome.activity.MainActivity;
import com.example.vacationhome.helper.FirebaseHelper;
import com.example.vacationhome.model.Usuario;

public class CCActivity extends AppCompatActivity {
private EditText edit_nome;
private EditText edit_email;
private EditText edit_pass;
private EditText edit_phone;

private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccactivity);

        configCliques();

        iniciaComponentes();
    }

    public void validaDados(View view){

        String nome = edit_nome.getText().toString();
        String email = edit_email.getText().toString();
        String pass = edit_pass.getText().toString();
        String phone = edit_phone.getText().toString();


        if(!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!phone.isEmpty()) {
                    if (!pass.isEmpty()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Usuario usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setPass(pass);
                        usuario.setPhone(phone);



                        registerUsuario(usuario);


                    } else {
                        edit_pass.requestFocus();
                        edit_pass.setError("Write your password");
                    }
                }else {
                    edit_phone.requestFocus();
                    edit_phone.setError("Write your phone number");
                }
            } else {
                edit_email.requestFocus();
                edit_email.setError("Write your email");
            }
        }else{
            edit_nome.requestFocus();
            edit_nome.setError("Write your name");
        }

    }

private void registerUsuario(Usuario usuario){
    FirebaseHelper.getAuth().createUserWithEmailAndPassword(
            usuario.getEmail(), usuario.getPass()
    ).addOnCompleteListener(task -> {

        if(task.isSuccessful()){

            String idUser = task.getResult().getUser().getUid();
            usuario.setId(idUser);

            usuario.save();

            finish();
            startActivity(new Intent(this, MainActivity.class));
        }else {
            String error = task.getException().getMessage();
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            
        }

    });
}


    private void configCliques(){
        findViewById(R.id.bt_back).setOnClickListener(view -> finish());
    }

    private void iniciaComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_pass);
        edit_phone = findViewById(R.id.edit_phone);
        progressBar = findViewById(R.id.progressBar);
        TextView text_title = findViewById(R.id.text_title);
        text_title.setText("Crie sua conta");

    }

}