package server.ress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

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
	public List<Res> getAll() {
		String sql = "SELECT resId, resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId, resEnable, userId, modifyDate "
				+ "FROM Res ORDER BY modifyDate DESC;";
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
				Res res = new Res(resId, resName, resAddress, resLat, resLon, resTel, resHours, resCategoryId,
						resEnable, userId, modifyDate);
				ressList.add(res);
			}
			return ressList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ressList;
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
}
