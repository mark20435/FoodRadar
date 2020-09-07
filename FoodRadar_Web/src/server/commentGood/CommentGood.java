package server.commentGood;

public class CommentGood {
	
	private int commentGoodId;
	private int commentId;
	private int userId;
	
	
	public CommentGood() {
		super();
	}

	public CommentGood(int commentGoodId, int commentId, int userId) {
		super();
		this.commentGoodId = commentGoodId;
		this.commentId = commentId;
		this.userId = userId;
	}

	public int getCommentGoodId() {
		return commentGoodId;
	}

	public void setCommentGoodId(int commentGoodId) {
		this.commentGoodId = commentGoodId;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	

}
