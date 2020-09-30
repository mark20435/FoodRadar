package server.article;

import java.util.List;

import server.article.Article;

public interface ArticleDao {
	
	//發文
	int insert(Article article);
	
	int update(Article article);
	
	int delete(int articleId);

	Article findById(int articleId);
	
	byte[] getImage(int imgId);
	
	//點讚功能
	int articleGoodInsert(Article articleGood);
	
	//取消讚
	int articleGoodDelete(int articleId, int userId);
	
	//收藏文章
	int articleFavoriteInsert(Article articleFavorite);
	
	//取消收藏
	int articleFavoriteDelete(int userId, int articleId);
	
	//新進榜
	List<Article> getAllById();

	//排行榜
	List<Article> getAllByIdRank();
	
	//收藏榜
	List<Article> getAllByIdFavorite();
	
	List<Article> getAll();


}
