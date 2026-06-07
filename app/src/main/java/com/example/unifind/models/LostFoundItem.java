package com.example.unifind.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LostFoundItem extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String title;

    private String description;
    private String type; // "lost" or "found"
    private String location;
    private RealmList<String> photos;
    private long dateLostOrFound;
    private String reportedBy;
    private boolean isResolved;
    private long createdAt;

    // Empty constructor
    public LostFoundItem() {
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public RealmList<String> getPhotos() { return photos; }
    public long getDateLostOrFound() { return dateLostOrFound; }
    public String getReportedBy() { return reportedBy; }
    public boolean isResolved() { return isResolved; }
    public long getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setLocation(String location) { this.location = location; }
    public void setPhotos(RealmList<String> photos) { this.photos = photos; }
    public void setDateLostOrFound(long dateLostOrFound) { this.dateLostOrFound = dateLostOrFound; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public void setResolved(boolean resolved) { isResolved = resolved; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}