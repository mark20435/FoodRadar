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

	// 留言
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
	// 更新留言
	public int update(Comment comment) {
		int count = 0;
		String sql = "";
		sql = "UPDATE Comment SET commentModifyTime = ?, commentText = ? "
				+ "WHERE commentId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, comment.getCommentModifyTime());
			ps.setString(2, comment.getCommentText());
			ps.setInt(3, comment.getCommentId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	//刪除留言 >
	@Override
	public int delete(Comment comment) {
		int count = 0;
		String sql = "UPDATE Comment SET commentStatus = ? WHERE commentId = ? ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setBoolean(1, comment.isCommentStatus());
			ps.setInt(2, comment.getCommentId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// 顯示文章留言(包含點讚)
	@Override
	public List<Comment> findCommentById(int articleId, int userId) {
		List<Comment> commentList = new ArrayList<Comment>();
		System.out.println("articleId: " + articleId);
		String sql = "select \n" + "C.commentId as 'commentId'\n" + ",C.articleId as 'articleId'\n"
				+ ",C.userId as 'userId'\n" + ",C.commentModifyTime as 'commentModifyTime'\n"
				+ ",C.commentStatus as 'commentStatus'\n" + ",C.commentText as 'commentText'\n"
				+ ",C.commentTime as  'commentTime'\n" + ",UA.userName as 'userName'\n"
				+ ",(select count(*) from CommentGood CG where CG.commentId = C.commentId) as 'commentGoodCount'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from CommentGood CG where CG.commentId = C.commentId and CG.userId = ? ) as 'commentGoodStatus'\n"
				+ "FROM Comment C \n" + "join UserAccount UA on C.userId = UA.userId\n"
				+ "where C.commentStatus = 1 and C.articleId =  ?  \n" + "ORDER BY commentTime ASC ;";
		System.out.println("sql: " + sql);
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, userId);
			ps.setInt(2, articleId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int commentId = rs.getInt("commentId");
				String commentTime = rs.getString("commentTime");
				int commentUserId = rs.getInt("userId");
//				int articleId = rs.getInt("articleId");
				String commentModifyTime = rs.getString("commentModifyTime");
				boolean commentStatus = rs.getBoolean("commentStatus");
				String commentText = rs.getString("commentText");
				String userName = rs.getString("userName");
//				int commentGoodId = 1; // rs.getInt("commentGoodId");
				boolean commentGoodStatus = rs.getBoolean("commentGoodStatus");
				int commentGoodCount = rs.getInt("commentGoodCount");
				Comment comment = new Comment(commentId, commentTime, articleId, commentUserId, commentModifyTime,
						commentStatus, commentText, userName, commentGoodStatus, commentGoodCount);
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
				Comment comment = new Comment(commentId, commentTime, articleId, userId, commentModifyTime,
						commentStatus, commentText, userName, commentGoodStatus, commentGoodCount);
				commentList.add(comment);
			}
			return commentList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentList;
	}

	// 找到留言內容 > 修改留言用
	@Override
	public Comment findById(int commentId) {
		String sql = "SELECT commentText, commentModifyTime  FROM FoodRadar.Comment Where commentId = ? ;";
		Comment comment = null;
		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setInt(1, commentId);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String commentText = rs.getString("commentText");
					String commentModifyTime = rs.getString("commentModifyTime");
					comment = new Comment(commentText, commentModifyTime);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comment;
	}

}
