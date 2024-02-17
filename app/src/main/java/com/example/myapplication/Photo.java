package com.example.myapplication;

public class Photo {
    private String photoUrl;
    private String description;

    public Photo() {
        // Default constructor required for Firestore
    }

    public Photo(String photoUrl, String description) {
        this.photoUrl = photoUrl;
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

