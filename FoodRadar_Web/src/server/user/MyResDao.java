package server.user;

import java.util.List;

import server.ress.Res;

//Date Time: 2020-09-07 20:00:17
//interface Dao: MyResDao
public interface MyResDao {
	int insert(MyRes myres);

	int update(MyRes myres);

	int delete(int userId, int resId);

	MyRes findById(int id);

	List<MyRes> getAll();
	
	List<MyRes> getAllById(int id);

	byte[] getImage(int id);

	List<Res> getResById(Integer id, Integer userId);

}
