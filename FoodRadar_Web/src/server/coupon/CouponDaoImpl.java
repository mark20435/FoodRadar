package server.coupon;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import server.category.Category;
import server.main.ServiceLocator;
import server.ress.Res;

public class CouponDaoImpl implements CouponDao {
	
	DataSource dataSource;
	
    public CouponDaoImpl() {
    	
	    dataSource = ServiceLocator.getInstance().getDataSource();
    }
    
    public void setDataSource(DataSource dataSource) {
    	
		this.dataSource = dataSource;
	}
	@Override
	public int insert(Coupon coupon, byte[] image) {
		int count = 0;
		String sql = "INSERT INTO Coupon"
				+ "(couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonPhoto, couPonEnable)"
				+ "VALUES(?, ?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, coupon.getCouPonStartDate());
			ps.setString(2, coupon.getCouPonEndDate());
			ps.setBoolean(3, coupon.getCouPonType());
			ps.setString(4, coupon.getcouPonInfo());
			ps.setBytes(5, image);
			ps.setBoolean(6, coupon.getCouPonEnable());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public int update(Coupon coupon, byte[] image) {
		int count = 0;
		String sql = "";
		if(image != null) {
			sql = "UPDATE Coupon SET couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonPhoto, couPonEnable WHERE couPonId = ?;";
		}else {
			sql = "UPDATE Coupon SET couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonEnable WHERE couPonId = ?;";
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setString(1, coupon.getCouPonStartDate());
			ps.setString(2, coupon.getCouPonEndDate());
			ps.setBoolean(3, coupon.getCouPonType());
			ps.setString(4, coupon.getcouPonInfo());
			ps.setBoolean(5, coupon.getCouPonEnable());
			if(image != null) {
				ps.setBytes(6, image);
				ps.setInt(7, coupon.getId());
			}else {
				ps.setInt(6, coupon.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int id) {
		int count = 0;
		String sql = "DELETE FROM Coupon WHERE couponId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public byte[] getImage(int id) {
		String sql = "SELECT couPonPhoto FROM Coupon WHERE couPonId = ?;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
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
	public Coupon findById(int id) {
		String sql = "SELECT resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonEnable FROM Coupon WHERE couPonId = ?;";
		Coupon coupon = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				
				Integer resId = rs.getInt(1);
				String couPonStartDate = rs.getString(2);
				String couPonEndDate = rs.getString(3);
				Boolean couPonType = rs.getBoolean(4);
				String couPonInfo = rs.getString(5);
				Boolean couPonEnable = rs.getBoolean(6);
				Integer couPonId = rs.getInt(7);
				
				coupon = new Coupon(resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonEnable, couPonId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coupon;
	}

	@Override
	public List<Coupon> getAll() {
		String sqlStmt = "SELECT couPonId, resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo" + " FROM Coupon;";
		
		List<Coupon> couponList  = new ArrayList<Coupon>();
		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlStmt);) {			
			ResultSet rs = ps.executeQuery(sqlStmt);
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
				int couPonId = rs.getInt("couPonId");
				int resId = rs.getInt("resId");
				String couPonStartDate = rs.getString("couPonStartDate");
				String couPonEndDate = rs.getString("couPonEndDate");
				Boolean couPonType = rs.getBoolean("couPonType");
				String couPonInfo = rs.getString("couPonInfo");
				
				Coupon coupon = new Coupon(couPonId, couPonStartDate, couPonEndDate, couPonType, couPonInfo);
				couponList.add(coupon);
			}
			return couponList;
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return couponList;

	}
	

	@Override
	public List<Coupon> getAllEnable(int cupUserId) {
		String sql = "SELECT P.couPonId, P.resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo, CouPonEnable, P.userId" + 
				"FROM CouPon P\n" + 
				"left join UserAccount U on P.userId = U.userId\n" +
				"WHERE resEnable = 1 " + 
				"GROUP BY P.resId ";
				
				
		List<Coupon> couponsList = new ArrayList<Coupon>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int couPonId = rs.getInt(1);
				Integer resId = rs.getInt(2);
				String couPonStartDate = rs.getString(3);
				String couPonEndDate = rs.getString(4);
				boolean couPonType = rs.getBoolean(5);
				String couPonInfo = rs.getString(6);
				boolean couPonEnable = rs.getBoolean(7);
				Integer userId = rs.getInt(8);
				Coupon coupon = new Coupon(couPonId, resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonEnable, userId);
				coupon.setcouPonInfo(couPonInfo);			
				couponsList.add(coupon);
			}
			return couponsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return couponsList;
	}

	

//	@Override
//	public List<Coupon> couponfindById(int id) {
//		// TODO Auto-generated method stub
//		return couponfindById(id);
//	}

}
