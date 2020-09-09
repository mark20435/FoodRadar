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

// Date Time: 2020-09-08 08:14:26
// implements Dao: MyResDaoImpl
public class MyResDaoImpl implements MyResDao{
	DataSource dataSource;

	public MyResDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public int insert(MyRes myres) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(MyRes myres) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MyRes findById(int userId) {
		// Date Time: 2020-09-08 08:23:20
		// select statements : MyRes
		String sqlStmt = "SELECT myResId, userId, resId, modifyDate FROM MyRes WHERE userId = ?;";
		MyRes myres = null;
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery(sqlStmt);
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
	}

	@Override
	public List<MyRes> getAll() {
		// Date Time: 2020-09-08 08:23:20
		// select statements : MyRes
		String sqlStmt = "SELECT myResId, userId, resId, modifyDate FROM MyRes;";
		MyRes myres = null;
		List<MyRes> myResList = new ArrayList<MyRes>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ResultSet rs = ps.executeQuery(sqlStmt);
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
				int myResId = rs.getInt("myResId");
				int userId = rs.getInt("userId");
				int resId = rs.getInt("resId");
				Timestamp modifyDate = rs.getTimestamp("modifyDate");
				myres = new MyRes(myResId, userId, resId, modifyDate);
				myResList.add(myres);
			}
			return myResList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return myResList;
		
	}

	@Override
	public List<MyRes> getAllById(int userId) {
		MyRes myres = null;
		List<MyRes> myResList = new ArrayList<MyRes>();
		System.out.println("userId: " + userId);
		
	    try (
	    	Connection connection = dataSource.getConnection();	    		
	    	CallableStatement cs = connection.prepareCall("{call sp_GetMyRes(?)}"); // 設定 CallableStatement
	    	)
	    {	    	
	    	cs.setInt(1, userId); // 設定 IN 參數的 Index 及值	
//	    	System.out.println("getAllById.userId: " + userId);
	    	cs.execute(); // 執行 CallableStatement
	    	ResultSet rs = cs.getResultSet(); //用 ResultSet 接回 getResultSet 的查詢結果
//	    	System.out.println("getAllById.ResultSet rs: " + rs);
			while (rs.next()) { // 假如有下一個欄位的話，取得其資料
//				R.resId, R.resName, R.resHours, R.resTel, R.resAddress, R.resImg
				int resId = rs.getInt("resId");
				String resName = rs.getString("resName");
				String resHours = rs.getString("resHours");
				String resTel = rs.getString("resTel");
				String resAddress = rs.getString("resAddress");
				byte[] resImg = rs.getBytes("resImg");
//				Timestamp modifyDate = rs.getTimestamp("modifyDate");
				myres =  new MyRes(resId, resName, resHours, resTel, resAddress, resImg);
				myResList.add(myres);
			}
			return myResList;	    	
	    } catch (SQLException e) {
//	    	System.out.println("call sp_GetMyRes");
	    	e.printStackTrace();	    	
	    }
	    
	    
		// Date Time: 2020-09-08 08:23:20
		// select statements : MyRes
//		String sqlStmt = "SELECT myResId, userId, resId, modifyDate FROM MyRes WHERE userId = ?;";
//	    MyRes myres = null;
//		List<MyRes> myResList = new ArrayList<MyRes>();
//		try (Connection connection = dataSource.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
//			ps.setInt(1, userId);
//			ResultSet rs = ps.executeQuery(sqlStmt);
//			// 假如有下一個欄位的話，取得其資料
//			while (rs.next()) {
//				int myResId = rs.getInt("myResId");
////				int userId = rs.getInt("userId");
//				int resId = rs.getInt("resId");
//				Timestamp modifyDate = rs.getTimestamp("modifyDate");
//				myres = new MyRes(myResId, userId, resId, modifyDate);
//				myresList.add(myres);
//			}
//			return myresList;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	    
		return myResList;
	}
	
	@Override
	public byte[] getImage(int id) {
		String sql = "SELECT resImg FROM Res WHERE resId = ?;";
		System.out.println("getImage.id: " + id);
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			System.out.println("getImage.rs: " + rs);
			if (rs.next()) {
				image = rs.getBytes(1); // BLOB用byes陣列接收
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return image;
	}

}
