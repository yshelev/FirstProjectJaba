package com.example.myfirstprojectjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button buttonStartGame;
    Button buttonShop;
    Button buttonLog;
    User user = new User(-1, new int[]{0, 0, 0}, 0, "", "");
    TextView loginTV;
    Handler mHandler;
    TextView lostTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Context context = this;
        setContentView(R.layout.activity_main);

        buttonStartGame = findViewById(R.id.ButtonStartGame);
        buttonShop = findViewById(R.id.buttonShop);
        buttonLog = findViewById(R.id.buttonLog);
        loginTV = findViewById(R.id.textViewIsLog);
        lostTV = findViewById(R.id.textViewLosted);

        buttonStartGame.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            i.putExtra( "user", user);
            startActivityForResult(i, 3);
        });

        buttonShop.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ShopActivity.class);
            i.putExtra( "user", user);
            startActivityForResult(i, 1);
        });

        buttonLog.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, RegistActivity.class);
            i.putExtra( "user", user);
            startActivityForResult(i, 2);
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:

                this.user = (User) data.getSerializableExtra("user");
                loginTV.setText(Objects.equals(this.user.login, "") ? "Вы играете без аккаунта" : this.user.login + "");

                break;
        }
    }


}