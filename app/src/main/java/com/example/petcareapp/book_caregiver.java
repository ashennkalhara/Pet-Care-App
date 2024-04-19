package com.example.petcareapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class book_caregiver extends AppCompatActivity {
    EditText PetOwnerName, PetName, PetAge, PetColor, PetFeatures, PetLocation;
    TextView startDate, endDate, btnBookingStatus;
    Button btnAddPet;
    ImageButton btnImgPet;
    ImageView imgPet;
    RadioButton radioDog, radioCat, radioMale, radioFemale;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseStorage storage;
    StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_caregiver);

        PetOwnerName = findViewById(R.id.PetOwnerName);
        PetName = findViewById(R.id.PetName);
        PetAge = findViewById(R.id.PetAge);
        PetColor = findViewById(R.id.PetColor);
        PetFeatures = findViewById(R.id.PetFeatures);
        PetLocation = findViewById(R.id.PetLocation);

        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);

        radioDog = findViewById(R.id.radioDog);
        radioCat = findViewById(R.id.radioCat);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        btnBookingStatus = findViewById(R.id.btnBookingStatus);

        btnAddPet = findViewById(R.id.btnAddPet);
        btnImgPet = findViewById(R.id.btnImgPet);
        imgPet = findViewById(R.id.imgPet);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(startDate);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(endDate);
            }
        });

        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPetToFirestore();
            }
        });

        btnImgPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnBookingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookIntent = new Intent(book_caregiver.this, active_bookings.class);
                startActivity(bookIntent);
            }
        });
    }

    private void showDatePickerDialog(final TextView dateTextView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        dateTextView.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void addPetToFirestore() {
        if (fAuth.getCurrentUser() != null) {
            String userId = fAuth.getCurrentUser().getUid();
            String petOwnerName = PetOwnerName.getText().toString().trim();
            String petType = radioDog.isChecked() ? "Dog" : "Cat";
            String petName = PetName.getText().toString().trim();
            String petAge = PetAge.getText().toString().trim();
            String petColor = PetColor.getText().toString().trim();
            String petFeatures = PetFeatures.getText().toString().trim();
            String petLocation = PetLocation.getText().toString().trim();
            String petGender = radioMale.isChecked() ? "Male" : "Female";
            String petStartDate = startDate.getText().toString().trim();
            String petEndDate = endDate.getText().toString().trim();

            long dayCount = calculateDayCount(petStartDate, petEndDate);

            double petPrice = petType.equals("Dog") ? 1000.0 : 800.0;

            double totalPrice = petPrice * dayCount;

            Map<String, Object> petData = new HashMap<>();
            petData.put("PetOwnerName", petOwnerName);
            petData.put("PetType", petType);
            petData.put("PetName", petName);
            petData.put("PetAge", petAge);
            petData.put("PetColor", petColor);
            petData.put("PetFeatures", petFeatures);
            petData.put("PetLocation", petLocation);
            petData.put("PetGender", petGender);
            petData.put("PetStartDate", petStartDate);
            petData.put("PetEndDate", petEndDate);
            petData.put("TotalPrice", totalPrice);
            petData.put("Duration", dayCount);

            if (imageUri != null) {
                String imageName = "images/" + System.currentTimeMillis() + ".jpg";

                StorageReference imageRef = storageReference.child(imageName);

                imageRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        petData.put("ImageUrl", imageUrl);


                                        DocumentReference df = fstore.collection("Orders").document(fAuth.getCurrentUser().getUid());
                                        df.set(petData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(book_caregiver.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                                        clearForm();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(book_caregiver.this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Toast.makeText(book_caregiver.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                fstore.collection("users").document(userId).collection("orders").add(petData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(book_caregiver.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                clearForm();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(book_caregiver.this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(book_caregiver.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgPet.setImageURI(imageUri);
        }
    }

    private long calculateDayCount(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTime(sdf.parse(startDate));
            endCalendar.setTime(sdf.parse(endDate));

            long diffMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
            return diffMillis / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void clearForm() {
        PetOwnerName.getText().clear();
        PetName.getText().clear();
        PetAge.getText().clear();
        PetColor.getText().clear();
        PetFeatures.getText().clear();
        PetLocation.getText().clear();
        startDate.setText("Select Start Date");
        endDate.setText("Select End Date");
        imgPet.setImageResource(0);
    }
}
