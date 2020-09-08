package server.user;

import java.util.List;

//Date Time: 2020-09-07 20:00:17
//interface Dao: MyResDao
public interface MyResDao {
	int insert(MyRes myres);

	int update(MyRes myres);

	int delete(int id);

	MyRes findById(int id);

	List<MyRes> getAll();
	
	List<MyRes> getAllById(int id);

	byte[] getImage(int id);

}
