package com.example.unifind.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Category extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String name;

    private String icon;
    private String parentCategory;

    // Empty constructor
    public Category() {
    }

    public Category(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getIcon() { return icon; }
    public String getParentCategory() { return parentCategory; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setIcon(String icon) { this.icon = icon; }
    public void setParentCategory(String parentCategory) { this.parentCategory = parentCategory; }
}