package server.resAddress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.main.ServiceLocator;
import server.resAddress.ResAddress;

public class ResAddressDaoImpl implements ResAddressDao {
	DataSource dataSource;

	// 無參數建構子 > 實例化介面，無參數建構子內新增ServiceLocator的方法(getInstance())，用於連接取得資料庫資料
	public ResAddressDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<ResAddress> getRes() {
		String sql = "select resId, resName, resAddress, resCategoryInfo from Res R left join Category C on R.resCategoryId = C.resCategoryId where resEnable = 1 ;";
		List<ResAddress> resAddresses = new ArrayList<ResAddress>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int resId = rs.getInt("resId");
				String resName = rs.getString("resName");
				String resAddress = rs.getString("resAddress");
				String resCategoryInfo = rs.getString("resCategoryInfo");
//				ResAddress getResAddress = new ResAddress(resName, resAddress);
				ResAddress getResAddress = new ResAddress(resId, resName, resAddress, resCategoryInfo);
				resAddresses.add(getResAddress);
			}
			return resAddresses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resAddresses;
	}

}
