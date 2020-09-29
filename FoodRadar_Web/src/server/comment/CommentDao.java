package server.comment;

import java.util.List;

public interface CommentDao {
	
	int insert(Comment comment);
	int update(Comment comment);
	int delete(int commentId);
	
	//文章內文顯示留言資訊
	List<Comment> findCommentById(int articleId);

	List<Comment> getAll();

}
