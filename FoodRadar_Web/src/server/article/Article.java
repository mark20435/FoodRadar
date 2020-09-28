package server.article;

public class Article {
	
	private int articleId;
	private String articleTitle;
	private String articleTime ;
	private String articleText ;
	private String modifyTime ;
	private String resCategoryInfo;
	private int resId;
	private int userId;
	private String resName;
	private String userName;
	private int conAmount;
	private int conNum;
	private boolean articleStatus;
    private int articleGoodCount;
    private int commentCount;
    private int favoriteCount;
    private int articleGoodId;
    private boolean articleGoodStatus;
    private boolean articleFavoriteStatus;
    private byte[] articleImg;
    private byte[] userIcon;
    private int articleFavoriteId;
	
	public Article() {
		super();
	}

	//ArticleList頁面(新進榜，排行榜，收藏榜)
	public Article(String userName, String resCategoryInfo, String articleTime, String articleTitle, String articleText,
			String resName,int articleGoodCount, int commentCount, int favoriteCount,boolean articleGoodStatus ,boolean articleFavoriteStatus , 
			int articleId, int resId, int userId, int conAmount, int conNum, boolean articleStatus) {
		super();
		this.articleTitle = articleTitle;
		this.articleTime = articleTime;
		this.articleText = articleText;
		this.resCategoryInfo = resCategoryInfo;
		this.resName = resName;
		this.userName = userName;
		this.articleGoodCount = articleGoodCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
        this.articleId = articleId;
        this.resId = resId;
        this.userId = userId;
        this.conAmount = conAmount;
        this.conNum = conNum;
        this.articleStatus = articleStatus;
        this.articleGoodStatus = articleGoodStatus;
        this.articleFavoriteStatus = articleFavoriteStatus;
	}
	
	//點讚
    public Article(int articleGoodId,int userId , int articleId) {
        super();
        this.articleGoodId = articleGoodId;
        this.articleId = articleId;
        this.userId = userId;
    }
    //收藏
    public Article(int userId , int articleId) {
        super();
        this.articleId = articleId;
        this.userId = userId;
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


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public int getArticleGoodId() {
		return articleGoodId;
	}

	public void setArticleGoodId(int articleGoodId) {
		this.articleGoodId = articleGoodId;
	}

	public int getCommentCount() {
		return commentCount;
	}


	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}


	public int getFavoriteCount() {
		return favoriteCount;
	}


	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}


	public byte[] getArticleImg() {
		return articleImg;
	}


	public void setArticleImg(byte[] articleImg) {
		this.articleImg = articleImg;
	}


	public byte[] getUserIcon() {
		return userIcon;
	}


	public void setUserIcon(byte[] userIcon) {
		this.userIcon = userIcon;
	}

	public String getResCategoryInfo() {
		return resCategoryInfo;
	}

	public void setResCategoryInfo(String resCategoryInfo) {
		this.resCategoryInfo = resCategoryInfo;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public boolean isArticleGoodStatus() {
		return articleGoodStatus;
	}

	public void setArticleGoodStatus(boolean articleGoodStatus) {
		this.articleGoodStatus = articleGoodStatus;
	}

	public int getArticleGoodCount() {
		return articleGoodCount;
	}

	public void setArticleGoodCount(int articleGoodCount) {
		this.articleGoodCount = articleGoodCount;
	}

	public boolean isArticleFavoriteStatus() {
		return articleFavoriteStatus;
	}

	public void setArticleFavoriteStatus(boolean articleFavoriteStatus) {
		this.articleFavoriteStatus = articleFavoriteStatus;
	}

	public int getArticleFavoriteId() {
		return articleFavoriteId;
	}

	public void setArticleFavoriteId(int articleFavoriteId) {
		this.articleFavoriteId = articleFavoriteId;
	}

	
	
}