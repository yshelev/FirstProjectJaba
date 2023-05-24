package com.example.myfirstprojectjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {

    Button msbutton;
    Button bbutton;
    Button rbutton;
    Button brbutton;
    TextView errorsTV;
    TextView moneyTV;
    DBUsers mDBConnector;
    
    
    
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.shop_layout);
        User user = (User) getIntent().getExtras().getSerializable("user");

        mDBConnector = new DBUsers(this);
        msbutton = findViewById(R.id.movespeedButton);
        bbutton = findViewById(R.id.backButton);
        rbutton = findViewById(R.id.reloadButton);
        brbutton = findViewById(R.id.bountyRewardButton);
        errorsTV = findViewById(R.id.textViewERRORS);
        moneyTV = findViewById(R.id.textViewMONEY);
        moneyTV.setText("Ваши средства: " + user.money);
        msbutton.setOnClickListener(v -> {
            if (user.toSend[0] == 0) {
                if (user.money >= 10) {
                    user.money -= 10;
                    user.toSend[0] += 1;
                    moneyTV.setText("Ваши средства: " + user.money);
                    errorsTV.setText("Вы успешно приобрели товар");
                    mDBConnector.update(user);
                }
                else {
                    errorsTV.setText("Недостаточно средств");
                }
            }
            else {
                errorsTV.setText("Предмет уже был вами приобретен ");
            }

        });
        rbutton.setOnClickListener(v -> {
            if (user.toSend[1] == 0) {
                if (user.money >= 10) {
                    user.money -= 10;
                    user.toSend[1] += 1;
                    moneyTV.setText("Ваши средства: " + user.money);
                    errorsTV.setText("Вы успешно приобрели товар");
                    mDBConnector.update(user);
                }
                else {
                    errorsTV.setText("Недостаточно средств");
                }
            }
            else {
                errorsTV.setText("Предмет уже был вами приобретен ");
            }

        });
        brbutton.setOnClickListener(v -> {
            if (user.toSend[2] == 0) {
                if (user.money >= 10) {
                    user.money -= 10;
                    user.toSend[2] += 1;
                    moneyTV.setText("Ваши средства: " + user.money);
                    errorsTV.setText("Вы успешно приобрели товар");
                    mDBConnector.update(user);

                }
                else {
                    errorsTV.setText("Недостаточно средств");
                }
            }
            else {
                errorsTV.setText("Предмет уже был вами приобретен ");
            }

        });
        bbutton.setOnClickListener(v -> {
            Intent i = new Intent();
            i.putExtra("user", user);
            setResult(RESULT_OK, i);
            finish();
        });
    }
}