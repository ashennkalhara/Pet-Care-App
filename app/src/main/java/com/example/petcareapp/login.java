package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    EditText LoginName, LoginPassword;
    Button btnLoginInLogin, btnRegInLogin;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginName = findViewById(R.id.LoginName);
        LoginPassword = findViewById(R.id.LoginPassword);

        btnLoginInLogin = findViewById(R.id.btnLoginInLogin);
        btnRegInLogin = findViewById(R.id.btnRegInLogin);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        btnLoginInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = LoginName.getText().toString().trim();
                String password = LoginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    LoginName.setError("Email is required");
                    LoginName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    LoginPassword.setError("Password is required");
                    LoginPassword.requestFocus();
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(login.this, "User Logged Successfully", Toast.LENGTH_SHORT).show();

                                checkUserRole(authResult.getUser().getUid());
                            }
                        })

                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, "Login Failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnRegInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, login_selection.class));
            }
        });
    }

    private void checkUserRole(String uid) {
        DocumentReference df = fstore.collection("Users").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                if (documentSnapshot.getString("isCustomer") != null) {
                    startActivity(new Intent(getApplicationContext(), customer_home.class));
                    finish();
                } else if (documentSnapshot.getString("isCaregiver") != null) {
                    startActivity(new Intent(getApplicationContext(), caregiver_home.class));
                    finish();
                }
            }
        });
    }
}
