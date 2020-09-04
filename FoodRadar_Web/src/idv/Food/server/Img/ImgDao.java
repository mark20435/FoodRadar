package idv.Food.server.Img;

import java.util.List;

public interface ImgDao {
	
	int insert(Img img, byte[] image);
	int update(Img img, byte[] image);
	int delete(int imgId);

	Img findById(int imgId);
	
	byte[] getImage(int imgId);

	List<Img> getAll();

}
