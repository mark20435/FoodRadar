package idv.Food.server.Article;

public class Article {
	
	private int articleId;
	private String articleTitle;
	private String artitleTime ;
	private String artitleText ;
	private String modifyTime ;
	private int resId;
	private int userId;
	private int conAmount;
	private int conNum;
	private boolean articleStatus;
	
	public Article() {
		super();
	}


	public Article(int articleId, String articleTitle, String artitleTime, String artitleText, String modifyTime,
			int resId, int userId, int conAmount, int conNum, boolean articleStatus) {
		super();
		this.articleId = articleId;
		this.articleTitle = articleTitle;
		this.artitleTime = artitleTime;
		this.artitleText = artitleText;
		this.modifyTime = modifyTime;
		this.resId = resId;
		this.userId = userId;
		this.conAmount = conAmount;
		this.conNum = conNum;
		this.articleStatus = articleStatus;
	}


	public int getArticleId() {
		return articleId;
	}


	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}


	public String getArticleTitle() {
		return articleTitle;
	}


	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}


	public String getArtitleTime() {
		return artitleTime;
	}


	public void setArtitleTime(String artitleTime) {
		this.artitleTime = artitleTime;
	}


	public String getArtitleText() {
		return artitleText;
	}


	public void setArtitleText(String artitleText) {
		this.artitleText = artitleText;
	}


	public String getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}


	public int getResId() {
		return resId;
	}


	public void setResId(int resId) {
		this.resId = resId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getConAmount() {
		return conAmount;
	}


	public void setConAmount(int conAmount) {
		this.conAmount = conAmount;
	}


	public int getConNum() {
		return conNum;
	}


	public void setConNum(int conNum) {
		this.conNum = conNum;
	}


	public boolean isArticleStatus() {
		return articleStatus;
	}


	public void setArticleStatus(boolean articleStatus) {
		this.articleStatus = articleStatus;
	}
	
	
	
	

}
