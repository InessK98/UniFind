package com.example.unifind.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Listing extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String title;

    private String description;
    private double price;
    private String category;
    private String condition;
    private RealmList<String> photos;
    private String status; // "available", "sold", "reserved"
    private long createdAt;
    private String sellerId;
    private String location;

    // Empty constructor
    public Listing() {
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getCondition() { return condition; }
    public RealmList<String> getPhotos() { return photos; }
    public String getStatus() { return status; }
    public long getCreatedAt() { return createdAt; }
    public String getSellerId() { return sellerId; }
    public String getLocation() { return location; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setPhotos(RealmList<String> photos) { this.photos = photos; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setLocation(String location) { this.location = location; }
}