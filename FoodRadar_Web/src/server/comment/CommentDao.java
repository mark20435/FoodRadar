package server.comment;

import java.util.List;

public interface CommentDao {
	
	int insert(Comment comment);
	
	int update(Comment comment);
	
	//刪除留言
	int delete(Comment comment);
	
	//文章內文顯示留言資訊
	List<Comment> findCommentById(int articleId);

	List<Comment> getAll();
	
	Comment findById(int commentId);

}
