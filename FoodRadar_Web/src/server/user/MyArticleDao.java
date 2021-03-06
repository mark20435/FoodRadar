package server.user;

import java.util.List;

//Date Time: 2020-10-14 13:31:20
//interface Dao: MyArticleDao
public interface MyArticleDao {
	int insert(MyArticle myarticle);

	int update(MyArticle myarticle, byte[] image);

	int delete(int id);

	MyArticle findById(int id);

	List<MyArticle> myArticle(int id, String articleDate, String action);
	
	List<MyArticle> myArticleMyComment(int id);
	

	byte[] getImage(int id);

	int setEnableStatus(Integer id, Boolean enableStatus);

}
