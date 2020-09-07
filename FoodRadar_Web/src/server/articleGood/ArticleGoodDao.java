package server.articleGood;

import java.util.List;

public interface ArticleGoodDao {
	
	int insert(ArticleGood articleGood);
	int update(ArticleGood articleGood);
	int delete(int articleGoodId);
	
	ArticleGood findById(int articleId);
	
	//計算點讚數量
	ArticleGood articleGoodCount(ArticleGood articleGood);
	
	List<ArticleGood> getAll();
}
