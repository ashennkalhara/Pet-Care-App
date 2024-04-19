package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class caregiver_register extends AppCompatActivity {
    EditText CarRegName, CarRegEmail, CarPassword, CarPassword2;
    Button btnRegInCar;
    FirebaseAuth fAuth;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_register);

        CarRegName = findViewById(R.id.CarRegName);
        CarRegEmail = findViewById(R.id.CarRegEmail);
        CarPassword = findViewById(R.id.CarPassword);
        CarPassword2 = findViewById(R.id.CarPassword2);

        btnRegInCar = findViewById(R.id.btnRegInCar);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        btnRegInCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCaregiver();
            }
        });
    }

    private void registerCaregiver() {
        String name = CarRegName.getText().toString().trim();
        String email = CarRegEmail.getText().toString().trim();
        String password = CarPassword.getText().toString().trim();
        String password2 = CarPassword2.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            CarRegName.setError("Name is required");
            CarRegName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            CarRegEmail.setError("Email is required");
            CarRegEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            CarPassword.setError("Password is required");
            CarPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password2)) {
            CarPassword2.setError("Confirm password is required");
            CarPassword2.requestFocus();
            return;
        }

        if (!password.equals(password2)) {
            CarPassword2.setError("Passwords do not match");
            CarPassword2.requestFocus();
            return;
        }

        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(caregiver_register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        FirebaseUser firebaseUser = fAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            DocumentReference df = fstore.collection("Users").document(firebaseUser.getUid());

                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("CaregiverName", name);
                            userInfo.put("CaregiverEmail", email);
                            userInfo.put("isCaregiver", "2");

                            df.set(userInfo)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(caregiver_register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(caregiver_register.this, caregiver_home.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {

                                        Toast.makeText(caregiver_register.this, "Error Occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }

                        startActivity(new Intent(caregiver_register.this, caregiver_home.class));

                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(caregiver_register.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(caregiver_register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
