package ress;

import java.util.List;

public interface ResDao {
	int insert(Res res, byte[] image);
	int update(Res res, byte[] image);
	int delete(int resId);

	Res findById(int resId);

	List<Res> getAll();

	byte[] getImage(int resId);

}
