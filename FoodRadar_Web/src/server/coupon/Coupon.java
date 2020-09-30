package server.coupon;

import java.awt.Button;
import java.sql.Timestamp;

public class Coupon {
	
	private int couPonId;
    private int resId;
    private String resName;
    private String tvCouInfo;
    private Timestamp date;
    private Button btCollect;
    private Boolean couPonEnable;

    public Coupon(int couPonId, int resId, String resName, String tvCouInfo, Timestamp date, Button btCollect, Boolean couPonEnable) {
        this.couPonId = couPonId;
        this.resId = resId;
        this.resName = resName;
        this.tvCouInfo = tvCouInfo;
        this.date = date;
        this.btCollect = btCollect;
        this.couPonEnable = couPonEnable;
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
    
    public Boolean getCouPonEnable() {
        return couPonEnable;
    }

    public void setCouPonEnable(Boolean couPonEnable) {
        this.couPonEnable = couPonEnable;
    }
}


