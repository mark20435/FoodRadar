package server.ress;

import java.awt.Image;
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

import server.category.Category;
import server.main.ImageUtil;


@WebServlet("/ResServlet")
public class ResServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	ResDao resDao = null;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if (resDao == null) {
			resDao = new ResDaoMySqlImpl();
		}
		String action = jsonObject.get("action").getAsString();
		int id;
		Res res;
		byte[] image;

		if (action.equals("getAll")) {
			List<Res> ress = resDao.getAll();
			writeText(response, gson.toJson(ress));
		} 
		else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			id = jsonObject.get("id").getAsInt();
			
			System.out.println("id: " + id);
			
			int imageSize = jsonObject.get("imageSize").getAsInt();
			image = resDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType("image/jpeg");
				response.setContentLength(image.length);
				os.write(image);}
			
			
		} else if (action.equals("getCategories")) {
			List<Category> Categories = resDao.getCategories();
			writeText(response, gson.toJson(Categories));
		
		} else if (action.equals("resInsert") || action.equals("resUpdate")) {
			String resJson = jsonObject.get("res").getAsString();
			System.out.println("resJson = " + resJson);
			res = gson.fromJson(resJson, Res.class);
			image = null;
		
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;
			if (action.equals("resInsert")) {
				count = resDao.insert(res, image);
			} else if (action.equals("resUpdate")) {
				count = resDao.update(res, image);
			}
			writeText(response, String.valueOf(count));
		} else if (action.equals("resDelete")) {
			int resId = jsonObject.get("resId").getAsInt();
			int count = resDao.delete(resId);
			writeText(response, String.valueOf(count));
		} else if (action.equals("categoryfindById")) {
			id = jsonObject.get("id").getAsInt();
			List<Res> ress = resDao.CategoryfindById(id);	
			writeText(response, gson.toJson(ress));
		} else if (action.equals("findById")) {
			id = jsonObject.get("id").getAsInt();
			res = resDao.findById(id);
			writeText(response, gson.toJson(res));
		}else {
			writeText(response, "");
		}
	}
       
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		System.out.println(outText);
		
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (resDao == null) {
			resDao = new ResDaoMySqlImpl();
		}
		List<Res> ress = resDao.getAll();
		writeText(response, new Gson().toJson(ress));
	}

	
	

}
