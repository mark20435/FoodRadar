package server.article;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

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

	@Override
	public int insert(Article article) {
		int count = 0; // insert的時候時影響的筆數
		String sql = "";
		// 沒修改，不用insert > modifyTime
		sql = "INSERT INTO Article"
				+ "(articleTitle, articleTime, articleText, resId, userId, conAmount, conNum, articleStatus)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, article.getArticleTitle());
			ps.setString(2, article.getArticleTime());
			ps.setString(3, article.getArticleText());
			ps.setInt(4, article.getResId());
			ps.setInt(5, article.getUserId());
			ps.setInt(6, article.getConAmount());
			ps.setInt(7, article.getConNum());
			ps.setBoolean(8, article.isArticleStatus());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 更新資料(包含刪除文章 > 將articleStatus改為false)
	public int update(Article article) {
		int count = 0;
		String sql = "";
		sql = "UPDATE Article SET articleTitle = ?, articleText = ?, modifyTime = ?, resId = ?,"
				+ "userId = ?, conAmount = ?, conNum = ?, articleStatus = ?  WHERE articleId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, article.getArticleTitle());
			ps.setString(2, article.getArticleText());
			ps.setString(3, article.getModifyTime());
			ps.setInt(4, article.getResId());
			ps.setInt(5, article.getUserId());
			ps.setInt(6, article.getConAmount());
			ps.setInt(7, article.getConNum());
			ps.setBoolean(8, article.isArticleStatus());
			ps.setInt(9, article.getArticleId());
			// executeUpdate > 回傳int，更新資訊影響資料的筆數
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 此專案Delete方法很少用
	public int delete(int articleId) {
		int count = 0;
		String sql = "DELETE FROM Article WHERE articleId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 查詢(單一)資料
	public Article findById(int articleId) {
		String sql = "SELECT articleTitle, articleTime, articleText, modifyTime, resId,"
				+ " userId, conAmount, conNum, articleStatus FROM Article WHERE articleId = ?; ";
		Article article = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
			ResultSet rs = ps.executeQuery(sql);
			// 假如有下一個欄位的話，取得其資料
			if (rs.next()) {
				String articleTitle = rs.getString("articleTitle");
				String articleTime = rs.getString("articleTime");
				String articleText = rs.getString("articleText");
				String modifyTime = rs.getString("modifyTime");
				int resId = rs.getInt("resId");
				int userId = rs.getInt("userId");
				int conAmount = rs.getInt("conAmount");
				int conNum = rs.getInt("conNum");
				boolean articleStatus = rs.getBoolean("articleStatus");
//				article = new Article(articleId, articleTitle, articleTime, articleText, modifyTime, resId, userId,
//						conAmount, conNum, articleStatus);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return article;
	}

	@Override
	// 取得(資料庫)欄位資料，並排序方法
	public List<Article> getAllById() {
//		String sql = "SELECT articleId, articleTitle, articleText, modifyTime, resId, userId, conAmount, conNum, articleStatus "
//				+ " FROM Article ORDER BY articleTime DESC;";
		String sql = "{call sp_GetArticle(?)}";
		List<Article> articleList = new ArrayList<Article>();
		try (Connection connection = dataSource.getConnection();
				CallableStatement cs = connection.prepareCall(sql);
				) {
			ResultSet rs = cs.executeQuery();
			while (rs.next()) {
				int articleId = rs.getInt("articleId");
				String articleTitle = rs.getString("articleTitle");
				String articleTime = rs.getString("articleTime");
				String articleText = rs.getString("articleText");
				String resCategoryInfo = rs.getString("resCategoryInfo");
				String resName = rs.getString("resName");
				String userName = rs.getString("userName");
				boolean articleStatus = rs.getBoolean("articleStatus");
				int goodCount = rs.getInt("goodCount");
				int commentCount = rs.getInt("commentCount");
				int favoriteCount = rs.getInt("favoriteCount");
				Article article = new Article(articleId, articleTitle, articleTime, articleText, resCategoryInfo, resName,
						userName, articleStatus, goodCount, commentCount, favoriteCount);
				articleList.add(article);
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

//	@Override
//	// 取得圖片方法
//	public byte[] getImage(int articleId) {
//		String sql = " SELECT img FROM Img WHERE imgId = ?; ";
//		byte[] image = null;
//		try (Connection connection = dataSource.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql);) {
//			ps.setInt(1, Id);
//			ResultSet rs = ps.executeUpdate(sql);
//			if (rs.next()) {
//				image = rs.getBytes(1);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null ;
//	}

}
