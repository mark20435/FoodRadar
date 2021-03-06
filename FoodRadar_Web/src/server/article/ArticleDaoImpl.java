package server.article;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.articleGood.ArticleGood;
import server.img.Img;
import server.main.ServiceLocator;

public class ArticleDaoImpl implements ArticleDao {
	DataSource dataSource;

	public ArticleDaoImpl() {
		// 於無參數建構子內新增ServiceLocator的方法(getInstance())，用於連接取得資料庫資料
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public ArticleDaoImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	//發文
	@Override
	public int insert(Article article) {
		int count = 0; // insert的時候時影響的筆數
		String sql = "";
		// 沒修改，不用insert > modifyTime
		sql = "INSERT INTO Article"
				+ "(articleTitle, articleText, resId, userId, conAmount, conNum, articleStatus)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, article.getArticleTitle());
			ps.setString(2, article.getArticleText());
			ps.setInt(3, article.getResId());
			ps.setInt(4, article.getUserId());
			ps.setInt(5, article.getConAmount());
			ps.setInt(6, article.getConNum());
			ps.setBoolean(7, article.isArticleStatus());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 更新文章
	public int update(Article article) {
		int count = 0;
		String sql = "";
		sql = "UPDATE Article SET articleTitle = ?, articleText = ?, modifyTime = ?,"
				+ "conAmount = ?, conNum = ?  WHERE articleId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, article.getArticleTitle());
			ps.setString(2, article.getArticleText());
			ps.setString(3, article.getModifyTime());
			ps.setInt(4, article.getConAmount());
			ps.setInt(5, article.getConNum());
			ps.setInt(6, article.getArticleId());
			// executeUpdate > 回傳int，更新資訊影響資料的筆數
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 刪除文章 > 將articleStatus改為false
	public int delete(Article article) {
		int count = 0;
		String sql = "";
		sql = "UPDATE Article SET articleStatus = ?  WHERE articleId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setBoolean(1, article.isArticleStatus());
			ps.setInt(2, article.getArticleId());
			// executeUpdate > 回傳int，更新資訊影響資料的筆數
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 查詢(單一)資料，文章內文
	public Article findById(int loginUserId, int articleId) {
		Article article = null;
		String sql = "select\n" + 
				"case when UA.userAvatar is null Then NULL Else UA.userAvatar end as 'userAvatar'\n" + 
				",case when UA.userName = '' Then '無名的食客' Else UA.userName end as 'userName'\n" + 
				",C.resCategoryInfo as 'resCategoryInfo'\n" + 
				",A.articleTime as 'articleTime'\n" + 
				",A.articleTitle as 'articleTitle'\n" + 
				",A.articleText as 'articleText'\n" + 
				",R.resName as 'resName'\n" + 
				",(select count(*) from ArticleGood AC where AC.articleId = A.articleId) as 'articleGoodCount'\n" + 
				",(select count(*) from Comment CO where CO.commentStatus=1 and CO.articleId = A.articleId) as 'commentCount'\n" + 
				",(select count(*) from MyArticle MA where MA.articleId = A.articleId) as 'favoriteCount'\n" + 
				",(select case count(*) when 0 then 0 else 1 end from ArticleGood AG where AG.articleId = A.articleId and AG.userId = ? ) as 'articleGoodStatus'\n" + 
				",(select case count(*) when 0 then 0 else 1 end from MyArticle MA where MA.articleId = A.articleId and MA.userId = ? ) as 'articleFavoriteStatus'\n" + 
				",A.articleId as 'articleId'\n" + 
				",A.resId as 'resId'\n" + 
				",A.userId as 'userId'\n" + 
				",A.conAmount as 'conAmount'\n" + 
				",A.conNum as 'conNum'\n" + 
				",A.articleStatus as 'articleStatus'\n" + 
				",A.modifyTime as 'modifyTime'\n" + 
				" FROM Article A\n" + 
				" join UserAccount UA on A.userId = UA.userId\n" + 
				" join Res R on A.resId = R.resId\n" + 
				" join Category C on R.resCategoryId = C.resCategoryId\n" + 
				"where A.articleStatus = 1 and A.articleId = ? ; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, loginUserId);
			ps.setInt(2, loginUserId);
			ps.setInt(3, articleId);
			System.out.println("SQL:" + sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				boolean articleStatus = rs.getBoolean("articleStatus");
				// 發文狀態為0的資料不抓取，不會顯示在前端
				if (articleStatus == true) {
					String userName = rs.getString("userName");
					String resCategoryInfo = rs.getString("resCategoryInfo");
					String articleTime = rs.getString("articleTime");
					String articleTitle = rs.getString("articleTitle");
					String articleText = rs.getString("articleText");
					String resName = rs.getString("resName");
					int articleGoodCount = rs.getInt("articleGoodCount");
					int commentCount = rs.getInt("commentCount");
					int favoriteCount = rs.getInt("favoriteCount");
					boolean articleGoodStatus = rs.getBoolean("articleGoodStatus");
					boolean aritcleFavoriteStatus = rs.getBoolean("articleFavoriteStatus");
					int resId = rs.getInt("resId");
					int userId = rs.getInt("userId");
					int conAmount = rs.getInt("conAmount");
					int conNum = rs.getInt("conNum");
					String modifyTime = rs.getString("modifyTime");
					article = new Article(userName, resCategoryInfo, articleTime, articleTitle, articleText, resName,
							articleGoodCount, commentCount, favoriteCount, articleGoodStatus, aritcleFavoriteStatus,
							articleId, resId, userId, conAmount, conNum, articleStatus, modifyTime);
//					articleList.add(article);
				} else {
					return null;
				}
			}
			return article;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return article;
	}
	
	@Override
	// 查詢(單一)資料，文章MaxId並 insert圖片
	public int findByIdMax(int Id,Img img, byte[] image) {
		Article article = null;
		String sql = "select Max(articleId) From Article; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			System.out.println("SQL:" + sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
					int articleId = rs.getInt("articleId");
					article = new Article(articleId);
			}
//			return article;
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		return article;
		int count = 0;
		String sqlImg = "INSERT INTO Img" + "(articleId, img)" + "VALUES(?,?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlImg);) {
			ps.setInt(1, article.getArticleId());
			ps.setBytes(2, image);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 取得(資料庫)欄位資料，並排序方法 > 新進榜
	public List<Article> getAllById(int loginUserId) {
		Article article = null;
		String sql = "select\n" + "case when UA.userAvatar is null Then NULL Else UA.userAvatar end as 'userAvatar'\n"
				+ ",case when UA.userName = '' Then '無名的食客' Else UA.userName end as 'userName'\n"
				+ ",C.resCategoryInfo as 'resCategoryInfo'\n" + ",A.articleTime as 'articleTime'\n"
				+ ",A.articleTitle as 'articleTitle'\n" + ",A.articleText as 'articleText'\n"
				+ ",R.resName as 'resName'\n"
				+ ",(select count(*) from ArticleGood AC where AC.articleId = A.articleId) as 'articleGoodCount'\n"
				+ ",(select count(*) from Comment CO where CO.commentStatus=1 and CO.articleId = A.articleId) as 'commentCount'\n"
				+ ",(select count(*) from MyArticle MA where MA.articleId = A.articleId) as 'favoriteCount'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from ArticleGood AG where AG.articleId = A.articleId and AG.userId = ? ) as 'articleGoodStatus'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from MyArticle MA where MA.articleId = A.articleId and MA.userId = ?) as 'articleFavoriteStatus'\n"
				+ ",A.articleId as 'articleId'\n" 
				+ ",A.resId as 'resId'\n" + ",A.userId as 'userId'\n"
				+ ",A.conAmount as 'conAmount'\n" + ",A.conNum as 'conNum'\n" + ",A.articleStatus as 'articleStatus'\n"
				+ ",A.modifyTime as 'modifyTime'\n" 
				+ " FROM Article A\n" + " join UserAccount UA on A.userId = UA.userId\n"
				+ " join Res R on A.resId = R.resId\n" 
//				+ " join Img I on A.articleId = I.articleId\n"
				+ " join Category C on R.resCategoryId = C.resCategoryId\n" + "where A.articleStatus = 1\n"
				+ "order by A.articleTime DESC;\n" + "";
		List<Article> articleList = new ArrayList<Article>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {		
			ps.setInt(1, loginUserId);
			ps.setInt(2, loginUserId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				boolean articleStatus = rs.getBoolean("articleStatus");
				// 發文狀態為0的資料不抓取，不會顯示在前端
				if (articleStatus == true) {
					String userName = rs.getString("userName");
					String resCategoryInfo = rs.getString("resCategoryInfo");
					String articleTime = rs.getString("articleTime");
					String articleTitle = rs.getString("articleTitle");
					String articleText = rs.getString("articleText");
					String resName = rs.getString("resName");
					int articleGoodCount = rs.getInt("articleGoodCount");
					int commentCount = rs.getInt("commentCount");
					int favoriteCount = rs.getInt("favoriteCount");
					boolean articleGoodStatus = rs.getBoolean("articleGoodStatus");
					boolean aritcleFavoriteStatus = rs.getBoolean("articleFavoriteStatus");
					int articleId = rs.getInt("articleId");
					int resId = rs.getInt("resId");
					int userId = rs.getInt("userId");
					int conAmount = rs.getInt("conAmount");
					int conNum = rs.getInt("conNum");
					String modifyTime = rs.getString("modifyTime");
					article = new Article(userName, resCategoryInfo, articleTime, articleTitle, articleText, resName,
							articleGoodCount, commentCount, favoriteCount, articleGoodStatus, aritcleFavoriteStatus,
							articleId, resId, userId, conAmount, conNum, articleStatus, modifyTime);
					articleList.add(article);
				} else {
					return null;
				}
			}
			return articleList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleList;
	}

	@Override
	public byte[] getImage(int imgId) {
		String sql = " SELECT img FROM Img WHERE imgId = ?; ";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, imgId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public List<Article> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	// 點讚功能
	@Override
	public int articleGoodInsert(int articleId, int loginUserId) {
		int count = 0;
		String sql = "INSERT INTO ArticleGood (articleId, userId)\n" + "(SELECT ? ,? FROM ArticleGood\n"
				+ " WHERE NOT EXISTS(SELECT * FROM ArticleGood WHERE articleId = ? AND userId = ?) LIMIT 1\n" + ");";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
//			ps.setInt(1, articleGood.getArticleId());
//			ps.setInt(2, articleGood.getUserId());
//			ps.setInt(3, articleGood.getArticleId());
//			ps.setInt(4, articleGood.getUserId());
			ps.setInt(1, articleId);
			ps.setInt(2, loginUserId);
			ps.setInt(3, articleId);
			ps.setInt(4, loginUserId);
			count = ps.executeUpdate();
			System.out.println("count: " + count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// 取消點讚
	@Override
	public int articleGoodDelete(int articleId, int userId) {
		int count = 0;
		String sql = "DELETE FROM ArticleGood WHERE articleId = ? AND userId = ? ;";
//		System.out.println("articleId: " + articleId);
//		System.out.println("userId: " + articleId);
//		System.out.println("sql: " + sql);
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
			ps.setInt(2, userId);
			count = ps.executeUpdate();
			System.out.println("count: " + count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// 收藏功能
	@Override
	public int articleFavoriteInsert(int articleId, int loginUserId) {
		int count = 0;
		String sql = "INSERT INTO MyArticle (userId, articleId)\n" + "(SELECT ? ,? FROM MyArticle\n"
				+ "WHERE NOT EXISTS(SELECT * FROM MyArticle WHERE userId = ? AND articleId = ?) LIMIT 1\n" + ");";
		System.out.println("SQL:" + sql);
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
//			ps.setInt(1, articleFavorite.getUserId());
//			ps.setInt(2, articleFavorite.getArticleId());
//			ps.setInt(3, articleFavorite.getUserId());
//			ps.setInt(4, articleFavorite.getArticleId());
			ps.setInt(1, loginUserId);
			ps.setInt(2, articleId);
			ps.setInt(3, loginUserId);
			ps.setInt(4, articleId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// 取消收藏
	@Override
	public int articleFavoriteDelete(int loginUserId, int articleId) {
		int count = 0;
		String sql = "DELETE FROM MyArticle WHERE userId = ? AND articleId = ? ;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, loginUserId);
			ps.setInt(2, articleId);
			count = ps.executeUpdate();
			System.out.println("count: " + count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// 排行榜
	@Override
	public List<Article> getAllByIdRank(int loginUserId) {
		Article article = null;
		System.out.println("Article:" + article);
		String sql = "select\n" + "case when UA.userAvatar is null Then NULL Else UA.userAvatar end as 'userAvatar'\n"
				+ ",case when UA.userName = '' Then '無名的食客' Else UA.userName end as 'userName'\n"
				+ ",C.resCategoryInfo as 'resCategoryInfo'\n" + ",A.articleTime as 'articleTime'\n"
				+ ",A.articleTitle as 'articleTitle'\n" + ",A.articleText as 'articleText'\n"
				+ ",R.resName as 'resName'\n"
				+ ",(select count(*) from ArticleGood AC where AC.articleId = A.articleId) as 'articleGoodCount'\n"
				+ ",(select count(*) from Comment CO where CO.commentStatus=1 and CO.articleId = A.articleId) as 'commentCount'\n"
				+ ",(select count(*) from MyArticle MA where MA.articleId = A.articleId) as 'favoriteCount'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from ArticleGood AG where AG.articleId = A.articleId and AG.userId = ? ) as 'articleGoodStatus'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from MyArticle MA where MA.articleId = A.articleId and MA.userId = ? ) as 'articleFavoriteStatus'\n"
				+ ",A.articleId as 'articleId'\n" + ",A.resId as 'resId'\n" + ",A.userId as 'userId'\n"
				+ ",A.conAmount as 'conAmount'\n" + ",A.conNum as 'conNum'\n" + ",A.articleStatus as 'articleStatus'\n"
				+ ",A.modifyTime as 'modifyTime'\n" 
				+ " FROM Article A\n" + " join UserAccount UA on A.userId = UA.userId\n"
				+ " join Res R on A.resId = R.resId\n" 
//				+ " join Img I on A.articleId = I.articleId\n"
				+ " join Category C on R.resCategoryId = C.resCategoryId\n" + "where A.articleStatus = 1\n"
				+ "order by articleGoodCount DESC; ";
		List<Article> articleList = new ArrayList<Article>();
//		System.out.println("articleList:"+ articleList);
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, loginUserId);
			ps.setInt(2, loginUserId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				boolean articleStatus = rs.getBoolean("articleStatus");
				// 發文狀態為0的資料不抓取，不會顯示在前端
				if (articleStatus == true) {
					String userName = rs.getString("userName");
					String resCategoryInfo = rs.getString("resCategoryInfo");
					String articleTime = rs.getString("articleTime");
					String articleTitle = rs.getString("articleTitle");
					String articleText = rs.getString("articleText");
					String resName = rs.getString("resName");
					int articleGoodCount = rs.getInt("articleGoodCount");
					int commentCount = rs.getInt("commentCount");
					int favoriteCount = rs.getInt("favoriteCount");
					boolean articleGoodStatus = rs.getBoolean("articleGoodStatus");
					boolean aritcleFavoriteStatus = rs.getBoolean("articleFavoriteStatus");
					int articleId = rs.getInt("articleId");
					int resId = rs.getInt("resId");
					int userId = rs.getInt("userId");
					int conAmount = rs.getInt("conAmount");
					int conNum = rs.getInt("conNum");
					String modifyTime = rs.getString("modifyTime");
					article = new Article(userName, resCategoryInfo, articleTime, articleTitle, articleText, resName,
							articleGoodCount, commentCount, favoriteCount, articleGoodStatus, aritcleFavoriteStatus,
							articleId, resId, userId, conAmount, conNum, articleStatus, modifyTime);
					articleList.add(article);
				} else {
					return null;
				}
			}
			return articleList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleList;

	}

	// 收藏榜
	@Override
	public List<Article> getAllByIdFavorite(int loginUserId) {
		Article article = null;
		String sql = "select\n" + "case when UA.userAvatar is null Then NULL Else UA.userAvatar end as 'userAvatar'\n"
				+ ",case when UA.userName = '' Then '無名的食客' Else UA.userName end as 'userName'\n"
				+ ",C.resCategoryInfo as 'resCategoryInfo'\n" + ",A.articleTime as 'articleTime'\n"
				+ ",A.articleTitle as 'articleTitle'\n" + ",A.articleText as 'articleText'\n"
				+ ",R.resName as 'resName'\n"
				+ ",(select count(*) from ArticleGood AC where AC.articleId = A.articleId) as 'articleGoodCount'\n"
				+ ",(select count(*) from Comment CO where CO.commentStatus=1 and CO.articleId = A.articleId) as 'commentCount'\n"
				+ ",(select count(*) from MyArticle MA where MA.articleId = A.articleId) as 'favoriteCount'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from ArticleGood AG where AG.articleId = A.articleId and AG.userId = ? ) as 'articleGoodStatus'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from MyArticle MA where MA.articleId = A.articleId and MA.userId = ? ) as 'articleFavoriteStatus'\n"
				+ ",A.articleId as 'articleId'\n" + ",A.resId as 'resId'\n" + ",A.userId as 'userId'\n"
				+ ",A.conAmount as 'conAmount'\n" + ",A.conNum as 'conNum'\n" + ",A.articleStatus as 'articleStatus'\n"
				+ ",A.modifyTime as 'modifyTime'\n" 
				+ " FROM Article A\n" + " join UserAccount UA on A.userId = UA.userId\n"
				+ " join Res R on A.resId = R.resId\n" 
//				+ " join Img I on A.articleId = I.articleId\n"
				+ " join Category C on R.resCategoryId = C.resCategoryId\n" + "where A.articleStatus = 1\n"
				+ "order by favoriteCount DESC;";
		List<Article> articleList = new ArrayList<Article>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, loginUserId);
			ps.setInt(2, loginUserId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				boolean articleStatus = rs.getBoolean("articleStatus");
				// 發文狀態為0的資料不抓取，不會顯示在前端
				if (articleStatus == true) {
					String userName = rs.getString("userName");
					String resCategoryInfo = rs.getString("resCategoryInfo");
					String articleTime = rs.getString("articleTime");
					String articleTitle = rs.getString("articleTitle");
					String articleText = rs.getString("articleText");
					String resName = rs.getString("resName");
					int articleGoodCount = rs.getInt("articleGoodCount");
					int commentCount = rs.getInt("commentCount");
					int favoriteCount = rs.getInt("favoriteCount");
					boolean articleGoodStatus = rs.getBoolean("articleGoodStatus");
					boolean aritcleFavoriteStatus = rs.getBoolean("articleFavoriteStatus");
					int articleId = rs.getInt("articleId");
					int resId = rs.getInt("resId");
					int userId = rs.getInt("userId");
					int conAmount = rs.getInt("conAmount");
					int conNum = rs.getInt("conNum");
					String modifyTime = rs.getString("modifyTime");
					article = new Article(userName, resCategoryInfo, articleTime, articleTitle, articleText, resName,
							articleGoodCount, commentCount, favoriteCount, articleGoodStatus, aritcleFavoriteStatus,
							articleId, resId, userId, conAmount, conNum, articleStatus, modifyTime);
					articleList.add(article);
				} else {
					return null;
				}
			}
			return articleList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleList;
	}

}
