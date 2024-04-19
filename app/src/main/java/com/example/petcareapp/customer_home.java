package com.example.petcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class customer_home extends AppCompatActivity {
    Button btnBookCar;
    TextView txtHi;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        btnBookCar = findViewById(R.id.btnBookCar);
        txtHi = findViewById(R.id.txtHi);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = fstore.collection("Users").document(userid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            String username = documentSnapshot.getString("CustomerName");
            txtHi.setText("Hi "+username);
        });

        btnBookCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BookIntent = new Intent(customer_home.this, book_caregiver.class);
                startActivity(BookIntent);
            }
        });

    }
}