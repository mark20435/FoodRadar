package server.img;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import server.article.Article;
import server.main.ImageUtil;

@WebServlet("/ImgServlet")
public class ImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8"; // 編碼
	ImgDao imgDao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 解碼
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonInput = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonInput.append(line);
		}
		// 印出資料 > Debug用
		// System.out.println("input: " + jsonInput);

		// 宣告jsonObject
		JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);
		// Dao不為空值的話，取得實作方法內容
		if (imgDao == null) {
			imgDao = new ImgDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<Img> imgs = imgDao.getAll();
			writeText(response, gson.toJson(imgs));
		} 
		//特定文章圖片資訊(不含圖)
		else if (action.equals("getAllById")) {
			int id = jsonObject.get("articleId").getAsInt();
			List<Img> imgs = imgDao.getAllById(id);
			writeText(response, gson.toJson(imgs));
		} 
		//特定文章的圖片 > 透過文章ID
		else if (action.equals("getImageByArticleId")) { // 使用getImage方法
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt(); // image物件 > 縮小的圖片尺寸數字，client會呼叫此物件的KEY(imageSize)
			byte[] image = imgDao.getImageByArticleId(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize); // 將取得的圖片縮小
				response.setContentType("image/*"); // 取得資料庫圖片
				response.setContentLength(image.length); // 讀取圖片長度
				os.write(image);
			}
		} 
		//getImage方法
		else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt(); 
			byte[] image = imgDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType("image/*");
				response.setContentLength(image.length);
				os.write(image);
			}
		} 
		else if (action.equals("imgInsert") || action.equals("imgUpdate") || action.equals("findByIdMax")) { // 宣告的"bookInsert" key > client端會呼叫到
			String imgJson = jsonObject.get("img").getAsString();
			System.out.println("imgJson:" + imgJson);
			Img img = gson.fromJson(imgJson, Img.class);
			byte[] image = null;

			// 檢查是否有取得圖片
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					// getMimeDecoder() > 取得解碼器
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;
			if (action.equals("imgInsert")) {
				count = imgDao.insert(img, image);
			} 
			else if (action.equals("imgUpdate")) {
				count = imgDao.update(img, image);
			} else if (action.equals("findByIdMax")) {
				count = imgDao.findByIdMax(img, image);
			}
			writeText(response, String.valueOf(count));
		} else if (action.equals("imgDelete")) {
			int imgId = jsonObject.get("imgId").getAsInt();
			int count = imgDao.delete(imgId);
			writeText(response, String.valueOf(count));
		} else if (action.equals("findById")) {
			int id = jsonObject.get("imgId").getAsInt();
			Img img = imgDao.findById(id);
			writeText(response, gson.toJson(img));
		} else {
			writeText(response, "");
		}
	}

	// 將response轉為json字串的方法
	private void writeText(HttpServletResponse response, String TextOut) throws IOException {
		response.setContentType(CONTENT_TYPE); // 編碼包裝
		PrintWriter printOut = response.getWriter();
		printOut.write(TextOut);
		// Debug用
		System.out.println("Textout:" + TextOut);
	}

	//doGet > 網頁用
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (imgDao == null) {
			imgDao = new ImgDaoImpl();
		}
		List<Img> img = imgDao.getAll();
		writeText(response, new Gson().toJson(img));
	}

}
