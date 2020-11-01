package server.img;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import server.article.Article;
import server.main.ServiceLocator;

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

	// 取得上傳文章的ID並上傳圖片 > 更新文章
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
				ps.setInt(3, img.getImgId());
			} else {
				ps.setInt(2, img.getImgId());
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
		String sql = "SELECT articleId, img FROM Img WHERE imgId = ?;";
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

	// 取得特定文章圖片
	@Override
	public byte[] getImageByArticleId(int articleId) {
		String sql = "SELECT img FROM Img WHERE articleId = ? ;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
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
	public byte[] getImage(int imgId) {
		String sql = "SELECT img FROM Img WHERE imgId = ? ;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, imgId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return image;
	}

	// 取所有圖片資訊
	@Override
	public List<Img> getAll() {
		String sql = "SELECT articleId, imgId FROM Img ORDER BY imgId DESC;";
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

	// 取特定文章圖片資訊
	@Override
	public List<Img> getAllById(int articleId) {
		String sql = "SELECT articleId, imgId, img FROM Img where articleId = ? ORDER BY imgId DESC;";
		List<Img> imgList = new ArrayList<Img>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int imgId = rs.getInt("imgId");
				Img img = new Img(imgId, articleId);
				imgList.add(img);
			}
			return imgList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return imgList;
	}

	// 取得上傳文章的ID並上傳圖片 > 發文
	@Override
	public int findByIdMax(Img img, byte[] image) {
		int count = 0;
		// sql語法 > 取文章Id最大(最新的文章)的圖片
		String sql = "select Max(articleId) as 'articleId' From Article; ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			System.out.println("SQL:" + sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int articleId = rs.getInt("articleId");
				String sqlImg = "INSERT INTO Img " + "(articleId, img) " + " VALUES(?,?) ;";
				try (Connection connectionImg = dataSource.getConnection();
						PreparedStatement psImg = connection.prepareStatement(sqlImg);) {
					psImg.setInt(1, articleId);
					psImg.setBytes(2, image);
					count = psImg.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public byte[] getImageBase(int imgId) {
		byte[] image = null;
		String sql = "SELECT img FROM Img WHERE imgId = ? ;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, imgId);
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
