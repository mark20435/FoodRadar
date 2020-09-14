package server.articleGood;

import java.util.List;

public interface ArticleGoodDao {
	
	int insert(ArticleGood articleGood);
	int update(ArticleGood articleGood);
	int delete(int articleGoodId);
	
//	ArticleGood findById(int articleGoodId);
	ArticleGood findById(int userId);
	
	List<ArticleGood> getAll();
	List<ArticleGood> getAllCount();
}
