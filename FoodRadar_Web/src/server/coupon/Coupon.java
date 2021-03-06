package server.coupon;

import java.awt.Button;
import java.sql.Timestamp;

import com.sun.accessibility.internal.resources.accessibility;

public class Coupon {
	
	private int id;
	private int userId;
	private int couPonId;
	private int resId;
    private String couPonInfo;
    private Timestamp date;
    private String couPonStartDate;
    private String couPonEndDate;
    private Button btCollect;
    private Boolean spTypeBoolean;
    private Boolean spEnableBoolean;
    private Boolean couPonEnable;
    private Boolean couPonType;
    private Boolean couponLoveStatus;
    private int loveCount;
    private String resName;
    
    
    public Coupon(int couPonId, Integer resId, String couPonStartDate, String couPonEndDate, boolean couPonType,
			String couPonInfo, boolean couPonEnable, Integer userId, Boolean couponLoveStatus) {
		this.couPonId = couPonId;
		this.resId = resId;
		this.couPonStartDate = couPonStartDate;
		this.couPonEndDate = couPonEndDate;
		this.couPonType = couPonType;
		this.couPonInfo = couPonInfo;
		this.couPonEnable = couPonEnable;
		this.userId = userId;
		this.couponLoveStatus = couponLoveStatus;
		
	}
    
    public Coupon(int couPonId, Integer resId, String couPonStartDate, String couPonEndDate, boolean couPonType,
			String couPonInfo, boolean couPonEnable, Integer userId) {
		this.couPonId = couPonId;
		this.resId = resId;
		this.couPonStartDate = couPonStartDate;
		this.couPonEndDate = couPonEndDate;
		this.couPonType = couPonType;
		this.couPonInfo = couPonInfo;
		this.couPonEnable = couPonEnable;
		this.userId = userId;
		
	}
	
	public Coupon(int couPonId, Integer resId, String couPonStartDate, String couPonEndDate, boolean couPonType,
			String couPonInfo, boolean couPonEnable, Integer userId, String resName) {
		this.couPonId = couPonId;
		this.resId = resId;
		this.couPonStartDate = couPonStartDate;
		this.couPonEndDate = couPonEndDate;
		this.couPonType = couPonType;
		this.couPonInfo = couPonInfo;
		this.couPonEnable = couPonEnable;
		this.userId = userId;
		this.resName = resName;
	}

//    public Coupon(Integer id, int couPonId, int resId, int userId, String resName, String couPonInfo,
//    		Timestamp date, Button btCollect, Boolean couPonEnable, String couPonStartDate, String couPonEndDate, 
//    		Boolean couPonType, Boolean couponLoveStatus, int loveCount) {
//        this.id = id;
//        this.userId = userId;
//        this.couPonInfo = couPonInfo;
//        this.date = date;
//        this.btCollect = btCollect;
//        this.couPonEnable = couPonEnable;
//        this.couPonStartDate = couPonStartDate;
//        this.couPonEndDate = couPonEndDate;
//        this.couPonType = couPonType;
//        this.couponLoveStatus = couponLoveStatus;
//        this.loveCount = loveCount;
//        
//    }
    public Coupon(Integer resId, String couPonStartDate, String couPonEndDate, Boolean couPonType,
			String couPonInfo, Boolean couPonEnable, Integer couPonId) {
		this.resId = resId;
		this.couPonStartDate = couPonStartDate;
		this.couPonEndDate = couPonEndDate;
		this.couPonType = couPonType;
		this.couPonInfo = couPonInfo;
		this.couPonEnable = couPonEnable;
		this.couPonId = couPonId;
		
	}
    
    public Coupon(int couPonId, int resId, String couPonStartDate, String couPonEndDate, boolean couPonType, String couPonInfo, boolean couPonEnable, int userId) {
        this.resId = resId;
        this.couPonId = couPonId;
        this.couPonStartDate = couPonStartDate;
        this.couPonEndDate = couPonEndDate;
        this.couPonType = couPonType;
        this.couPonInfo = couPonInfo;
        this.couPonEnable = couPonEnable;
        this.userId = userId;

    }
    
    public Coupon(int resId, String couPonStartDate, String couPonEndDate, boolean spTypeBoolean, String couPonInfo, boolean spEnableBoolean, int userId) {
        this.resId = resId;
        this.couPonStartDate = couPonStartDate;
        this.couPonEndDate = couPonEndDate;
        this.spTypeBoolean = spTypeBoolean;
        this.spEnableBoolean = spEnableBoolean;
        this.couPonInfo = couPonInfo;
        this.userId = userId;

    }
    
    public Boolean getSpTypeBoolean() {
		return spTypeBoolean;
	}

	public void setSpTypeBoolean(Boolean spTypeBoolean) {
		this.spTypeBoolean = spTypeBoolean;
	}

	public Boolean getSpEnableBoolean() {
		return spEnableBoolean;
	}

	public void setSpEnableBoolean(Boolean spEnableBoolean) {
		this.spEnableBoolean = spEnableBoolean;
	}

	public Boolean getCouponLoveStatus() {
		return couponLoveStatus;
	}

	public void setCouponLoveStatus(Boolean couponLoveStatus) {
		this.couponLoveStatus = couponLoveStatus;
	}

	public int getLoveCount() {
		return loveCount;
	}

	public void setLoveCount(int loveCount) {
		this.loveCount = loveCount;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

//	public Coupon(Integer resId, String couPonInfo, String couPonStartDate, String couPonEndDate,
//			String couPonType, boolean couPonEnable, int couponId) {
//		// TODO Auto-generated constructor stub
//	}



//	public Coupon(int id, Integer couponId, String couPonStartDate, String couPonEndDate, String couPonType,
//			String couPonInfo, boolean couPonEnable, Integer resId) {
//		// TODO Auto-generated constructor stub
//	}
//

//	public Coupon(int couPonId, int resId, String couPonStartDate, String couPonEndDate, Boolean couPonType,
//			String couPonInfo, int userId) {
//		this.couPonId = couPonId;
//		this.resId = resId;
//		this.couPonStartDate = couPonStartDate;
//		this.couPonEndDate = couPonEndDate;
//		this.couPonType = couPonType;
//		this.couPonInfo = couPonInfo;
//		this.userId = userId;
//	}

	public int getCouPonId() {
		return couPonId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
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


