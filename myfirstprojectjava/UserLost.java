package com.example.myfirstprojectjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserLost extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_layout);
        Button b = findViewById(R.id.buttonLost);
        b.setOnClickListener(v -> {
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
        });
    }
}
