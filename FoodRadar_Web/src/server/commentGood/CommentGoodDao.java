package server.commentGood;

import java.util.List;

public interface CommentGoodDao {
	
	//留言點讚
	int insert(CommentGood commentGood);
	
	int update(CommentGood commentGood);
	
	int delete(int commentId, int userId);
	
	CommentGood findById(int commentGood);
	
	List<CommentGood> getAll();

}
