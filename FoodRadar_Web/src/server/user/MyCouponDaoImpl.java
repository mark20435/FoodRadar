package server.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.google.api.client.util.Data;

import server.coupon.Coupon;
import server.coupon.CouponDao;
import server.coupon.CouponDaoImpl;
import server.main.ServiceLocator;

public class MyCouponDaoImpl implements MyCouponDao{
    DataSource dataSource;
    
	public MyCouponDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	@Override
	public int insert(MyCoupon mycoupon) {
		int count = 0;
		String sql = "INSERT INTO MyCouPon" + "(userId, couPonId) " + "VALUES(?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setInt(1, mycoupon.getUserId());
			ps.setInt(2, mycoupon.getCouPonId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(MyCoupon mycoupon) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int userId, int couPonId) {
		int count = 0;
		String sql = "DELETE MyCouPon" + "WHERE userId = ?, couPonId = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setInt(1, userId);
			ps.setInt(2, couPonId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public MyCoupon findById(int userId) {
		String sqlStmt = "SELECT myCouPonId, userId, couPonId, couPonIsUsed, modifyDate FROM MyCouPon WHERE userId = ?;";
		MyCoupon mycoupon = null;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);){
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				int myCouPonId = rs.getInt("myCouPonId");
				int couPonId = rs.getInt("couPonId");
				boolean couPonIsUsed = rs.getBoolean("couPonIsUsed");
				Timestamp modifyDate = rs.getTimestamp("modifyDate");
				mycoupon = new MyCoupon(myCouPonId, userId, couPonId, couPonIsUsed, modifyDate);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mycoupon;
	}

	@Override
	public List<MyCoupon> getAll() {
		String sqlStmt = "SELECT myCouPonId, userId, couPonId, couPonIsUsed, modifyDate FROM MyCouPon;";
		MyCoupon mycoupon = null;
		List<MyCoupon> myCouponList = new ArrayList<MyCoupon>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ResultSet rs = ps.executeQuery(sqlStmt);
			
			while (rs.next()) {
				int myCouPonId = rs.getInt("myCouPonId");
				int userId = rs.getInt("userId");
				int couPonId = rs.getInt("couPonId");
				boolean couPonIsUsed = rs.getBoolean("couPonIsUsed");
				Timestamp modifyDate = rs.getTimestamp("modifyDate");
				mycoupon = new MyCoupon(myCouPonId, userId, couPonId, couPonIsUsed, modifyDate);
				myCouponList.add(mycoupon);
			}
			return myCouponList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return myCouponList;
	}

	@Override
	public List<MyCoupon> getAllById(int userId) {
		MyCoupon mycoupon = null;
		List<MyCoupon> myCouponList = new ArrayList<MyCoupon>();
		new PubTools().showConsoleMsg("MyCouponDaoImpl.getAllById.userId", String.valueOf(userId));
		
		String sqlStmt = "SELECT Cp.couPonId, Cp.resId, Cp.couPonStartDate, Cp.couPonEndDate, Cp.couPonType, Cp.couPonInfo, Cp.couPonPhoto, Cp.couPonEnable, Cp.userId " + 
		       " FROM MyCouPon Mp Join CouPon Cp ON Mp.couPonId = Cp.couPonId " + 
				" WHERE Mp.couPonIsUsed = 0 AND Cp.couPonType = 1 AND Cp.couPonEnable = 1 AND Mp.userId = ?;";
		new PubTools().showConsoleMsg("MyCouponDaoImpl.getAllById.sqlStmt", sqlStmt);
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);
				){
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int couPonId = rs.getInt("couPonId");
				int resId = rs.getInt("resId");
				String couPonStartDate = rs.getString("couPonStartDate");
				String couPonEndDate = rs.getString("couPonEndDate");
				boolean couPonType = rs.getBoolean("couPonType");
				String couPonInfo = rs.getString("couPonInfo");
				System.out.println("mycocopa");
				byte[] couPonPhoto = rs.getBytes("couPonPhoto");
				boolean couPonEnable = rs.getBoolean("couPonEnable");
				
				mycoupon = new MyCoupon(couPonId, resId, couPonStartDate, couPonEndDate, couPonType, couPonInfo, couPonPhoto, couPonEnable);
				myCouponList.add(mycoupon);	
				
			}
			return myCouponList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
 		
		return myCouponList;
	}

	@Override
	public byte[] getImage(int id) {
		String sql = "SELECT couPonPhoto FROM CouPon WHERE couPonId = ?;";
		System.out.println("getImage.id: " + id);
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			System.out.println("getImage.rs: " + rs);
			if (rs.next()) {
				image = rs.getBytes(1);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public List<Coupon> getCouponById(Integer id, Integer userId) {
		Coupon coupon = null;
		CouponDao couponDao = new CouponDaoImpl();
		List<Coupon> couponList = new ArrayList<Coupon>();
		couponList = couponDao.getAllEnable(userId);
		
		List<Coupon> couponListById = new ArrayList<Coupon>();
		for (Coupon getCoupon : couponList) {
			Integer resID = getCoupon.getCouPonId();
			if (resID.equals(id)) {
				couponListById.add(getCoupon);
				break;
			}
			
		}
		return couponListById;
	}
	
	

}
