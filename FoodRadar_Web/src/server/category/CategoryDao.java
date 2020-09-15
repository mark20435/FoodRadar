package server.category;

import java.util.List;

//Date Time: 2020-09-11 18:35:24
//interface Dao: CategoryDao
public interface CategoryDao {
	int insert(Category category, byte[] image);

	int update(Category category, byte[] image);

	int delete(int id);

	Category findById(int id);

	List<Category> getAll();

	byte[] getImage(int id);

}
