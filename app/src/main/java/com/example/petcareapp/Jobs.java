package com.example.petcareapp;

public class Jobs {
    String PetOwnerName;
    String PetType;
    String PetGender,ImageUrl,PetLocation;
    Double TotalPrice;
    Long Duration;


    public Jobs(String petOwnerName, String petType, String petGender, String imageUrl, String petLocation, String totalPrice, Long duration) {
        PetOwnerName = petOwnerName;
        PetType = petType;
        PetGender = petGender;
        ImageUrl = imageUrl;
        PetLocation = petLocation;
        TotalPrice = Double.valueOf(totalPrice);
        Duration = duration;
    }

    public Jobs() {
    }

    public String getPetOwnerName() {
        return PetOwnerName;
    }

    public void setPetOwnerName(String petOwnerName) {
        PetOwnerName = petOwnerName;
    }

    public String getPetType() {
        return PetType;
    }

    public void setPetType(String petType) {
        PetType = petType;
    }

    public String getPetGender() {
        return PetGender;
    }

    public void setPetGender(String petGender) {
        PetGender = petGender;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPetLocation() {
        return PetLocation;
    }

    public void setPetLocation(String petLocation) {
        PetLocation = petLocation;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = Double.valueOf(totalPrice);
    }

    public Long getDuration() {return Duration; }

    public void setDuration(Long duration) {Duration = duration; }
}
