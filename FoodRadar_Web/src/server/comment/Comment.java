package server.comment;

public class Comment {

	private int commmentId;
	private String commentTime;
	private int articleId;
	private int userId;
	private String commentModifyTime;
	private boolean commentStatus;
	private String commentText;

	Comment() {
		super();
	}

	public Comment(int commmentId, String commentTime, int articleId, int userId, String commentModifyTime,
			boolean commentStatus, String commentText) {
		super();
		this.commmentId = commmentId;
		this.commentTime = commentTime;
		this.articleId = articleId;
		this.userId = userId;
		this.commentModifyTime = commentModifyTime;
		this.commentStatus = commentStatus;
		this.commentText = commentText;
	}

	public int getCommmentId() {
		return commmentId;
	}

	public void setCommmentId(int commmentId) {
		this.commmentId = commmentId;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
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

	public String getCommentModifyTime() {
		return commentModifyTime;
	}

	public void setCommentModifyTime(String commentModifyTime) {
		this.commentModifyTime = commentModifyTime;
	}

	public boolean isCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(boolean commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

}
