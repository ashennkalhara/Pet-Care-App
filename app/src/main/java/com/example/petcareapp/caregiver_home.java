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

public class caregiver_home extends AppCompatActivity {
    Button btnChoosePet;
    TextView txtWelcome;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_home);

        btnChoosePet = findViewById(R.id.btnChoosePet);
        txtWelcome = findViewById(R.id.txtWelcome);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = fstore.collection("Users").document(userid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            String username = documentSnapshot.getString("CaregiverName");
            txtWelcome.setText("Hi "+username);
        });

        btnChoosePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChooseIntent = new Intent(caregiver_home.this, choose_pet.class);
                startActivity(ChooseIntent);
            }
        });
    }
}