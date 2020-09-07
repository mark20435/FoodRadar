package idv.Food.server.CommentGood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import idv.Food.server.main.ServiceLocator;

public class CommentGoodDaoImpl implements CommentGoodDao {
	DataSource dataSource;

	public CommentGoodDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public CommentGoodDaoImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public int insert(CommentGood commentGood) {
		int count = 0;
		String sql = "INSERT INTO CommentGood" + "(commentId, userId)" + "VALUES(?, ?)";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentGood.getCommentId());
			ps.setInt(2, commentGood.getUserId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	//好像不需要？
	public int update(CommentGood commentGood) {
		int count = 0;
		String sql = "";
		sql = "UPDATE CommentGood SET commentId = ?, userId = ?" + "WHERE commentGoodId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentGood.getCommentId());
			ps.setInt(2, commentGood.getUserId());
			ps.setInt(3, commentGood.getCommentGoodId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int commentGoodId) {
		int count = 0;
		String sql = "DELETE FROM CommentGood WHERE commentGoodId = ? ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentGoodId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public CommentGood findById(int commentId) {
		CommentGood commentGood = null;
		String sql = "SELECT commentGoodId, userId WHERE commentId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentId);
			ResultSet rs = ps.executeQuery(sql);
			if (rs.next()) {
				int commentGoodId = rs.getInt("commentGoodId");
				int userId = rs.getInt("userId");
				commentGood = new CommentGood(commentGoodId, commentId, userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentGood;
	}

	@Override
	// 不確定要不要
	public CommentGood commentGoodCount(CommentGood commentGood) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// 這個需要？
	public List<CommentGood> getAll() {
		String sql = "SELECT commentGoodId, userId " + " FROM CommentGood ORDER BY commentId DESC;";
		List<CommentGood> commentGoodList = new ArrayList<CommentGood>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int commentGoodId = rs.getInt("commentGoodId");
				int commentId = rs.getInt("commentId");
				int userId = rs.getInt("userId");
				CommentGood commentGood = new CommentGood(commentGoodId, commentId, userId);
				commentGoodList.add(commentGood);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentGoodList;
	}

}
