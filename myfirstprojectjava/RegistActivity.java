package com.example.myfirstprojectjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class RegistActivity extends AppCompatActivity {
    DBUsers mDBConnector;
    Context mContext;
    Button bbutton;
    Button rbutton;
    Button cibutton;
    EditText etl;
    EditText etp;
    User user;
    TextView tverrors;


    @SuppressLint({"SetTextI18n", "Recycle", "SuspiciousIndentation"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.regist_layout);

        mContext = this;
        mDBConnector = new DBUsers(this);
        bbutton = findViewById(R.id.buttonBack);
        cibutton = findViewById(R.id.buttonComeIn);
        rbutton = findViewById(R.id.buttonReg);
        tverrors = findViewById(R.id.textViewER);

        etl = findViewById(R.id.editTextLogin);
        etp = findViewById(R.id.editTextPassword);

        user = (User) getIntent().getExtras().getSerializable("user");


        bbutton.setOnClickListener(v -> {
            Intent i = new Intent();
            setResult(RESULT_CANCELED, i);
            finish();
        });

        rbutton.setOnClickListener(v -> {
            String login = etl.getText().toString();
            String password = etp.getText().toString();
            if (!login.equals("")) {
                User checkUser = mDBConnector.select(login);
                if (checkUser == null)
                    user.login = login;
                    user.password = password;
                    if (mDBConnector.insert(user) != -1){
                        tverrors.setText("вы успешно зарегистрировались!");
                    }
                    else {
                        tverrors.setText("произошла некая ошибка. Попробуйте снова");
                    }
            }
        });

        cibutton.setOnClickListener(v -> {

            try {
                String login = etl.getText().toString();
                if (!login.equals("")) {
                    User user = mDBConnector.select(login);
                    if (user == null) {
                        tverrors.setText("Такого логина не существует.");
                    } else {
                        Intent i = new Intent();
                        i.putExtra("user", user);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
                else {
                    tverrors.setText("пустой логин");
                }
            } catch (Exception e) {
                ((TextView) findViewById(R.id.textViewER)).setText(e.toString());
            }
        });
    }
}