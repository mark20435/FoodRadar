package server.commentGood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.main.ServiceLocator;

public class CommentGoodDaoImpl implements CommentGoodDao {
	DataSource dataSource;

	public CommentGoodDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public CommentGoodDaoImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	//留言點讚
	@Override
	public int insert(CommentGood commentGood) {
		int count = 0;
		String sql = "INSERT INTO CommentGood (commentId, userId)\n"
				+ "(SELECT ? ,? FROM CommentGood\n"
				+ " WHERE NOT EXISTS(SELECT * FROM CommentGood WHERE commentId = ? AND userId = ?) LIMIT 1\n"
				+ ");";	
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentGood.getCommentId());
			ps.setInt(2, commentGood.getUserId());
			ps.setInt(3, commentGood.getCommentId());
			ps.setInt(4, commentGood.getUserId());	
			System.out.println("### sql::" + ps.toString());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	//取消留言點讚
	@Override
	public int delete(int commentId, int userId) {
		int count = 0;
		String sql = "DELETE FROM CommentGood WHERE commentId = ? AND userId = ? ;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentId);
			ps.setInt(2, userId);
			count = ps.executeUpdate();
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
