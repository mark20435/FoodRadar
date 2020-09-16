package com.example.foodradar_android.main;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String info;
    private int cateSn;

    public Category(int id, String info, int cateSn) {
        this.id = id;
        this.info = info;
        this.cateSn = cateSn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCateSn() {
        return cateSn;
    }

    public void setCateSn(int cateSn) {
        this.cateSn = cateSn;
    }
}
