package com.example.unifind.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class message extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String content;

    private String senderId;
    private String receiverId;
    private String listingId;
    private long sentAt;
    private boolean isRead;

    // Empty constructor
    public message() {
    }

    // Getters
    public String getId() { return id; }
    public String getContent() { return content; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getListingId() { return listingId; }
    public long getSentAt() { return sentAt; }
    public boolean isRead() { return isRead; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setContent(String content) { this.content = content; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public void setListingId(String listingId) { this.listingId = listingId; }
    public void setSentAt(long sentAt) { this.sentAt = sentAt; }
    public void setRead(boolean read) { isRead = read; }
}