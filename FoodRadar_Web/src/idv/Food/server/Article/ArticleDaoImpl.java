package idv.Food.server.Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import idv.Food.server.main.ServiceLocator;

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
	public int insert(Article article, byte[] image) {
		int count = 0; // insert的時候時影響的筆數
		String sql = "INSERT INTO Article"
				+ "(articleTitle, articleTime, articleText, resId, userId, conAmount, conNum)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, article.getArticleTitle());
			ps.setString(2, article.getArtitleTime());
			ps.setString(3, article.getArtitleText());
			ps.setInt(4, article.getResId());
			ps.setInt(5, article.getUserId());
			ps.setInt(6, article.getConAmount());
			ps.setInt(7, article.getConNum());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	//更新資料
	public int update(Article article, byte[] image) {
		int count = 0;
		String sql = "";
		// 更新圖片
		if (image != null) {
			sql = "UPDATE Article SET articleTitle = ?, articleText = ?, modifyTime = ?, resId = ?, "
					+ "conAmount = ?, img = ? WHERE articleId = ?;";
		} else {
			sql = "UPDATE Res SET resName = ?, resAdress = ?, resLat = ?, resLon = ?, "
					+ "resTel = ?, image = ? WHERE resId = ?;";
		}

		return count;
	}

	@Override
	public int delete(int articleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Article findById(int articleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Article> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//取得圖片方法
	public byte[] getImage(int articleId) { 
		String sql = " SELECT img FROM Img WHERE articleId = ?; ";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
			ResultSet rs = ps.executeQuery(sql);
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

}
