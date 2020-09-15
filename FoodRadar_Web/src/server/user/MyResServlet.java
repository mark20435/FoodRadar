package server.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import server.category.ImageUtil;

@WebServlet("/MyResServlet")
public class MyResServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	MyResDao myResDao = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (myResDao == null) {
			myResDao = new MyResDaoImpl();
		}
		
		String action = jsonObject.get("action").getAsString();
		Integer id = jsonObject.get("id").getAsInt();
		System.out.println("doPost.action: " + action);
		System.out.println("doPost.id: " + id);
		if (action.equals("getAll")) {
			List<MyRes> myResList = myResDao.getAll(); // 先不抓圖檔，讓app端先顯示文字之後再用資料的ID去資料庫取圖
//			System.out.println("doPost.books: " + books);
			writeText(response, gson.toJson(myResList));
			
		} else if (action.equals("getAllById")) {
			System.out.println("getAllById.id: " + id);
			List<MyRes> myResList = myResDao.getAllById(id);
//			System.out.println("doPost.books: " + books);
			writeText(response, gson.toJson(myResList));
			
		} else if (action.equals("getImage")) {
			System.out.println("getImage.id: " + id);
			int imageSize = jsonObject.get("imageSize").getAsInt();
			OutputStream os = response.getOutputStream();
			byte[] image = myResDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize); // 在server端縮圖
				response.setContentType("image/jpeg"); // 這定回傳種類為圖片
				response.setContentLength(image.length);
				os.write(image);
			}
		}
		
		

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (myResDao == null) {
			myResDao = new MyResDaoImpl();
		}
		List<MyRes> myResList = myResDao.getAll();
//		System.out.println("doGet.books: " + myResList);
		writeText(response, new Gson().toJson(myResList));
		
		List<String> strList = new ArrayList<String>();
		strList.add("3");
		strList.add("6");
		writeText(response, new Gson().toJson(strList));
		
		List<MyRes> myResListById = myResDao.getAllById(3);
		writeText(response, new Gson().toJson(myResListById));
	}
	
	
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
		// System.out.println("output: " + outText);
	}


}
