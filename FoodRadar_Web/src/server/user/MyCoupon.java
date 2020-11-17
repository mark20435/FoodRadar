package server.user;

import java.awt.Button;
import java.sql.Timestamp;

public class MyCoupon {
	
	private int id;
	private int resId;
    private String couPonInfo;
    private String couPonStartDate;
    private String couPonEndDate;   
    private Boolean couPonEnable;
    private Boolean couPonType;
	
	private int myCouPonId;
    private int userId;
    private int couPonId;
    private boolean couPonIsUsed;
    private Timestamp modifyDate;
    private byte[] couPonPhoto;

    
    
    public MyCoupon(int id, int resId, String couPonInfo, String couPonStartDate, String couPonEndDate,
			Boolean couPonEnable, Boolean couPonType, int myCouPonId, int userId, int couPonId, boolean couPonIsUsed,
			Timestamp modifyDate, byte[] couPonPhoto) {
		super();
		this.id = id;
		this.resId = resId;
		this.couPonInfo = couPonInfo;
		this.couPonStartDate = couPonStartDate;
		this.couPonEndDate = couPonEndDate;
		this.couPonEnable = couPonEnable;
		this.couPonType = couPonType;
		this.myCouPonId = myCouPonId;
		this.userId = userId;
		this.couPonId = couPonId;
		this.couPonIsUsed = couPonIsUsed;
		this.modifyDate = modifyDate;
		this.couPonPhoto = couPonPhoto;
	}

	public MyCoupon(int myCouPonId, int userId, int couPonId, boolean couPonIsUsed, Timestamp modifyDate, byte[] couPonPhoto) {
        this.myCouPonId = myCouPonId;
        this.userId = userId;
        this.couPonId = couPonId;
        this.couPonIsUsed = couPonIsUsed;
        this.modifyDate = modifyDate;
        this.couPonPhoto = couPonPhoto;
    }

    public MyCoupon(int myCouPonId, int userId, int couPonId, boolean couPonIsUsed, Timestamp modifyDate) {
    	this.myCouPonId = myCouPonId;
        this.userId = userId;
        this.couPonId = couPonId;
        this.couPonIsUsed = couPonIsUsed;
        this.modifyDate = modifyDate;
	}

    
    
	public MyCoupon(int couPonId, int resId, String couPonStartDate, String couPonEndDate, boolean couPonType,
			String couPonInfo, byte[] couPonPhoto, boolean couPonEnable) {
		this.couPonId = couPonId;
		this.resId = resId;
		this.couPonStartDate = couPonStartDate;
		this.couPonEndDate = couPonEndDate;
		this.couPonType = couPonType;
		this.couPonInfo = couPonInfo;
		this.couPonPhoto = couPonPhoto;
		this.couPonEnable = couPonEnable;
	}
	
	public MyCoupon(int couPonId, int userId, Boolean couPonIsUsed) {
		this.userId = userId;
		this.couPonIsUsed = couPonIsUsed;
		this.couPonId = couPonId;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getCouPonInfo() {
		return couPonInfo;
	}

	public void setCouPonInfo(String couPonInfo) {
		this.couPonInfo = couPonInfo;
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

	public Boolean getCouPonEnable() {
		return couPonEnable;
	}

	public void setCouPonEnable(Boolean couPonEnable) {
		this.couPonEnable = couPonEnable;
	}

	public Boolean getCouPonType() {
		return couPonType;
	}

	public void setCouPonType(Boolean couPonType) {
		this.couPonType = couPonType;
	}

	public int getMyCouPonId() {
        return myCouPonId;
    }

    public void setMyCouPonId(int myCouPonId) {
        this.myCouPonId = myCouPonId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCouPonId() {
        return couPonId;
    }

    public void setCouPonId(int couPonId) {
        this.couPonId = couPonId;
    }

    public boolean isCouPonIsUsed() {
        return couPonIsUsed;
    }

    public void setCouPonIsUsed(boolean couPonIsUsed) {
        this.couPonIsUsed = couPonIsUsed;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public byte[] getCouPonPhoto() {
        return couPonPhoto;
    }

    public void setCouPonPhoto(byte[] couPonPhoto) {
        this.couPonPhoto = couPonPhoto;
    }

}
