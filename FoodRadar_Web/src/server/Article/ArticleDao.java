package server.Article;

import java.util.List;

public interface ArticleDao {
	
	int insert(Article article);
	int update(Article article);
	int delete(int articleId);

	Article findById(int articleId);
	
//	byte[] getImage(int articleId);

	List<Article> getAll();


}
