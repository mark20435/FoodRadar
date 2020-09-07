package server.commentGood;

import java.util.List;

public interface CommentGoodDao {
	
	int insert(CommentGood commentGood);
	int update(CommentGood commentGood);
	int delete(int commentGoodId);
	
	CommentGood findById(int commentGood);
	
	//計算點讚數量
	CommentGood commentGoodCount(CommentGood commentGood);
	
	List<CommentGood> getAll();

}
