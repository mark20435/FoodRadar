package server.comment;

import java.util.List;

public interface CommentDao {
	
	int insert(Comment comment);
	int update(Comment comment);
	int delete(int commentId);

	Comment findById(int commentId);

	List<Comment> getAll();

}
