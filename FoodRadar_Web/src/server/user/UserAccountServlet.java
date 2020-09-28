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

@WebServlet("/UserAccountServlet")
public class UserAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserAccountDao userAccountDao = null;
	private PubTools pubTools = new PubTools();



	@Override
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
		if (userAccountDao == null) {
			userAccountDao = new UserAccountDaoImpl();
		}
		
		String action = jsonObject.get("action").getAsString();
		Integer id = jsonObject.get("id").getAsInt();
		pubTools.showConsoleMsg("doPost.action" , action);
		pubTools.showConsoleMsg("doPost.id" , id.toString());
		
		if (action.equals("getAll")) {
			List<UserAccount> userAccountList = userAccountDao.getAll(); // 先不抓圖檔，讓app端先顯示文字之後再用資料的ID去資料庫取圖
			pubTools.writeText(response, gson.toJson(userAccountList));
			
		} else if (action.equals("getImage")) {
			int imageSize = jsonObject.get("imageSize").getAsInt();
			OutputStream os = response.getOutputStream();
			byte[] image = userAccountDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize); // 在server端縮圖
				response.setContentType("image/jpeg"); // 這定回傳種類為圖片
				response.setContentLength(image.length);
				os.write(image);
			}
		} else if (action.equals("userAccountInsert")) {
			// userAccount userAccount
			String userAccountJson = jsonObject.get("userAccount").getAsString();
			pubTools.showConsoleMsg("userAccountJson", userAccountJson);
			UserAccount userAccount = gson.fromJson(userAccountJson, UserAccount.class);			
			int count = 0;
//			count = userAccountDao.insert(userAccount);
			pubTools.writeText(response, String.valueOf(count));
			
		} else if (action.equals("userAccountDelete")) {
			Integer userId = jsonObject.get("userId").getAsInt();
			Integer resId = jsonObject.get("resId").getAsInt();int count = 0;
//			count = userAccountDao.delete(userId, resId);
			pubTools.writeText(response, String.valueOf(count));			
		}
		
		
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		if (userAccountDao == null) {
			userAccountDao = new UserAccountDaoImpl();
		}
		List<UserAccount> userAccountList = userAccountDao.getAll();
		pubTools.showConsoleMsg("doGet.userAccountList" , "");
		System.out.println("userAccountList: " + userAccountList);
		pubTools.writeText(response, new Gson().toJson(userAccountList));
		
		List<String> strList = new ArrayList<String>();
		strList.add("3");
		strList.add("6");
		pubTools.writeText(response, new Gson().toJson(strList));
		
		UserAccount userAccountFindById = userAccountDao.findById(3); // userAccountDao.findById(3);
		pubTools.writeText(response, new Gson().toJson(userAccountFindById));
	}
       


}