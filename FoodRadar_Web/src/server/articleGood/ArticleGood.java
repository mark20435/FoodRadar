package server.articleGood;

public class ArticleGood {
	private int articleGoodId;
	private int articleId;
	private int userId;
	
	
	public ArticleGood() {
		super();
	}


	public ArticleGood(int articleGoodId, int articleId, int userId) {
		super();
		this.articleGoodId = articleGoodId;
		this.articleId = articleId;
		this.userId = userId;
	}


	public int getArticleGoodId() {
		return articleGoodId;
	}


	public void setArticleGoodId(int articleGoodId) {
		this.articleGoodId = articleGoodId;
	}


	public int getArticleId() {
		return articleId;
	}


	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	
	
}
