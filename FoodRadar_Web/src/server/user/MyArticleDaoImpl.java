package server.user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.main.ServiceLocator;

// Date Time: 2020-10-14 13:31:20
// implements Dao: MyArticleDaoImpl
public class MyArticleDaoImpl implements MyArticleDao{
	DataSource dataSource;

	public MyArticleDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public int insert(MyArticle myarticle) {
		// Date Time: 2020-10-14 13:31:20
		// insert statements : MyArticle
		int count = 0;
//		String sqlStmt = "INSERT INTO MyArticle(myArticleId, userId, articleId, modifyDate) ";
//		sqlStmt += " VALUES( ?, ?, ?, ?);";
		String sqlStmt = "INSERT INTO MyArticle(userId, articleId) ";
		sqlStmt += " VALUES( ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
//			ps.setInt(1, myarticle.getMyArticleId());
			ps.setInt(1, myarticle.getUserId());
			ps.setInt(2, myarticle.getArticleId());
//			ps.setTimestamp(4, myarticle.getModifyDate());

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return count;

	}

	@Override
	public int update(MyArticle myarticle, byte[] image) {
		return 0;
		/*
		// Date Time: 2020-10-14 13:31:20
		// update statements : MyArticle
		int count = 0;
		String sqlStmt = "UPDATE MyArticle ";
		sqlStmt += " SET myArticleId = ?, userId = ?, articleId = ?, modifyDate = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, myarticle.getMyArticleId());
			ps.setInt(2, myarticle.getUserId());
			ps.setInt(3, myarticle.getArticleId());
			ps.setTimestamp(4, myarticle.getModifyDate());

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
		*/

	}

	@Override
	public int delete(int id) {
		// Date Time: 2020-10-14 14:34:03
		// delete statements : MyArticle
		int count = 0;
		String sqlStmt = "DELETE MyArticle WHERE myArticleId = ?; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, id);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return count;
	}

	@Override
	public MyArticle findById(int id) {
		return null;
		/*
		// Date Time: 2020-09-08 08:23:20
		// select statements : MyRes
		String sqlStmt = "SELECT myResId, userId, resId, modifyDate FROM MyRes WHERE userId = ?;";
		MyRes myres = null;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			// 假如有下一個欄位的話，取得其資料
			if (rs.next()) {
				int myResId = rs.getInt("myResId");
//				int userId = rs.getInt("userId");
				int resId = rs.getInt("resId");
				Timestamp modifyDate = rs.getTimestamp("modifyDate");
				myres = new MyRes(myResId, userId, resId, modifyDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return myres;
		 */
		
	}

	@Override
	public List<MyArticle> myArticle(int id, String articleDate, String action) {
		/*
		 SELECT A.articleId, A.articleTitle, A.articleTime
		,(SELECT CASE TRIM(IFNULL(userName,'')) WHEN '' THEN concat('美食雷達',U.userId,'號') ELSE userName END
 		FROM UserAccount U WHERE U.userId =A.userId) AS 'userName'
		FROM Article A
		WHERE articleId IN (SELECT articleId FROM MyArticle WHERE userId = 3); 
		 */
		
		// Date Time: 2020-10-14 14:34:03
		// select statements : MyArticle
		// String sqlStmt = "SELECT myArticleId, userId, articleId, modifyDate FROM MyArticle WHERE userId = ?;";
		String sqlStmt = "SELECT A.articleId, A.articleTitle, A.articleTime, A.articleText ";
		sqlStmt += ",(SELECT CASE TRIM(IFNULL(userName,'')) WHEN '' THEN concat('美食雷達',U.userId,'號') ELSE userName END "; 
		sqlStmt += " FROM UserAccount U WHERE U.userId =A.userId) AS 'userName'";
		switch (action) {
			case "getMyArticleCollect":
				sqlStmt += " FROM Article A WHERE articleId IN (SELECT articleId FROM MyArticle WHERE userId = ?)";
				break;
			case "getMyArticleIsMe":
			case "getArticleByUserPhone":
//				sqlStmt += " FROM Article A WHERE A.userId = ? AND A.articleTime <= ? ";
				sqlStmt += " FROM Article A WHERE A.userId = ? AND date_format(A.articleTime, '%Y-%m-%d') <= ? ";
				break;
			default:
				break;
		}
		sqlStmt += " ORDER BY A.articleTime DESC;";
		MyArticle myArticle = null;

		List<MyArticle> myArticleList  = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
			// PreparedStatement ps = connection.prepareStatement();) {
			// ResultSet rs = ps.executeQuery(sqlStmt);
			PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, id);
			if (action.contentEquals("getArticleByUserPhone")) {
				ps.setString(2, articleDate);	
			}
			ResultSet rs = ps.executeQuery();
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
				int articleId = rs.getInt("articleId");
				String articleTitle = rs.getString("articleTitle");
				Timestamp articleTime = rs.getTimestamp("articleTime");
				String articleText = rs.getString("articleText");
				String userName = rs.getString("userName");

				myArticle = new MyArticle(articleId, articleTitle, articleTime, articleText, userName);
				myArticleList.add(myArticle);
			}
			return myArticleList;
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return myArticleList;

	}

	@Override
	public List<MyArticle> myArticleMyComment(int id) {
		/*
		SELECT A.articleId, A.articleTitle, C.commentId, C.commentTime, C.commentText
		,(SELECT CASE TRIM(IFNULL(userName,'')) WHEN '' THEN concat('美食雷達',U.userId,'號') ELSE userName END
		 FROM UserAccount U WHERE U.userId =A.userId) AS 'userName'
		FROM `Comment` C JOIN Article A
		JOIN (SELECT MAX(commentId) AS 'commentIdMax' ,articleId from `Comment` CM WHERE CM.commentStatus = 1 AND CM.userId = 3
		GROUP BY articleId) CMD
		ON C.articleId = A.articleId
		AND C.commentId = CMD.commentIdMax ORDER BY C.commentTime DESC; 
		 */
		// Date Time: 2020-10-17 21:52:27
		// select statements : Comment
		String sqlStmt = "SELECT A.articleId, A.articleTitle, C.commentId, C.commentTime, C.commentText"; 
		sqlStmt += ",(SELECT CASE TRIM(IFNULL(userName,'')) WHEN '' THEN concat('美食雷達',U.userId,'號') ELSE userName END"; 
		sqlStmt += " FROM UserAccount U WHERE U.userId =A.userId) AS 'userName'"; 
		sqlStmt += " FROM `Comment` C JOIN Article A"; 
		sqlStmt += " JOIN (SELECT MAX(commentId) AS 'commentIdMax' ,articleId from `Comment` CM";
		sqlStmt += " WHERE CM.commentStatus = 1 AND CM.userId = ? GROUP BY articleId) CMD"; 
		sqlStmt += " ON C.articleId = A.articleId AND C.commentId = CMD.commentIdMax";
		sqlStmt += " ORDER BY C.commentTime DESC;";		
		MyArticle myArticle = null;

		List<MyArticle> myArticleList  = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
			// PreparedStatement ps = connection.prepareStatement();) {
			// ResultSet rs = ps.executeQuery(sqlStmt);
			PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
				int articleId = rs.getInt("articleId");
				String articleTitle = rs.getString("articleTitle");
				Integer commentId = rs.getInt("commentId");
				Timestamp commentTime = rs.getTimestamp("commentTime");
				String commentText = rs.getString("commentText");
				String userName = rs.getString("userName");

				myArticle = new MyArticle(articleId, articleTitle, commentId, commentTime, commentText, userName);
				myArticleList.add(myArticle);
			}
			return myArticleList;
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return myArticleList;

	}
	
	@Override
	public byte[] getImage(int id) {
		/*
		 SELECT * FROM Img WHERE articleId = 1 ORDER BY imgId LIMIT 1;
		 */		
		String sql = "SELECT img FROM Img WHERE articleId = ? ORDER BY imgId LIMIT 1;";
		System.out.println("getImage.id: " + id);
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
//			System.out.println("getImage.rs: " + rs);
			if (rs.next()) {
				image = rs.getBytes("img"); // BLOB用byes陣列接收
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return image;
	}

}
