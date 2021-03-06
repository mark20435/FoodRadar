package server.ress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.category.Category;
import server.img.Img;
import server.main.ServiceLocator;

public class ResDaoMySqlImpl implements ResDao {
	DataSource dataSource;

	public ResDaoMySqlImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public int insert(Res res, byte[] image) {
		int count = 0;
		String sql = "INSERT INTO Res"
				+ "(resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId, resEnable, userId, resImg) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, res.getResName());
			ps.setString(2, res.getResAddress());
			ps.setDouble(3, res.getResLat());
			ps.setDouble(4, res.getResLon());
			ps.setString(5, res.getResTel());
			ps.setString(6, res.getResHours());
			ps.setInt(7, res.getResCategoryId());
			ps.setBoolean(8, res.isResEnable());
			ps.setInt(9, res.getUserId());
			ps.setBytes(10, image);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Res res, byte[] image) {
		int count = 0;
		String sql = "";

		if (image != null) {
			sql = "UPDATE Res SET resName = ?, resAddress = ?, resLat = ?, resLon = ?, "
					+ "resTel = ?, resHours = ?, resCategoryId = ?, resEnable = ?, userId = ?, modifyDate = ?, resImg = ? WHERE resId = ?;";
		} else {
			sql = "UPDATE Res SET resName = ?, resAddress = ?, resLat = ?, resLon = ?, "
					+ "resTel = ?, resHours = ?, resCategoryId = ?, resEnable = ?, userId = ?, modifyDate = ? WHERE resId = ?;";
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, res.getResName());
			ps.setString(2, res.getResAddress());
			ps.setDouble(3, res.getResLat());
			ps.setDouble(4, res.getResLon());
			ps.setString(5, res.getResTel());
			ps.setString(6, res.getResHours());
			ps.setInt(7, res.getResCategoryId());
			ps.setBoolean(8, res.isResEnable());
			ps.setInt(9, res.getUserId());
			ps.setObject(10, res.getModifyDate());
			if (image != null) {
				ps.setBytes(11, image);
				ps.setInt(12, res.getResId());
			} else {
				ps.setInt(11, res.getResId());
			}
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	@Override
	public int delete(int resId) {
		int count = 0;
		String sql = "DELETE FROM Res WHERE resId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Res findById(int resId) {
		String sql = "SELECT resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId, resEnable, userId, modifyDate FROM Res WHERE resId = ?;";
		Res res = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String resName = rs.getString(1);
				String resAddress = rs.getString(2);
				Double resLat = rs.getDouble(3);
				Double resLon = rs.getDouble(4);
				String resTel = rs.getString(5);
				String resHours = rs.getString(6);
				Integer resCategoryId = rs.getInt(7);
				Boolean resEnable = rs.getBoolean(8);
				Integer userId = rs.getInt(9);
				Timestamp modifyDate = rs.getTimestamp(10);
				res = new Res(resId, resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId, resEnable,
						userId, modifyDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	
	@Override
	public List<Res> CategoryfindById(int resId) {
		String sql = "SELECT resId, resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId, resEnable, userId, modifyDate FROM Res WHERE resEnable = 1 AND resCategoryId = ?;";
		List<Res> ressList = new ArrayList<Res>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resId);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Integer ressId = rs.getInt(1);
				String resName = rs.getString(2);
				String resAddress = rs.getString(3);
				Double resLat = rs.getDouble(4);
				Double resLon = rs.getDouble(5);
				String resTel = rs.getString(6);
				String resHours = rs.getString(7);
				Integer resCategoryId = rs.getInt(8);
				Boolean resEnable = rs.getBoolean(9);
				Integer userId = rs.getInt(10);
				Timestamp modifyDate = rs.getTimestamp(11);
				Res res = new Res(ressId, resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId, resEnable,
						userId, modifyDate);
				ressList.add(res);
			}
			return ressList;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ressList;
	}


	@Override
	public List<Res> getAll() {
		String sql = "SELECT resId, resName, resAddress, resLat, resLon, resTel, resHours, R.resCategoryId, resEnable, R.userId, R.modifyDate, resCategoryInfo, userName \n" + 
				"FROM Res R\n" + 
				"left join Category C on R.resCategoryId = C.resCategoryId\n" + 
				"left join UserAccount U on R.userId = U.userId\n" + 
				"ORDER BY modifyDate DESC;";
		List<Res> ressList = new ArrayList<Res>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int resId = rs.getInt(1);
				String resName = rs.getString(2);
				String resAddress = rs.getString(3);
				Double resLat = rs.getDouble(4);
				Double resLon = rs.getDouble(5);
				String resTel = rs.getString(6);
				String resHours = rs.getString(7);
				Integer resCategoryId = rs.getInt(8);
				Boolean resEnable = rs.getBoolean(9);
				Integer userId = rs.getInt(10);
				Timestamp modifyDate = rs.getTimestamp(11);
				String resCategoryInfo = rs.getString(12);
				String userName = rs.getString(13);
				Res res = new Res(resId, resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId,
						resEnable, userId, modifyDate);
				res.setResCategoryInfo(resCategoryInfo);
				res.setUserName(userName);
				ressList.add(res);
			}
			return ressList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ressList;
	}
	
	@Override
	public List<Res> getAllEnable(int curUserId) {
		String sql = "SELECT R.resId, resName, resAddress, resLat, resLon, resTel, resHours, R.resCategoryId, resEnable, R.userId, R.modifyDate, resCategoryInfo, userName, ifnull(avg(rating), -1) as rating, \n" +
				"(select case count(*) when 0 then false else true end from MyRes where resId = R.resId and userId = ?) as isMyRes " +
				"FROM Res R\n" + 
				"left join Category C on R.resCategoryId = C.resCategoryId\n" + 
				"left join UserAccount U on R.userId = U.userId\n" + 
				"left join ResRating RR on R.resId = RR.resId " +
				"WHERE resEnable = 1 " +
				"GROUP BY R.resId " +
				"ORDER BY modifyDate DESC;";
		List<Res> ressList = new ArrayList<Res>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, curUserId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int resId = rs.getInt(1);
				String resName = rs.getString(2);
				String resAddress = rs.getString(3);
				Double resLat = rs.getDouble(4);
				Double resLon = rs.getDouble(5);
				String resTel = rs.getString(6);
				String resHours = rs.getString(7);
				Integer resCategoryId = rs.getInt(8);
				Boolean resEnable = rs.getBoolean(9);
				Integer userId = rs.getInt(10);
				Timestamp modifyDate = rs.getTimestamp(11);
				String resCategoryInfo = rs.getString(12);
				String userName = rs.getString(13);
				Float rating = rs.getFloat(14);
				Boolean myRes = rs.getBoolean(15);
				Res res = new Res(resId, resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId,
						resEnable, userId, modifyDate);
				res.setResCategoryInfo(resCategoryInfo);
				res.setUserName(userName);
				res.setRating(rating);
				res.setMyRes(myRes);
				ressList.add(res);
			}
			return ressList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ressList;
	}
	
	@Override
	public List<Category> getCategories() {
		// Date Time: 2020-09-11 18:35:24
		// select statements : Category
		String sql = "SELECT resCategoryId, resCategoryInfo, resCategorySn" 
		        + " FROM Category;";
		

		List<Category> categoryList  = new ArrayList<Category>();
		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);) {			
			ResultSet rs = ps.executeQuery(sql);
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
				int id = rs.getInt(1);
				String info = rs.getString(2);
				int cateSn = rs.getInt(3);
				

				Category category = new Category(id, info, cateSn);
				categoryList.add(category);
			}
			return categoryList;
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return categoryList;

	}

	@Override
	public byte[] getImage(int resId) {
		String sql = "SELECT resImg FROM Res WHERE resId = ?;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public List<Img> getImgByResId(int resId) {
		String sql = "select imgId, articleId\n" + 
				"from Img\n" + 
				"where articleId in \n" + 
				"	(select articleId \n" + 
				"	 from Article\n" + 
				"     where resId = ?);";
		List<Img> imgs = new ArrayList<Img>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int imgId = rs.getInt(1);
				int articleId = rs.getInt(2);
				
				Img img = new Img(imgId, articleId);
				imgs.add(img);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return imgs;
	}

	@Override
	public int insertResRating(ResRating resRating) {
		int count = 0;
		String sql = "INSERT INTO ResRating"
				+ "(resId, userId, rating) "
				+ "VALUES(?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resRating.getResId());
			ps.setInt(2, resRating.getUserId());
			ps.setFloat(3, resRating.getRating());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public ResRating findRatingByResIdAndUserId(int resId, int userId) {
		String sql = "SELECT resRatingId, rating FROM ResRating WHERE resId = ? and userId = ?;";
		ResRating resRating = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resId);
			ps.setInt(2, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Integer resRatingId = rs.getInt(1);
				Float rating = rs.getFloat(2);
				resRating = new ResRating(resRatingId, resId, userId, rating);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resRating;
	}

	@Override
	public int updateResRating(ResRating resRating) {
		int count = 0;
		String sql = "UPDATE ResRating SET rating = ? "
				+ "WHERE resRatingId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setFloat(1, resRating.getRating());
			ps.setInt(2, resRating.getResRatingId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}
