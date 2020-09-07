package server.article;

public class Article {
	
	private int articleId;
	private String articleTitle;
	private String articleTime ;
	private String articleText ;
	private String modifyTime ;
	private int resId;
	private int userId;
	private int conAmount;
	private int conNum;
	private boolean articleStatus;
	
	public Article() {
		super();
	}


	public Article(int articleId, String articleTitle, String articleTime, String articleText, String modifyTime,
			int resId, int userId, int conAmount, int conNum, boolean articleStatus) {
		super();
		this.articleId = articleId;
		this.articleTitle = articleTitle;
		this.articleTime = articleTime;
		this.articleText = articleText;
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


	public String getArticleTime() {
		return articleTime;
	}


	public void setArticleTime(String articleTime) {
		this.articleTime = articleTime;
	}


	public String getArticleText() {
		return articleText;
	}


	public void setArticleText(String articleText) {
		this.articleText = articleText;
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
