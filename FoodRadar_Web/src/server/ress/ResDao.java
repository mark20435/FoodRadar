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
	
	List<Res> getAllEnable();

	byte[] getImage(int resId);
	
	List<Category> getCategories();
	
	List<Img> getImgByResId(int resId);
}
