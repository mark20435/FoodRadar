package server.coupon;

import java.awt.Button;
import java.sql.Timestamp;

import com.sun.accessibility.internal.resources.accessibility;

public class Coupon {
	
	private int id;
    private String couPonInfo;
    private Timestamp date;
    private String couPonStartDate;
    private String couPonEndDate;
    private Button btCollect;
    private Boolean couPonEnable;
    private Boolean couPonType;
    
    

    public Coupon(Integer id, int couPonId, int resId, String resName, String couPonInfo,
    		Timestamp date, Button btCollect, Boolean couPonEnable, String couPonStartDate, String couPonEndDate, Boolean couPonType) {
        this.id = id;
        this.couPonInfo = couPonInfo;
        this.date = date;
        this.btCollect = btCollect;
        this.couPonEnable = couPonEnable;
        this.couPonStartDate = couPonStartDate;
        this.couPonEndDate = couPonEndDate;
        this.couPonType = couPonType;
        
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCouPonInfo() {
		return couPonInfo;
	}

	public void setCouPonInfo(String couPonInfo) {
		this.couPonInfo = couPonInfo;
	}

	public Coupon(Integer resId, String couPonInfo, String couPonStartDate, String couPonEndDate,
			String couPonType, boolean couPonEnable, int couponId) {
		// TODO Auto-generated constructor stub
	}

	public Coupon(int id, String couPonStartDate, String couPonEndDate, Boolean couPonType, String couPonInfo) {
		this.id = id;
        this.couPonInfo = couPonInfo;
        this.couPonStartDate = couPonStartDate;
        this.couPonEndDate = couPonEndDate;
        this.couPonType = couPonType;
		
	}

	public Coupon(int id, Integer couponId, String couPonStartDate, String couPonEndDate, String couPonType,
			String couPonInfo, boolean couPonEnable, Integer resId) {
		// TODO Auto-generated constructor stub
	}

	public Coupon(Integer resId, String couPonStartDate, String couPonEndDate, Boolean couPonType,
			String couPonInfo, Boolean couPonEnable, Integer couPonId) {
		// TODO Auto-generated constructor stub
	}

	

	public Boolean getCouPonType() {
		return couPonType;
	}

	public void setCouPonType(Boolean couPonType) {
		this.couPonType = couPonType;
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



    public String getcouPonInfo() {
        return couPonInfo;
    }

    public void setcouPonInfo(String couPonInfo) {
        this.couPonInfo = couPonInfo;
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


