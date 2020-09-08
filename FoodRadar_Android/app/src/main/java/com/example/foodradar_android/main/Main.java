package com.example.foodradar_android.main;

import java.io.Serializable;

public class Main implements Serializable {
    private int imageId;
    private String cateName;

    public Main(int imageId, String cateName) {
        this.imageId = imageId;
        this.cateName = cateName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
