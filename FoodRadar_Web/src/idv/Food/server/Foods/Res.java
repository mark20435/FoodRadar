package idv.Food.server.Foods;

public class Res {

	 private int resId;
	    private String resName;
	    private String resAddress;
	    private String resTel;
	    private int categoryId;
	    private boolean resEnable;
	    private int userId;
	    private String resCategoryInfo;
	    private String resLat;
	    private String resLon;
	    

	    public Res(int resId, String resName, String resAddress, String resTel, String resCategoryInfo, int categoryId, boolean resEnable, int userId) {
	        this.resId = resId;
	        this.resName = resName;
	        this.resAddress = resAddress;
	        this.resTel = resTel;
	        this.categoryId = categoryId;
	        this.resEnable = resEnable;
	        this.userId = userId;
	        this.resCategoryInfo = resCategoryInfo;
	    }

	    

		public Res(int resId, String resName, String resAddress, String resLat, String resLon, String resTel) {
			this.resLat = resLat;
			this.resLon = resLon;
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


