package server.img;

public class Img {
	
	private int imgId;
	private int articleId;
	private byte[] imgByte;
	
	
	public Img() {
		super();
	}

	public Img(int imgId, int articleId) {
		super();
		this.imgId = imgId;
		this.articleId = articleId;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImagId(int imgId) {
		this.imgId = imgId;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public Img(int imgId, int articleId, byte[] imgByte) {
		super();
		this.imgId = imgId;
		this.articleId = articleId;
		this.imgByte = imgByte;
	}

	public byte[] getImgByte() {
		return imgByte;
	}

	public void setImgByte(byte[] imgByte) {
		this.imgByte = imgByte;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	
}
