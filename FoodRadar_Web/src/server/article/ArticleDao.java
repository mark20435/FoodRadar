package server.article;

import java.util.List;

import server.article.Article;
import server.img.Img;

public interface ArticleDao {
	
	//發文
	int insert(Article article);
	
	int update(Article article);
	
	//刪除文章
//	int delete(int articleId);
	int delete(Article article);

	//Detail內文
	Article findById(int loginUserId, int articleId);
	
	int findByIdMax(int Id,Img img, byte[] image);
	
	byte[] getImage(int imgId);
	
	//點讚功能
//	int articleGoodInsert(Article articleGood);
	int articleGoodInsert(int articleId, int loginUserId);
	
	//取消讚
	int articleGoodDelete(int articleId, int userId);
	
	//收藏文章
	int articleFavoriteInsert(int articleId, int loginUserId);
	
	//取消收藏
	int articleFavoriteDelete(int loginUserId, int articleId);
	
	//新進榜
//	List<Article> getAllById();
	List<Article> getAllById(int loginUserId);

	//排行榜
	List<Article> getAllByIdRank(int loginUserId);
	
	//收藏榜
	List<Article> getAllByIdFavorite(int loginUserId);
	
	List<Article> getAll();


}
