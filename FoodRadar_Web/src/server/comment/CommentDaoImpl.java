package server.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.main.ServiceLocator;

public class CommentDaoImpl implements CommentDao {
	DataSource dataSource;

	// 1.撰寫無參數建構子方法 > ServiceLocator類別
	public CommentDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public CommentDaoImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public int insert(Comment comment) {
		int count = 0;
		String sql = "INSERT INTO Comment" + "(commentTime, articleId, userId, commentStatus, commentText)"
				+ "VALUES(?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, comment.getCommentTime());
			ps.setInt(2, comment.getArticleId());
			ps.setInt(3, comment.getUserId());
			ps.setBoolean(4, comment.isCommentStatus());
			ps.setString(5, comment.getCommentText());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 更新資料(包含刪除留言)
	public int update(Comment comment) {
		int count = 0;
		String sql = "";
		sql = "UPDATE Comment SET articleId = ?, userId = ?, commentModifyTime = ?, commentStatus = ?, commentText = ?"
				+ "WHERE commentId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, comment.getArticleId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3, comment.getCommentModifyTime());
			ps.setBoolean(4, comment.isCommentStatus());
			ps.setString(5, comment.getCommentText());
			ps.setInt(6, comment.getCommmentId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int commentId) {
		int count = 0;
		String sql = "DELETE FROM Comment WHERE commentId = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Comment findById(int commentId) {
		Comment comment = null;
		String sql = "SELECT commentTime, articleId, userId, commentModifyTime, commentStatus, commentText"
				+ " FROM Comment WHERE commentId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, commentId);
			ResultSet rs = ps.executeQuery(sql);
			if (rs.next()) {
				String commentTime = rs.getString("commentTime");
				int articleId = rs.getInt("articleId");
				int userId = rs.getInt("userId");
				String commentModifyTime = rs.getString("commentModifyTime");
				boolean commentStatus = rs.getBoolean("commentStatus");
				String commentText = rs.getString("commentText");
				comment = new Comment(commentId, commentTime, articleId, userId, commentModifyTime, commentStatus,
						commentText);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comment;
	}

	@Override
	public List<Comment> getAll() {
		String sql = "SELECT commentId, articleId, userId, commentModifyTime, commentStatus, commentText "
				+ " FROM Comment ORDER BY commentTime DESC;";
		List<Comment> commentList = new ArrayList<Comment>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int commentId = rs.getInt("commentId");
				int articleId = rs.getInt("articleId");
				int userId = rs.getInt("userId");
				String commentModifyTime = rs.getString("commentModifyTime");
				String commentTime = rs.getString("commentTime");
				boolean commentStatus = rs.getBoolean("commentStatus");
				String commentText = rs.getString("commentText");
				Comment comment = new Comment(commentId, commentTime, articleId, userId, commentModifyTime, commentStatus,
						commentText);
				commentList.add(comment);
			}
			return commentList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentList;
	}

}
