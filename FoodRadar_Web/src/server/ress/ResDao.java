package server.ress;

import java.util.List;

import server.category.Category;

public interface ResDao {
	int insert(Res res, byte[] image);
	int update(Res res, byte[] image);
	int delete(int resId);

	List<Res> CategoryfindById(int resId);
	
	Res findById(int resId);

	List<Res> getAll();

	byte[] getImage(int resId);
	
<<<<<<< HEAD
	
=======
	List<Category> getCategories();
>>>>>>> dde8d24d62a73563e145361c3cafa77db4c0b1f1

}
