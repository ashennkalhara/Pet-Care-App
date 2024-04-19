package com.example.petcareapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class login_selection extends AppCompatActivity {
    Button btnCusReg, btnCusLogin, btnCarReg, btnCarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);

        btnCusReg = findViewById(R.id.btnCusReg);
        btnCusLogin = findViewById(R.id.btnCusLogin);
        btnCarReg = findViewById(R.id.btnCarReg);
        btnCarLogin = findViewById(R.id.btnCarLogin);

        btnCusReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(login_selection.this, com.example.petcareapp.customer_register.class);
                startActivity(loginIntent);
            }
        });

        btnCusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(login_selection.this, com.example.petcareapp.login.class);
                startActivity(loginIntent);
            }
        });

        btnCarReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(login_selection.this, com.example.petcareapp.caregiver_register.class);
                startActivity(loginIntent);
            }
        });

        btnCarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(login_selection.this, com.example.petcareapp.login.class);
                startActivity(loginIntent);
            }
        });
    }
}
