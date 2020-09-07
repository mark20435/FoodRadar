package idv.Food.server.Foods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import idv.Food.server.main.ServiceLocator;



public class ResDaoMySqlImpl implements ResDao{
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
		String sql = "INSERT INTO Res" + 
				"(resName, resAddress, resLat, resLon, resTel) "	+ 
				"VALUES(?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ps.setString(1, res.getResName());
			ps.setString(2, res.getResAddress());
			ps.setString(3, res.getResLat());
			ps.setString(4, res.getResLon());
			ps.setString(5, res.getResTel());
			ps.setBytes(6, image);
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
					+ "resTel = ?, image = ? WHERE resId = ?;";
		} else {
			sql = "UPDATE Res SET resName = ?, resAddress = ?, resLat = ?, resLon = ?, "
					+ "resTel = ?, image = ? WHERE resId = ?;";
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			
			ps.setString(1, res.getResName());
			ps.setString(2, res.getResAddress());
			ps.setString(3, res.getResLat());
			ps.setString(4, res.getResLon());
			ps.setString(5, res.getResTel());
			if (image != null) {
				ps.setBytes(6, image);
				ps.setInt(7, res.getResId());
			} else {
				ps.setInt(7, res.getResId());
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
		String sql = "SELECT resId, resName, resAddress, resLat, resLon, resTel, FROM Res WHERE resId = ?;";
		Res res = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, resId);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String resName = rs.getString(1);
				String resAddress = rs.getString(2);
				String resTel = rs.getString(5);
				String resLat = rs.getString(3);
				String resLon = rs.getString(4);
				res = new Res(resId, resName, resAddress, resLat, resLon, resTel);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return res;
	}

	@Override
	public List<Res> getAll() {
		String sql = "SELECT resId, resName, resAddress, resTel, resLat, resLon " 
				+ "FROM Res ORDER BY modifyDate DESC;";
		List<Res> ressList = new ArrayList<Res>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int resId = rs.getInt(1);
				String resName = rs.getString(2);
				String resAddress = rs.getString(3);
				String resTel = rs.getString(4);
				String resLat = rs.getString(5);
				String resLon = rs.getString(6);
				Res res = new Res(resId, resName, resAddress, resTel, resLat, resLon);
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
		String sql = "SELECT image FROM Res WHERE resId = ?;";
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


