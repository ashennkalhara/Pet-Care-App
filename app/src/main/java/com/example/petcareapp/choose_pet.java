package com.example.petcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class choose_pet extends AppCompatActivity {

    private RecyclerView recyclerViewPets;
    private PetAdapter petAdapter;
    private List<Jobs> petList;
    private FirebaseFirestore fstore;

    private Button btnLogOutCare;
    private EditText editTextSearchLocation;
    private Button buttonSearch;

    private boolean initialRetrievalDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pet);

        btnLogOutCare = findViewById(R.id.btnLogOutCare);
        editTextSearchLocation = findViewById(R.id.editTextSearchLocation);
        buttonSearch = findViewById(R.id.buttonSearch);

        fstore = FirebaseFirestore.getInstance();

        recyclerViewPets = findViewById(R.id.rcvJobView);
        recyclerViewPets.setHasFixedSize(true);
        recyclerViewPets.setLayoutManager(new LinearLayoutManager(this));

        petList = new ArrayList<>();

        petAdapter = new PetAdapter(this, petList);
        recyclerViewPets.setAdapter(petAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchLocation = editTextSearchLocation.getText().toString();
                retrievePetData(searchLocation);
            }
        });

        btnLogOutCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutCareIntent = new Intent(choose_pet.this, login_selection.class);
                startActivity(logOutCareIntent);
            }
        });

        retrievePetData("");
        initialRetrievalDone = true;
    }

    private void retrievePetData(String searchLocation) {
        if (!initialRetrievalDone) {
            petList.clear();
        }

        if (searchLocation.isEmpty()) {
            // Retrieve all pets
            fstore.collection("Orders")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Jobs pet = document.toObject(Jobs.class);
                                petList.add(pet);
                            }
                            petAdapter.notifyDataSetChanged();
                        } else {
                        }
                    });
        } else {
            fstore.collection("Orders")
                    .whereEqualTo("PetLocation", searchLocation)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Jobs pet = document.toObject(Jobs.class);
                                petList.add(pet);
                            }
                            petAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(choose_pet.this, "No result found", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
