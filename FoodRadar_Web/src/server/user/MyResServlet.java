package server.user;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import server.main.ImageUtil;
import server.user.PubTools;

@WebServlet("/MyResServlet")
public class MyResServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private String CONTENT_TYPE = PubTools.CONTENT_TYPE;
	private MyResDao myResDao = null;
	private PubTools pubTools = new PubTools();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fromName = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
		pubTools.showConsoleMsg(fromName, "[START]");
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
		pubTools.showConsoleMsg("doPost.action" , action);
		pubTools.showConsoleMsg("doPost.id" , id.toString());
		
		if (action.equals("getAll")) {
			List<MyRes> myResList = myResDao.getAll(); // 先不抓圖檔，讓app端先顯示文字之後再用資料的ID去資料庫取圖
			pubTools.writeText(response, gson.toJson(myResList));
			
		} else if (action.equals("getAllById")) {
			List<MyRes> myResList = myResDao.getAllById(id);
			pubTools.writeText(response, gson.toJson(myResList));
			
		} else if (action.equals("getImage")) {
			int imageSize = jsonObject.get("imageSize").getAsInt();
			OutputStream os = response.getOutputStream();
			byte[] image = myResDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize); // 在server端縮圖
				response.setContentType("image/jpeg"); // 這定回傳種類為圖片
				response.setContentLength(image.length);
				os.write(image);
			}
		} else if (action.equals("myResInsert")) {
			// MyRes myres
			String myResJson = jsonObject.get("myres").getAsString();
			pubTools.showConsoleMsg("myResJson", myResJson);
			MyRes myres = gson.fromJson(myResJson, MyRes.class);			
			int count = 0;
			count = myResDao.insert(myres);
			pubTools.writeText(response, String.valueOf(count));
			
		} else if (action.equals("myResDelete")) {
			Integer userId = jsonObject.get("userId").getAsInt();
			Integer resId = jsonObject.get("resId").getAsInt();int count = 0;
			count = myResDao.delete(userId, resId);
			pubTools.writeText(response, String.valueOf(count));			
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (myResDao == null) {
			myResDao = new MyResDaoImpl();
		}
		List<MyRes> myResList = myResDao.getAll();
		pubTools.writeText(response, new Gson().toJson(myResList));
		
		List<String> strList = new ArrayList<String>();
		strList.add("3");
		strList.add("6");
		pubTools.writeText(response, new Gson().toJson(strList));
		
		List<MyRes> myResListById = myResDao.getAllById(3);
		pubTools.writeText(response, new Gson().toJson(myResListById));
	}
	
	
//	private void writeText(HttpServletResponse response, String outText) throws IOException {
//		response.setContentType(CONTENT_TYPE);
//		PrintWriter out = response.getWriter();
//		out.print(outText);
//		// 將輸出資料列印出來除錯用
//		// System.out.println("output: " + outText);
//	}

}
