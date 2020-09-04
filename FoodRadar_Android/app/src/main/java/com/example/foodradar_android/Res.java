package com.example.foodradar_android;

import java.io.Serializable;

public class Res implements Serializable {

    private int resId;
    private String resName;
    private String resAdress;
    private String resTel;
    private int categoryId;
    private boolean resEnable;
    private int userId;
    private String resCategoryInfo;

    public Res(int resId, String resName, String resAdress, String resTel, int categoryId, boolean resEnable, int userId) {
        this.resId = resId;
        this.resName = resName;
        this.resAdress = resAdress;
        this.resTel = resTel;
        this.categoryId = categoryId;
        this.resEnable = resEnable;
        this.userId = userId;
        this.resCategoryInfo = resCategoryInfo;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResAdress() {
        return resAdress;
    }

    public void setResAdress(String resAdress) {
        this.resAdress = resAdress;
    }

    public String getResTel() {
        return resTel;
    }

    public void setResTel(String resTel) {
        this.resTel = resTel;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isResEnable() {
        return resEnable;
    }

    public void setResEnable(boolean resEnable) {
        this.resEnable = resEnable;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getResCategoryInfo() {
        return resCategoryInfo;
    }

    public void setResCategoryInfo(String resCategoryInfo) {
        this.resCategoryInfo = resCategoryInfo;
    }





}
