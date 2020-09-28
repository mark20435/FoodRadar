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

	//留言
	@Override
	public int insert(Comment comment) {
		int count = 0;
		String sql = "INSERT INTO Comment" + "(commentId, articleId, userId, commentStatus, commentText)"
				+ "VALUES(?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, comment.getCommentId());
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
			ps.setInt(6, comment.getCommentId());
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

	//顯示文章留言(包含點讚)
	@Override
	public List<Comment> findCommentById(int articleId) {
		List<Comment> commentList = new ArrayList<Comment>();
		System.out.println("articleId: " + articleId);
		String sql = "select \n"
				+ "C.commentId as 'commentId'\n"
				+ ",C.articleId as 'articleId'\n"
				+ ",C.userId as 'userId'\n"
				+ ",C.commentModifyTime as 'commentModifyTime'\n"
				+ ",C.commentStatus as 'commentStatus'\n"
				+ ",C.commentText as 'commentText'\n"
				+ ",C.commentTime as  'commentTime'\n"
				+ ",UA.userName as 'userName'\n"
//				+ ",CG.commentGoodId as 'commentGoodId'\n"
				+ ",(select count(*) from CommentGood CG where CG.commentId = C.commentId) as 'commentGoodCount'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from CommentGood CG where CG.commentId = C.commentId and CG.userId = C.userId ) as 'commentGoodStatus'\n"
				+ "FROM Comment C \n"
//				+ "join CommentGood CG on C.commentId = CG.commentId\n"
				+ "join UserAccount UA on C.userId = UA.userId\n"
				+ "where C.commentStatus = 1 and C.articleId =  ? \n"
				+ "ORDER BY commentTime DESC;";
		System.out.println("sql: " + sql);
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int commentId = rs.getInt("commentId");
				String commentTime = rs.getString("commentTime");
				int userId = rs.getInt("userId");
//				int articleId = rs.getInt("articleId");
				String commentModifyTime = rs.getString("commentModifyTime");
				boolean commentStatus = rs.getBoolean("commentStatus");
				String commentText = rs.getString("commentText");
				String userName = rs.getString("userName");
//				int commentGoodId = 1; // rs.getInt("commentGoodId");
				boolean commentGoodStatus = rs.getBoolean("commentGoodStatus");
				int commentGoodCount = rs.getInt("commentGoodCount");
				Comment comment = new Comment(commentId, commentTime, articleId, userId, commentModifyTime, commentStatus,
						commentText, userName, commentGoodStatus, commentGoodCount);
				commentList.add(comment);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentList;
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
				String userName = rs.getString("userName");
				String commentModifyTime = rs.getString("commentModifyTime");
				String commentTime = rs.getString("commentTime");
				boolean commentStatus = rs.getBoolean("commentStatus");
				String commentText = rs.getString("commentText");
				int commentGoodId = rs.getInt("commentGoodId");
				boolean commentGoodStatus = rs.getBoolean("commentGoodStatus");
				int commentGoodCount = rs.getInt("commentGoodCount");
				Comment comment = new Comment(commentId, commentTime, articleId, userId, commentModifyTime, commentStatus,
						commentText, userName, commentGoodStatus, commentGoodCount);
				commentList.add(comment);
			}
			return commentList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentList;
	}

}
