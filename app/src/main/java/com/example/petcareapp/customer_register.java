package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class customer_register extends AppCompatActivity {
    EditText CusRegName, CusRegEmail, CusPassword, CusPassword2;
    Button btnRegInCus;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        CusRegName = findViewById(R.id.CusRegName);
        CusRegEmail = findViewById(R.id.CusRegEmail);
        CusPassword = findViewById(R.id.CusPassword);
        CusPassword2 = findViewById(R.id.CusPassword2);

        btnRegInCus = findViewById(R.id.btnRegInCus);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        btnRegInCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCustomer();
            }
        });
    }

    private void registerCustomer() {
        String name = CusRegName.getText().toString().trim();
        String email = CusRegEmail.getText().toString().trim();
        String password = CusPassword.getText().toString().trim();
        String password2 = CusPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            CusRegName.setError("Name is required");
            CusRegName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            CusRegEmail.setError("Email is required");
            CusRegEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            CusPassword.setError("Password is required");
            CusPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password2)) {
            CusPassword2.setError("Confirm password is required");
            CusPassword2.requestFocus();
            return;
        }

        if (!password.equals(password2)) {
            CusPassword2.setError("Passwords do not match");
            CusPassword2.requestFocus();
            return;
        }

        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(customer_register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        FirebaseUser firebaseUser = fAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            DocumentReference df = fstore.collection("Users").document(firebaseUser.getUid());

                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("CustomerName", name);
                            userInfo.put("CustomerEmail", email);
                            userInfo.put("isCustomer", "1");

                            df.set(userInfo)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(customer_register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(customer_register.this, customer_home.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(customer_register.this, "Error Occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(customer_register.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(customer_register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
