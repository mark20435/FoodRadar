package idv.Food.server.Img;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import idv.Food.server.main.ServiceLocator;

public class ImgDaoImpl implements ImgDao {
	DataSource dataSource;

	public ImgDaoImpl() {
		// 於無參數建構子內新增ServiceLocator的方法(getInstance())，用於連接取得資料庫資料
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public ImgDaoImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public int insert(Img img, byte[] image) {
		int count = 0;
		String sql = "INSERT INTO Img" + "(articleId, img)" + "VALUES(?,?);";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, img.getArticleId());
			ps.setBytes(2, image);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Img img, byte[] image) {
		int count = 0;
		String sql = "";
		if (image != null) {
			sql = "UPDATE Img SET articleId = ?, img = ? WHERE imgId = ?;";
		} else {
			sql = "UPDATE Img SET articleId = ? WHERE imgId = ?;";
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, img.getArticleId());
			if (image != null) {
				ps.setBytes(2, image);
				ps.setInt(3, img.getImagId());
			} else {
				ps.setInt(2, img.getImagId());
			}
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int imgId) {
		int count = 0;
		String sql = "DELETE FROM Img WHERE imgId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, imgId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Img findById(int imgId) {
		String sql = "SELECT articleId FROM Img WHERE imgId = ?;";
		Img img = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, imgId);
			ResultSet rs = ps.executeQuery();
			int articleId = rs.getInt("articleId");
			img = new Img(imgId, articleId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return img;
	}

	@Override
	public byte[] getImage(int imgId) {
		String sql = "SELECT img FROM Img WHERE imgId = ?;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, imgId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes("imgId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public List<Img> getAll() {
		String sql = "SELECT articleId " + "FROM Img ORDER BY imgId DESC;";
		List<Img> imgList = new ArrayList<Img>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int imgId = rs.getInt("imgId");
				int articleId = rs.getInt("articleId");
				Img img = new Img(imgId, articleId);
				imgList.add(img);
			}
			return imgList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return imgList;
	}

}
