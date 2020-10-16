package server.img;

import java.util.List;

public interface ImgDao {
	
	int insert(Img img, byte[] image);
	int update(Img img, byte[] image);
	int delete(int imgId);

	Img findById(int imgId);

	
	byte[] getImage(int imgId);
	
	byte[] getImageByArticleId(int articleId);

	List<Img> getAll();
	
	List<Img> getAllById(int articleId);
	
	int findByIdMax(Img img, byte[] image);

}
