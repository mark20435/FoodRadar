package ress;

import java.sql.Timestamp;

public class Res {
	private int resId;
	private String resName;
	private String resAddress;
	private String resLat;
	private String resLon;
	private String resTel;
	private String resHours;
	private int resCategoryId;
	private boolean resEnable;
	private int userId;
	private Timestamp modifyDate;
	
	public Res(int resId, String resName, String resAddress, String resLat, String resLon, String resTel,
			String resHours, int resCategoryId, boolean resEnable, int userId, Timestamp modifyDate) {
		super();
		this.resId = resId;
		this.resName = resName;
		this.resAddress = resAddress;
		this.resLat = resLat;
		this.resLon = resLon;
		this.resTel = resTel;
		this.resHours = resHours;
		this.resCategoryId = resCategoryId;
		this.resEnable = resEnable;
		this.userId = userId;
		this.modifyDate = modifyDate;
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

	public String getResAddress() {
		return resAddress;
	}

	public void setResAddress(String resAddress) {
		this.resAddress = resAddress;
	}

	public String getResLat() {
		return resLat;
	}

	public void setResLat(String resLat) {
		this.resLat = resLat;
	}

	public String getResLon() {
		return resLon;
	}

	public void setResLon(String resLon) {
		this.resLon = resLon;
	}

	public String getResTel() {
		return resTel;
	}

	public void setResTel(String resTel) {
		this.resTel = resTel;
	}

	public String getResHours() {
		return resHours;
	}

	public void setResHours(String resHours) {
		this.resHours = resHours;
	}

	public int getResCategoryId() {
		return resCategoryId;
	}

	public void setResCategoryId(int resCategoryId) {
		this.resCategoryId = resCategoryId;
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

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	
}
