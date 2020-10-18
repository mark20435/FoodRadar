package server.ress;

import java.util.List;

import server.category.Category;
import server.img.Img;

public interface ResDao {
	int insert(Res res, byte[] image);
	int update(Res res, byte[] image);
	int delete(int resId);

	Res findById(int resId);

	List<Res> getAll();
	
	List<Res> getAllEnable(int curUserId);
	
	List<Res> CategoryfindById(int resId);
	
	byte[] getImage(int resId);
	
	List<Category> getCategories();
	
	List<Img> getImgByResId(int resId);
	
	int insertResRating(ResRating resRating);
	
	int updateResRating(ResRating resRating);
	
	ResRating findRatingByResIdAndUserId(int resId, int userId);
}
