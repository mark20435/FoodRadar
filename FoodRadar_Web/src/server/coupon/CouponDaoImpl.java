package server.coupon;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.sql.DataSource;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.google.firebase.auth.UserIdentifier;

import io.opencensus.common.ServerStatsFieldEnums.Id;

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
				+ "(resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonPhoto, couPonEnable, userId)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, coupon.getResId());
			ps.setString(2, coupon.getCouPonStartDate());
			ps.setString(3, coupon.getCouPonEndDate());
			ps.setBoolean(4, coupon.getSpTypeBoolean());
			ps.setString(5, coupon.getcouPonInfo());
			ps.setBytes(6, image);	
			ps.setBoolean(7, coupon.getSpEnableBoolean());
			ps.setInt(8, coupon.getUserId());		
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public int couponLoveInsert(int loginUserId, int couPonId) {
		int count = 0;
//		String sql = "INSERT INTO MyCouPon (userId, couPonId, couPonIsUsed)\n " 
//		+ "( SELECT ?, ?, 1 FROM CouPon WHERE NOT EXISTS ( SELECT * FROM MyCouPon WHERE couPonId = ? AND userId = ? ) LIMIT 1 );";
		
		String sql = "INSERT INTO MyCouPon (userId, couPonId, couPonIsUsed)\n" + "(SELECT ? ,?, 0 FROM CouPon\n"
				+ " WHERE NOT EXISTS(SELECT * FROM MyCouPon WHERE userId = ? AND couPonId = ? ) LIMIT 1\n" + ");";
		System.out.println("SQL:" + sql);
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, loginUserId);
			ps.setInt(2, couPonId);
			ps.setInt(3, loginUserId);
			ps.setInt(4, couPonId);
			System.out.println("userId: " + loginUserId);
			System.out.println("couPonId: " + couPonId);
			count = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public int update(Coupon coupon, byte[] image) {
		int count = 0;
//		String sql = "";
//		if(image != null) {
//			sql = "UPDATE Coupon SET couPonStartDate = ?, couPonEndDate = ?, couPonType = ?, couPonInfo = ?, couPonPhoto = ?, couPonEnable = ?, userId = ? "
//		             +  " WhERE couPonId = ?;";
//		}else {
//			sql = "UPDATE Coupon SET couPonStartDate = ?, couPonEndDate = ?, couPonType = ?, couPonInfo = ?, couPonPhoto = ?, couPonEnable = ?, userId = ? "
//			         +  " WhERE couPonId = ?;";
//		}
		String sql = "UPDATE Coupon SET couPonId = ?, resId = ?, couPonStartDate = ?, couPonEndDate = ?, couPonType = ?, couPonInfo = ?, couPonPhoto = ?, couPonEnable = ?, userId = ? "
	             +  " WhERE couPonId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {	
			System.out.println("couPonId: " + coupon);
			ps.setInt(1, coupon.getCouPonId());
			ps.setInt(2, coupon.getResId());
			ps.setString(3, coupon.getCouPonStartDate());
			ps.setString(4, coupon.getCouPonEndDate());
			ps.setBoolean(5, coupon.getCouPonType());
			ps.setString(6, coupon.getcouPonInfo());
			ps.setBytes(7, image);
			ps.setBoolean(8, coupon.getCouPonEnable());
			ps.setInt(9, coupon.getUserId());
			ps.setInt(10, coupon.getCouPonId());
			count = ps.executeUpdate();
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
	public List<Coupon> getAll(int userId) {
		String sqlStmt = "SELECT C.couPonId, C.resId, C.couPonStartDate, C.couPonEndDate, C.couPonType, C.couPonInfo, C.couPonEnable, C.userId"
				+ ",(select ResName from Res R where R.resId = C.resId) as 'ResName'\n"
				+ ",(select case count(*) when 0 then 0 else 1 end from MyCouPon MC where MC.couPonId = C.couPonId and MC.userId = ?) as 'CouPonLoveStatus'\n" 
				+ " FROM Coupon C;";
		List<Coupon> couponList  = new ArrayList<Coupon>();
		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
				int couPonId = rs.getInt("couPonId");
				int resId = rs.getInt("resId");
				String couPonStartDate = rs.getString("couPonStartDate");
				String couPonEndDate = rs.getString("couPonEndDate");
				Boolean couPonType = rs.getBoolean("couPonType");
				String couPonInfo = rs.getString("couPonInfo");
				Boolean couPonEnable = rs.getBoolean("couPonEnable");
			    userId = rs.getInt("userId");
			    String resName = rs.getString("ResName");
				
				Coupon coupon = new Coupon(couPonId, resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonEnable, userId, resName);
				couponList.add(coupon);
			}
			return couponList;
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return couponList;

	}
	
	@Override
	public List<Coupon> getAllcouPonType(int userId, Boolean couPonType) {
		String sqlStmt = "SELECT C.couPonId, C.resId, C.couPonStartDate, C.couPonEndDate, C.couPonType, C.couPonInfo, C.couPonEnable, C.userId"  
				+ ",(select case count(*) when 0 then 0 else 1 end from MyCouPon MC where MC.couPonId = C.couPonId and MC.userId = ?) as 'CouPonLoveStatus'\n" 
				+ " FROM Coupon C WHERE C.couPonType = ?;";
		System.out.println("couponType :" + sqlStmt);
		List<Coupon> couponList  = new ArrayList<Coupon>();
		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, userId);
			ps.setBoolean(2, couPonType);
//			if (couPonType == true) {
//				ps.setBoolean(2, true);
//			} else {
//				ps.setBoolean(2, false);
//			}
			//System.out.println("getAllcouPonType sql::" + ps.toString());
			ResultSet rs = ps.executeQuery();
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
					int couPonId = rs.getInt("couPonId");
					int resId = rs.getInt("resId");
					String couPonStartDate = rs.getString("couPonStartDate");
					String couPonEndDate = rs.getString("couPonEndDate");
					Boolean couPonTypefromDB = rs.getBoolean("couPonType");
					String couPonInfo = rs.getString("couPonInfo");
					Boolean couPonEnable = rs.getBoolean("couPonEnable");
				    userId = rs.getInt("userId");
				    Boolean CouPonLoveStatus = rs.getBoolean("CouPonLoveStatus");
					
					Coupon coupon = new Coupon(couPonId, resId, couPonStartDate, couPonEndDate, couPonTypefromDB, couPonInfo, couPonEnable, userId, CouPonLoveStatus);
					couponList.add(coupon);
					System.out.println("couPonInfo: " + couPonInfo);
				
				
			}
			return couponList;
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return couponList;

	}

	@Override
	public List<Coupon> getAllEnable(int cupuserId) {
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
