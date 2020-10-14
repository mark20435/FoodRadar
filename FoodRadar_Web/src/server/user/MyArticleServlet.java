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

@WebServlet("/MyArticleServlet")
public class MyArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MyArticleDao myArticleDao = null;
	private PubTools pubTools = new PubTools();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fromName = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
		pubTools.showConsoleMsg(fromName, "[START]=>MyResServlet");
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (myArticleDao == null) {
			myArticleDao = new MyArticleDaoImpl();
		}
				
		String action = jsonObject.get("action").getAsString();
		Integer id = 0;
		pubTools.showConsoleMsg("doPost.action" , action);

		// 先不抓圖檔，讓app端先顯示文字之後再用資料的ID去資料庫取圖
		if (action.equals("getAllById")) {
			id = jsonObject.get("id").getAsInt();
			pubTools.showConsoleMsg("getAllById.id" , id.toString());
			List<MyArticle> myArticleList = myArticleDao.getAllById(id);
			pubTools.writeText(response, gson.toJson(myArticleList));
			
		} else if (action.equals("getImage")) {
			int imageSize = jsonObject.get("imageSize").getAsInt();
			id = jsonObject.get("id").getAsInt();
			pubTools.showConsoleMsg("getImage.id" , id.toString());
			OutputStream os = response.getOutputStream();
			byte[] image = myArticleDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize); // 在server端縮圖
				response.setContentType("image/jpeg"); // 這定回傳種類為圖片
				response.setContentLength(image.length);
				os.write(image);
			}
			
		} else if (action.equals("myResInsert")) {
			String myArticleJson = jsonObject.get("myres").getAsString();
			pubTools.showConsoleMsg("myResJson", myArticleJson);
			MyArticle myArticle = gson.fromJson(myArticleJson, MyArticle.class);			
			int count = 0;
			count = myArticleDao.insert(myArticle);
			pubTools.writeText(response, String.valueOf(count));
			
		} else if (action.equals("myResDelete")) {
			Integer myArticleId = jsonObject.get("myArticleId").getAsInt();
			int count = 0;
			count = myArticleDao.delete(myArticleId);
			pubTools.writeText(response, String.valueOf(count));

		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (myArticleDao == null) {
			myArticleDao = new MyArticleDaoImpl();
		}
		List<MyArticle> myArticleList = myArticleDao.getAllById(3);
		pubTools.writeText(response, new Gson().toJson(myArticleList));
		
		pubTools.writeText(response, "<br><br>");
		
		List<String> strList = new ArrayList<String>();
		strList.add("3");
		strList.add("6");
		strList.add("9");
		pubTools.writeText(response, new Gson().toJson(strList));		

	}

}
