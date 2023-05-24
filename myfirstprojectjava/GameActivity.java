package com.example.myfirstprojectjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements  UserCallback {
    User user;
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = new DrawView(this, (User) getIntent().getExtras().getSerializable("user"));
        setContentView(drawView);
        drawView.setCallBack(this);
    }

    @Override
    public void onUserDeath(User user) {
        this.user = user;
        Intent i = new Intent(GameActivity.this, UserLost.class);
        startActivityForResult(i, 4);
        drawView.stopDrawThread();
        drawView.stopGameThread();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Intent i = new Intent();
                i.putExtra("user", user);
                setResult(RESULT_OK, i);
                finish();


        }
    }
}