package com.example.unifind.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String name;

    @Required
    private String email;

    private String studentId;
    private String university;
    private String profilePhoto;
    private double rating;
    private int totalListings;
    private int soldListings;
    private RealmList<String> favoriteListingIds;

    // Empty constructor required by Realm
    public User() {
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rating = 0.0;
        this.totalListings = 0;
        this.soldListings = 0;
        this.university = "UIR";
        this.favoriteListingIds = new RealmList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getStudentId() { return studentId; }
    public String getUniversity() { return university; }
    public String getProfilePhoto() { return profilePhoto; }
    public double getRating() { return rating; }
    public int getTotalListings() { return totalListings; }
    public int getSoldListings() { return soldListings; }
    public RealmList<String> getFavoriteListingIds() {
        if (favoriteListingIds == null) {
            favoriteListingIds = new RealmList<>();
        }
        return favoriteListingIds;
    }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setUniversity(String university) { this.university = university; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
    public void setRating(double rating) { this.rating = rating; }
    public void setTotalListings(int totalListings) { this.totalListings = totalListings; }
    public void setSoldListings(int soldListings) { this.soldListings = soldListings; }
    public void setFavoriteListingIds(RealmList<String> favoriteListingIds) { this.favoriteListingIds = favoriteListingIds; }
}