package com.example.unifind.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Report extends RealmObject {

    public static final String TARGET_TYPE_LISTING      = "listing";
    public static final String TARGET_TYPE_USER         = "user";
    public static final String TARGET_TYPE_LOST_FOUND   = "lostFoundItem";
    public static final String TARGET_TYPE_REVIEW       = "review";

    public static final String STATUS_PENDING  = "pending";
    public static final String STATUS_REVIEWED = "reviewed";
    public static final String STATUS_RESOLVED = "resolved";

    public static final String REASON_SPAM        = "spam";
    public static final String REASON_OFFENSIVE   = "offensive";
    public static final String REASON_FAKE        = "fake";
    public static final String REASON_INAPPROPRIATE = "inappropriate";
    public static final String REASON_OTHER       = "other";

    @PrimaryKey
    private String id;
    private String reason;
    private String additionalDetails;
    private String reporterId;
    private String targetId;
    private String targetType;
    private String status;

    private long createdAt;
    private long reviewedAt;

    public Report() {
        this.status = STATUS_PENDING;
        this.createdAt = System.currentTimeMillis();
    }

    public Report(String reason, String reporterId, String targetId, String targetType) {
        this();
        this.reason     = reason;
        this.reporterId = reporterId;
        this.targetId   = targetId;
        this.targetType = targetType;
    }

    public Report(String reason, String additionalDetails, String reporterId, String targetId, String targetType) {
        this();
        this.reason            = reason;
        this.additionalDetails = additionalDetails;
        this.reporterId        = reporterId;
        this.targetId          = targetId;
        this.targetType        = targetType;
    }

    public boolean isPending() {
        return STATUS_PENDING.equals(status);
    }

    public boolean isResolved() {
        return STATUS_RESOLVED.equals(status);
    }

    public void markAsReviewed(long reviewTime) {
        this.status     = STATUS_REVIEWED;
        this.reviewedAt = reviewTime;
    }

    public void markAsResolved(long reviewTime) {
        this.status     = STATUS_RESOLVED;
        this.reviewedAt = reviewTime;
    }

    // Getters and Setters
    public String getId()                { return id; }
    public void   setId(String id)       { this.id = id; }
    public String getReason()            { return reason; }
    public void   setReason(String r)    { this.reason = r; }
    public String getReporterId()        { return reporterId; }
    public void   setReporterId(String i){ this.reporterId = i; }
    public String getTargetId()          { return targetId; }
    public void   setTargetId(String t)  { this.targetId = t; }
    public String getTargetType()        { return targetType; }
    public void   setTargetType(String t){ this.targetType = t; }
    public String getStatus()            { return status; }
    public void   setStatus(String s)    { this.status = s; }
    public String getAdditionalDetails() { return additionalDetails; }
    public void   setAdditionalDetails(String details) { this.additionalDetails = details; }
    public long   getCreatedAt()         { return createdAt; }
    public void   setCreatedAt(long time){ this.createdAt = time; }
    public long   getReviewedAt()        { return reviewedAt; }
    public void   setReviewedAt(long time){ this.reviewedAt = time; }
}