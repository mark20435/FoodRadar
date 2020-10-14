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
	public List<MyArticle> getAllById(int id) {
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
		String sqlStmt = "SELECT A.articleId, A.articleTitle, A.articleTime ";
		sqlStmt += ",(SELECT CASE TRIM(IFNULL(userName,'')) WHEN '' THEN concat('美食雷達',U.userId,'號') ELSE userName END "; 
		sqlStmt +=    " FROM UserAccount U WHERE U.userId =A.userId) AS 'userName'";
		sqlStmt += " FROM Article A WHERE articleId IN (SELECT articleId FROM MyArticle WHERE userId = ?);";
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
				Timestamp articleTime = rs.getTimestamp("articleTime");
				String userName = rs.getString("userName");

				myArticle = new MyArticle(articleId, articleTitle, articleTime, userName);
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
