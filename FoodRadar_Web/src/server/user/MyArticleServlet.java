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
import com.google.gson.GsonBuilder;
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
		// vvvvvv 直接把物件經GSON傳到後端Servlet的寫法，其中日期時間，有特別進行格式處理以免解析時格式無法確認
       Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
       // ^^^^^^ 直接把物件經GSON傳到後端Servlet的寫法，其中日期時間，有特別進行格式處理以免解析時格式無法確認
       // vvvvvv 日期時間，未特別進行格式處理的寫法
       // Gson gson = new Gson();
       // ^^^^^^ 日期時間，未特別進行格式處理的寫法
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
		
		if (action.equals("getMyArticleCollect") // 我的文章收藏 
				|| action.equals("getMyArticleIsMe")) { // 我的發文
			id = jsonObject.get("id").getAsInt();
			pubTools.showConsoleMsg(action + ".id" , id.toString());
			String articleDate = "";
			List<MyArticle> myArticleList = myArticleDao.myArticle(id, articleDate, action);
			pubTools.writeText(response, gson.toJson(myArticleList));
			
		} else if (action.equals("getMyArticleMyComment")) { // 我的回文
			id = jsonObject.get("id").getAsInt();
			pubTools.showConsoleMsg(action + ".id" , id.toString());
			List<MyArticle> myArticleList = myArticleDao.myArticleMyComment(id);
			pubTools.writeText(response, gson.toJson(myArticleList));
			
		} else if (action.equals("getArticleByUserPhone")) { // 會員發文管理
			String userPhone = jsonObject.get("userPhone").getAsString();
			String articleDate = jsonObject.get("articleDate").getAsString();
//			UserAccountDao userAccountDao = null;
			UserAccount userAccount = new UserAccountDaoImpl().findByPhone(userPhone);
			id = userAccount.getUserId();
			pubTools.showConsoleMsg(action + ".id" , id.toString());
			List<MyArticle> myArticleList = myArticleDao.myArticle(id, articleDate, action);
			pubTools.writeText(response, gson.toJson(myArticleList));

		} else if (action.equals("setEnableStatus")) {  // 發文Enable狀態設定
			id = jsonObject.get("id").getAsInt();
			Boolean enableStatus = jsonObject.get("enableStatus").getAsBoolean();
			pubTools.showConsoleMsg("id: ", id.toString());
			pubTools.showConsoleMsg("enableStatus: ", String.valueOf(enableStatus));
			int count = 0;
			count = myArticleDao.setEnableStatus(id, enableStatus);
			pubTools.writeText(response, String.valueOf(count));	
			
//		} else if (action.equals("getCommentByUserPhone")) { // 會員回文管理
//			id = jsonObject.get("id").getAsInt();
//			pubTools.showConsoleMsg(action + ".id" , id.toString());
//			List<MyArticle> myArticleList = myArticleDao.myArticleMyComment(id);
//			pubTools.writeText(response, gson.toJson(myArticleList));
			
		} else if (action.equals("getImage")) { // 先不抓圖檔，讓app端先顯示文字之後再用資料的ID去資料庫取圖
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
			String myArticleJson = jsonObject.get("myArtcile").getAsString();
			pubTools.showConsoleMsg("myResInsert", myArticleJson);
			MyArticle myArticle = gson.fromJson(myArticleJson, MyArticle.class);			
			int count = 0;
			count = myArticleDao.insert(myArticle);
			pubTools.writeText(response, String.valueOf(count));
			
		} else if (action.equals("myResDelete")) {
			Integer myArticleId = jsonObject.get("myArticleId").getAsInt();
			pubTools.showConsoleMsg("myResDelete", myArticleId.toString());
			int count = 0;
			count = myArticleDao.delete(myArticleId);
			pubTools.writeText(response, String.valueOf(count));

		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (myArticleDao == null) {
			myArticleDao = new MyArticleDaoImpl();
		}
		List<MyArticle> myArticleList = myArticleDao.myArticle(3,"","getMyArticleCollect");
		pubTools.writeText(response, new Gson().toJson(myArticleList));
		
		pubTools.writeText(response, "<br><br>");
		
		List<String> strList = new ArrayList<String>();
		strList.add("3");
		strList.add("6");
		strList.add("9");
		pubTools.writeText(response, new Gson().toJson(strList));
		
		MyArticle myArticleByPhone = myArticleDao.findById(3);
		pubTools.writeText(response, new Gson().toJson(myArticleByPhone));

	}

}
