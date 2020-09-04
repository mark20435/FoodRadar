package idv.Food.server.Article;

import java.util.List;

public interface ArticleDao {
	
	int insert(Article article, byte[] image);
	int update(Article article, byte[] image);
	int delete(int articleId);

	Article findById(int articleId);
	
	byte[] getImage(int articleId);

	List<Article> getAll();


}
