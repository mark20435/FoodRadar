package server.user;

import java.sql.Timestamp;

public class MyRes {
	// Date Time: 2020-09-07 20:00:17
	// table name: MyRes
	private int myResId;
	private int userId;
	private int resId;
	private Timestamp modifyDate;

	private String resName;
	private String resHours;
	private String resTel;
	private String resAddress;
	private byte[] resImg;

	// R.resName, R.resHours, R.resTel, R.resAddress, R.resImg
	// 要呈現在畫面上的餐廳資料
	public MyRes(int resId, String resName, String resHours, String resTel, String resAddress, byte[] resImg) {
		super();
		this.resId = resId;
		this.resName = resName;
		this.resHours = resHours;
		this.resTel = resTel;
		this.resAddress = resAddress;
		this.resImg = resImg;
	}

	// 我的餐廳收藏資料
	public MyRes(int myResId, int userId, int resId, Timestamp modifyDate) {
		super();
		this.myResId = myResId;
		this.userId = userId;
		this.resId = resId;
		this.modifyDate = modifyDate;
	}

	public int getMyResId() {
		return myResId;
	}

	public void setMyResId(int myResId) {
		this.myResId = myResId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResHours() {
		return resHours;
	}

	public void setResHours(String resHours) {
		this.resHours = resHours;
	}

	public String getResTel() {
		return resTel;
	}

	public void setResTel(String resTel) {
		this.resTel = resTel;
	}

	public String getResAddress() {
		return resAddress;
	}

	public void setResAddress(String resAddress) {
		this.resAddress = resAddress;
	}

	public byte[] getResImg() {
		return resImg;
	}

	public void setResImg(byte[] resImg) {
		this.resImg = resImg;
	}

}
