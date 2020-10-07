package com.example.foodradar_android.coupon;

import android.widget.Button;

import java.io.Serializable;
import java.sql.Timestamp;

public class Coupon implements Serializable {
    private int id;
    private int couPonId;
    private int resId;
    private String resName;
    private String tvCouInfo;
    private String couPonStartDate;
    private String couPonEndDate;
    private boolean couPonType;
    private boolean couPonEnable;
    private Timestamp date;
    private Button btCollect;

    public Coupon(int id, int couPonId, int resId, String resName, String tvCouInfo, String couPonStartDate, String couPonEndDate, boolean couPonType, boolean couPonEnable, Timestamp date, Button btCollect) {
        this.id = id;
        this.couPonId = couPonId;
        this.resId = resId;
        this.resName = resName;
        this.tvCouInfo = tvCouInfo;
        this.couPonStartDate = couPonStartDate;
        this.couPonEndDate = couPonEndDate;
        this.couPonType = couPonType;
        this.couPonEnable = couPonEnable;
        this.date = date;
        this.btCollect = btCollect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCouPonId() {
        return couPonId;
    }

    public void setCouPonId(int couPonId) {
        this.couPonId = couPonId;
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

    public String getTvCouInfo() {
        return tvCouInfo;
    }

    public void setTvCouInfo(String tvCouInfo) {
        this.tvCouInfo = tvCouInfo;
    }

    public String getCouPonStartDate() {
        return couPonStartDate;
    }

    public void setCouPonStartDate(String couPonStartDate) {
        this.couPonStartDate = couPonStartDate;
    }

    public String getCouPonEndDate() {
        return couPonEndDate;
    }

    public void setCouPonEndDate(String couPonEndDate) {
        this.couPonEndDate = couPonEndDate;
    }

    public boolean isCouPonType() {
        return couPonType;
    }

    public void setCouPonType(boolean couPonType) {
        this.couPonType = couPonType;
    }

    public boolean isCouPonEnable() {
        return couPonEnable;
    }

    public void setCouPonEnable(boolean couPonEnable) {
        this.couPonEnable = couPonEnable;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Button getBtCollect() {
        return btCollect;
    }

    public void setBtCollect(Button btCollect) {
        this.btCollect = btCollect;
    }
}
