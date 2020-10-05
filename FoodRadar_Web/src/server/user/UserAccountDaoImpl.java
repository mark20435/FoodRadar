package server.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.main.ServiceLocator;

// Date Time: 2020-09-10 15:22:30
// implements Dao: UserAccountDaoImpl
public class UserAccountDaoImpl implements UserAccountDao {
	DataSource dataSource;
	private PubTools pubTools = new PubTools();

	public UserAccountDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public int insert(UserAccount useraccount, byte[] image) {
		// Date Time: 2020-09-11 13:20:54
		// insert statements : UserAccount
		int count = 0;
		String sqlStmt = "INSERT INTO UserAccount(userPhone, userPwd, userBirth, userName, allowNotifi, isEnable, isAdmin, userAvatar) ";
//		sqlStmt += " VALUES( ?, ?, ?, ?, ?, ?, ?, ?)";
		sqlStmt += " SELECT ?, ?, ?, ?, ?, ?, ?, ?";
		sqlStmt += " WHERE NOT EXISTS (SELECT userPhone FROM UserAccount WHERE userPhone = ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
//			ps.setInt(1, useraccount.getUserId());
			ps.setString(1, useraccount.getUserPhone());
			ps.setString(2, useraccount.getUserPwd());
			ps.setTimestamp(3, useraccount.getUserBirth());
			ps.setString(4, useraccount.getUserName());
			ps.setBoolean(5, useraccount.getAllowNotifi());
			ps.setBoolean(6, true); // useraccount.getIsEnable());
			ps.setBoolean(7, false); // useraccount.getIsAdmin());
			ps.setBytes(8, image); // useraccount.getUserAvatar());
			ps.setString(9, useraccount.getUserPhone());
//			ps.setTimestamp(10, useraccount.getCreateDate());
//			ps.setTimestamp(11, useraccount.getModifyDate());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(UserAccount useraccount, byte[] image) {
		// Date Time: 2020-09-11 16:52:47
		// update statements : UserAccount
		int count = 0;
		String sqlStmt = "UPDATE UserAccount ";
//		sqlStmt += " SET userId = ?, userPhone = ?, userPwd = ?, userBirth = ?, userName = ?, allowNotifi = ?, isEnable = ?, isAdmin = ?, userAvatar = ?, createDate = ?, modifyDate = ?";
		sqlStmt += " SET userPwd = ?, userBirth = ?, userName = ?, allowNotifi = ?, userAvatar = ?, modifyDate = now()";
		sqlStmt += " WHERE userId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
//			ps.setInt(1, useraccount.getUserId());
//			ps.setString(2, useraccount.getUserPhone());
			ps.setString(1, useraccount.getUserPwd());
			ps.setTimestamp(2, useraccount.getUserBirth());
			ps.setString(3, useraccount.getUserName());
			ps.setBoolean(4, useraccount.getAllowNotifi());
//			ps.setBoolean(7, useraccount.getIsEnable());
//			ps.setBoolean(8, useraccount.getIsAdmin());
			ps.setBytes(5, image);
			ps.setInt(6, useraccount.getUserId());
//			ps.setTimestamp(10, useraccount.getCreateDate());
//			ps.setTimestamp(11, useraccount.getModifyDate());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserAccount findById(int id) {
		// Date Time: 2020-09-10 15:22:30
		// select statements : UserAccount
		String sqlStmt = "SELECT userId, userPhone, userPwd, userBirth, userName, ";
		sqlStmt += " allowNotifi, isEnable, isAdmin, userAvatar, createDate, modifyDate ";
		sqlStmt += " FROM UserAccount WHERE userId = ?;";
//		sqlStmt = "SELECT `userId`, `userPhone`, `userPwd`, `userBirth`, `userName`, `allowNotifi`, `isEnable`, `isAdmin`, `userAvatar`, `createDate`, `modifyDate`";
//		sqlStmt += " FROM `FoodRadar`.`UserAccount` WHERE `userId` = ?;";
		UserAccount userAccount = null;
		pubTools.showConsoleMsg("findById.sqlStmt", sqlStmt);
		pubTools.showConsoleMsg("findById.userId", String.valueOf(id));
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, id);
//			ResultSet rs = ps.executeQuery(sqlStmt);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int userId = rs.getInt("userId");
				String userPhone = rs.getString("userPhone");
				String userPwd = rs.getString("userPwd");
				Timestamp userBirth = rs.getTimestamp("userBirth");
				String userName = rs.getString("userName");
				Boolean allowNotifi = rs.getBoolean("allowNotifi");
				Boolean isEnable = rs.getBoolean("isEnable");
				Boolean isAdmin = rs.getBoolean("isAdmin");
				byte[] userAvatar = rs.getBytes("userAvatar");
				Timestamp createDate = rs.getTimestamp("createDate");
				Timestamp modifyDate = rs.getTimestamp("modifyDate");

				userAccount = new UserAccount(userId, userPhone, userPwd, userBirth, userName, allowNotifi, isEnable,
						isAdmin, userAvatar, createDate, modifyDate);
			}
			return userAccount;
		} catch (SQLException e) {
			pubTools.showConsoleMsg("getImage.SQLException", String.valueOf(e.getErrorCode()));
			e.printStackTrace();
		} catch (Exception e) {
			pubTools.showConsoleMsg("getImage.Exception", String.valueOf(e.getMessage()));
			e.printStackTrace();
		}
		return userAccount;
	}

	@Override
	public List<UserAccount> getAll() {
		// Date Time: 2020-09-10 15:22:30
		// select statements : UserAccount
		String sqlStmt = "SELECT userId, userPhone, userPwd, userBirth, userName, allowNotifi, isEnable, isAdmin, userAvatar, createDate, modifyDate "
				+ " FROM UserAccount;";
		UserAccount userAccount = null;
		pubTools.showConsoleMsg("getAll.sqlStmt", sqlStmt);
		List<UserAccount> userAccountList = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ResultSet rs = ps.executeQuery(sqlStmt);
			// 假如有下一個欄位的話，取得其資料
			while (rs.next()) {
				int userId = rs.getInt("userId");
				String userPhone = rs.getString("userPhone");
				String userPwd = rs.getString("userPwd");
				Timestamp userBirth = rs.getTimestamp("userBirth");
				String userName = rs.getString("userName");
				Boolean allowNotifi = rs.getBoolean("allowNotifi");
				Boolean isEnable = rs.getBoolean("isEnable");
				Boolean isAdmin = rs.getBoolean("isAdmin");
				byte[] userAvatar = rs.getBytes("userAvatar");
				Timestamp createDate = rs.getTimestamp("createDate");
				Timestamp modifyDate = rs.getTimestamp("modifyDate");

				userAccount = new UserAccount(userId, userPhone, userPwd, userBirth, userName, allowNotifi, isEnable,
						isAdmin, userAvatar, createDate, modifyDate);
				userAccountList.add(userAccount);
			}
			return userAccountList;
		} catch (SQLException e) {
			pubTools.showConsoleMsg("getImage.SQLException", String.valueOf(e.getErrorCode()));
			e.printStackTrace();
		} catch (Exception e) {
			pubTools.showConsoleMsg("getImage.Exception", String.valueOf(e.getMessage()));
			e.printStackTrace();
		}
		return userAccountList;
	}

	@Override
	public byte[] getImage(int id) {
		byte[] userAvatar = null;
		String sqlStmt = "SELECT userAvatar ";
		sqlStmt += " FROM UserAccount WHERE userId = ?;";
		pubTools.showConsoleMsg("getImage.sqlStmt", sqlStmt);
		pubTools.showConsoleMsg("getImage.userId", String.valueOf(id));
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				userAvatar = rs.getBytes("userAvatar");
			}
			return userAvatar;
		} catch (SQLException e) {
			pubTools.showConsoleMsg("getImage.SQLException", String.valueOf(e.getErrorCode()));
			e.printStackTrace();
		} catch (Exception e) {
			pubTools.showConsoleMsg("getImage.Exception", String.valueOf(e.getMessage()));
			e.printStackTrace();
		}
		return userAvatar;
	}

	@Override
	public List<UserAccount> userLogin(String userPhone, String userPwd) {
		// Date Time: 2020-09-10 15:22:30
		// select statements : UserAccount
		String sqlStmt = "SELECT userId, userPhone, userPwd, userBirth, userName, ";
		sqlStmt += " allowNotifi, isEnable, isAdmin, userAvatar, createDate, modifyDate ";
		sqlStmt += " FROM UserAccount WHERE userPhone = ? AND userPwd = ? LIMIT 1;";
		UserAccount userAccount = null;
		List<UserAccount> userAccountList = new ArrayList<>();
		pubTools.showConsoleMsg("userLogin.sqlStmt", sqlStmt);
		pubTools.showConsoleMsg("userLogin.userPhone", String.valueOf(userPhone));
		pubTools.showConsoleMsg("userLogin.userPwd", String.valueOf(userPwd));
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStmt);) {
			ps.setString(1, userPhone);
			ps.setString(2, userPwd);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int userId = rs.getInt("userId");
				String userPhoneFromDB = rs.getString("userPhone");
				String userPwdFromDB = rs.getString("userPwd");
				Timestamp userBirth = rs.getTimestamp("userBirth");
				String userName = rs.getString("userName");
				Boolean allowNotifi = rs.getBoolean("allowNotifi");
				Boolean isEnable = rs.getBoolean("isEnable");
				Boolean isAdmin = rs.getBoolean("isAdmin");
				byte[] userAvatar = rs.getBytes("userAvatar");
//				byte[] userAvatar = null;
				Timestamp createDate = rs.getTimestamp("createDate");
				Timestamp modifyDate = rs.getTimestamp("modifyDate");

				userAccount = new UserAccount(userId, userPhoneFromDB, userPwdFromDB, userBirth, userName, allowNotifi, isEnable,
						isAdmin, userAvatar, createDate, modifyDate);
				userAccountList.add(userAccount);
			}
			pubTools.showConsoleMsg("userLogin.userAccountList.userId", "String.valueOf(userId)");
			return userAccountList;
		} catch (SQLException e) {
			pubTools.showConsoleMsg("getImage.SQLException", String.valueOf(e.getErrorCode()));
			e.printStackTrace();
		} catch (Exception e) {
			pubTools.showConsoleMsg("getImage.Exception", String.valueOf(e.getMessage()));
			e.printStackTrace();
		}
		return userAccountList;
	}

}
