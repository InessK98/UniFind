package com.example.unifind.models;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class SavedListing extends RealmObject {

    @PrimaryKey
    private String id;
    private String userId;
    private String listingId;
    private long savedAt;

    @Ignore
    private Listing listing;

    public SavedListing() {
        this.savedAt = System.currentTimeMillis();
    }

    public SavedListing(String userId, String listingId) {
        this();
        this.userId = userId;
        this.listingId = listingId;
        this.id = getCompositeId();
    }

    public String getCompositeId() {
        if (userId == null || listingId == null) {
            return null;
        }
        return userId + "_" + listingId;
    }

    public boolean isListingLoaded() {
        return listing != null;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getListingId() { return listingId; }
    public void setListingId(String listingId) { this.listingId = listingId; }

    public long getSavedAt() { return savedAt; }
    public void setSavedAt(long savedAt) { this.savedAt = savedAt; }

    public Listing getListing() { return listing; }
    public void setListing(Listing listing) { this.listing = listing; }

    @Override
    public String toString() {
        return "SavedListing{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", listingId='" + listingId + '\'' +
                '}';
    }
}