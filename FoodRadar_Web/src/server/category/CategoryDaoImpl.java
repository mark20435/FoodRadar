package server.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.main.ServiceLocator;

// Date Time: 2020-09-11 18:35:24
// implements Dao: CategoryDaoImpl
public class CategoryDaoImpl implements CategoryDao{
	DataSource dataSource;

	public CategoryDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public int insert(Category category, byte[] image) {
		int count = 0;
		String sql = "INSERT INTO Category" 
		+ "(info, cateSn, image)"
		+ "VALUES(?, ?, ?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setString(1, category.getInfo());
			ps.setInt(2,category.getCateSn());
			ps.setBytes(3, image);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Category category, byte[] image) {
		int count = 0;
		String sql = "";
		if(image != null) {
			sql = "UPDATE Category SET resCategoryInfo = ?, resCategorySn = ?, resCategoryImg = ? WHERE resCategoryId = ?;";
		}else {
			sql = "UPDATE Category SET resCategoryInfo = ?, resCategorySn WHERE resCategoryId = ?;";
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setString(1, category.getInfo());
			ps.setInt(2, category.getCateSn());
			if(image != null) {
				ps.setBytes(3, image);
				ps.setInt(4, category.getId());
			}else {
				ps.setInt(3, category.getId());
			}
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
	public byte[] getImage(int id) {
		String sql = "SELECT resCategoryImg FROM Category WHERE resCategoryId = ?;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public Category findById(int id) {
		String sql = "SELECT info resCategorySn FROM Category WHERE resCategoryId = ?;";
		Category category = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String info = rs.getString(1);
				Integer cateSn = rs.getInt(2);
				category = new Category(info, cateSn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}

	@Override
	public List<Category> getAll() {
		// Date Time: 2020-09-11 18:35:24
		// select statements : Category
		String sqlStmt = "SELECT resCategoryId, resCategoryInfo, resCategorySn" 
		        + " FROM Category;";
		

		List<Category> categoryList  = new ArrayList<Category>();
		try (Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlStmt);) {			
			ResultSet rs = ps.executeQuery(sqlStmt);
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
}
