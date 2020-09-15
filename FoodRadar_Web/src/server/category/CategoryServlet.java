package server.category;

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

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoryDao categoryDao = null;
	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (categoryDao == null) {
			categoryDao = new CategoryDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();
		System.out.println("categorys.action: " + action);
		if (action.equals("getAll")) {
			List<Category> categorys = categoryDao.getAll();
			System.out.println("categorys: " + categorys);
			writeText(resp, gson.toJson(categorys));
		} else if (action.equals("getImage")) {
			OutputStream os = resp.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = categoryDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				resp.setContentType("image/jpeg");
				resp.setContentLength(image.length);
				os.write(image);
			}
		} else if (action.equals("categoryInsert") || action.equals("categoryUpdate")) {
			String categoryJson = jsonObject.get("category").getAsString();
			System.out.println("categoryJson = " + categoryJson);
			Category category = gson.fromJson(categoryJson, Category.class);
			byte[] image = null;
			// 檢查是否有上傳圖片
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;
			if (action.equals("categoryInsert")) {
				count = categoryDao.insert(category, image);
			} else if (action.equals("categoryUpdate")) {
				count = categoryDao.update(category, image);
			}
			writeText(resp, String.valueOf(count));
		} else if (action.equals("categoryDelete")) {
			int categoryId = jsonObject.get("categoryId").getAsInt();
			int count = categoryDao.delete(categoryId);
			writeText(resp, String.valueOf(count));
		} else if (action.equals("findById")) {
			int id = jsonObject.get("id").getAsInt();
			Category category = categoryDao.findById(id);
			writeText(resp, gson.toJson(category));
		} else {
			writeText(resp, "");
		}
	
	}
	
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		String CONTENT_TYPE = "text/html; charset=utf-8";
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("doGet: " + response);
		if (categoryDao == null) {
			categoryDao = new CategoryDaoImpl();
		}
		List<Category> resCategories = categoryDao.getAll();
		writeText(response, new Gson().toJson(resCategories));
		
		
	}
       
    
	
}
