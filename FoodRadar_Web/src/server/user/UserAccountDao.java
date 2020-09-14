package server.user;

import java.util.List;

//Date Time: 2020-09-10 15:22:30
//interface Dao: UserAccountDao
public interface UserAccountDao {
	int insert(UserAccount useraccount, byte[] image);

	int update(UserAccount useraccount, byte[] image);

	int delete(int id);

	UserAccount findById(int id);

	List<UserAccount> getAll();

	byte[] getImage(int id);

}
