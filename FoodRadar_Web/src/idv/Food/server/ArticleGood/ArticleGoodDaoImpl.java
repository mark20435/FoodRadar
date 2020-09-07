package idv.Food.server.ArticleGood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import idv.Food.server.main.ServiceLocator;

public class ArticleGoodDaoImpl implements ArticleGoodDao {
	DataSource dataSource;

	public ArticleGoodDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public ArticleGoodDaoImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public int insert(ArticleGood articleGood) {
		int count = 0;
		String sql = "INSERT INTO ArticleGood" + "(articleId, userId)" + "VALUES(?, ?)";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleGood.getArticleId());
			ps.setInt(2, articleGood.getUserId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 好像不需要？
	public int update(ArticleGood articleGood) {
		int count = 0;
		String sql = "";
		sql = "UPDATE ArticleGood SET userId = ? " + "WHERE articleId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleGood.getUserId());
			ps.setInt(2, articleGood.getArticleId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int articleGoodId) {
		int count = 0;
		String sql = "DELETE FROM ArticleGood WHERE articleGoodId = ? ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleGoodId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public ArticleGood findById(int articleId) {
		ArticleGood articleGood = null;
		String sql = "SELECT articleGoodId, userId WHERE articleId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int articleGoodId = rs.getInt("rticleGoodId");
				int userId = rs.getInt("userId");
				articleGood = new ArticleGood(articleGoodId, articleId, userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleGood;
	}

	@Override
	// 不確定要不要
	public ArticleGood articleGoodCount(ArticleGood articleGood) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleGood> getAll() {
		String sql = "SELECT  articleGoodId, userId " + "FROM ArticleGood ORDER BY articleId DESC;";
		List<ArticleGood> articleGoodList = new ArrayList<ArticleGood>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int articleGoodId = rs.getInt("articleGoodId");
				int articleId = rs.getInt("articleId");
				int userId = rs.getInt("userId");
				ArticleGood articleGood = new ArticleGood(articleGoodId, articleId, userId);
				articleGoodList.add(articleGood);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleGoodList;
	}

}
