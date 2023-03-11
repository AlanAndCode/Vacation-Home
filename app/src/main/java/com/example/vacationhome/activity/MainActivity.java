package com.example.vacationhome.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.vacationhome.R;
import com.example.vacationhome.activity.auth.LoginActivity;
import com.example.vacationhome.helper.FirebaseHelper;

public class MainActivity extends AppCompatActivity {
private ImageButton bt_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponentes();

        configCliques();
    }

private void configCliques(){
        bt_menu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, bt_menu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if(menuItem.getItemId() == R.id.bt_menu){
startActivity(new Intent(this, FilterActivity.class));
                }else if((menuItem.getItemId() == R.id.menu_my_ad)){
                    if(FirebaseHelper.getAutenticado()){
                        startActivity(new Intent(this, MenuMyAdActivity.class));
                    }else {
                        showDialogLogin();
                    }
                }else {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MyAccountActivity.class));
                    } else {
                        showDialogLogin();
                    }
                }

                return true;
            });
popupMenu.show();
        });
}

private void showDialogLogin(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Autentication");
    builder.setMessage("you are not yet logged into the APP");
    builder.setCancelable(false);
    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
    builder.setPositiveButton("yes", (dialog, which) -> {
       startActivity(new Intent(this, LoginActivity.class));
    });

    AlertDialog dialog = builder.create();
    dialog.show();
}

private void iniciaComponentes(){
    bt_menu = findViewById(R.id.bt_menu);
}

}