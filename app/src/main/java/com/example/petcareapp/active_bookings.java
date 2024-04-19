package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class active_bookings extends AppCompatActivity {

    private TextView txtBookings;
    private ImageView imgSelected;

    private Button btnLogOut;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_bookings);

        txtBookings = findViewById(R.id.txtBookings);
        imgSelected = findViewById(R.id.imgSelected);
        btnLogOut = findViewById(R.id.btnLogOut);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        retrieveAndDisplayBookings();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(active_bookings.this, login_selection.class);
                startActivity(logOutIntent);
            }
        });
    }

    private void retrieveAndDisplayBookings() {
        if (fAuth.getCurrentUser() != null) {
            String userId = fAuth.getCurrentUser().getUid();

            DocumentReference df = fstore.collection("Orders").document(userId);
            df.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                StringBuilder bookingsText = new StringBuilder();

                                String petName = document.getString("PetName");
                                String petType = document.getString("PetType");
                                String petStartDate = document.getString("PetStartDate");
                                String petEndDate = document.getString("PetEndDate");
                                Double totalPrice = document.getDouble("TotalPrice");

                                bookingsText.append("Pet Name: ").append(petName).append("\n");
                                bookingsText.append("Pet Type: ").append(petType).append("\n");
                                bookingsText.append("Start Date: ").append(petStartDate).append("\n");
                                bookingsText.append("End Date: ").append(petEndDate).append("\n");
                                bookingsText.append("Total Price: Rs.").append(totalPrice).append("\n\n");

                                String imageUrl = document.getString("ImageUrl");
                                if (imageUrl != null) {
                                    Glide.with(this).load(imageUrl).into(imgSelected);
                                }

                                txtBookings.setText(bookingsText.toString());
                            } else {
                                txtBookings.setText("No booking data found");
                            }
                        } else {
                            txtBookings.setText("Error retrieving bookings");
                        }
                    });
        } else {
            txtBookings.setText("User not authenticated");
        }
    }
}
