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

import server.coupon.Coupon;
import server.main.ImageUtil;
import server.ress.Res;


@WebServlet("/MyCouponServlet")
public class MyCouponServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private MyCouponDao myCouponDao = null;
    private PubTools pubTools = new PubTools();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fromName = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
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
		if (myCouponDao == null) {
			myCouponDao = new MyCouponDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();
		Integer id = 0;
		pubTools.showConsoleMsg("MyCoupon.action: ", action);
		if (action.equals("getAll")) {
			List<MyCoupon> myCouponList = myCouponDao.getAll();	
			pubTools.writeText(response, gson.toJson(myCouponList));
			
		}else if (action.equals("getAllById")) {
			id = jsonObject.get("id").getAsInt();
			new PubTools().showConsoleMsg("getAllById.id", id.toString());
			List<MyCoupon> myCouponList = myCouponDao.getAllById(id);
			pubTools.writeText(response, gson.toJson(myCouponList));
			
		}else if (action.equals("getImage")) {
			int imageSize = jsonObject.get("imageSize").getAsInt();
			id = jsonObject.get("id").getAsInt();
			OutputStream os = response.getOutputStream();
			byte[] image = myCouponDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize); // 在server端縮圖
				response.setContentType("image/jpeg"); // 這定回傳種類為圖片
				response.setContentLength(image.length);
				os.write(image);
		}
		
	}else if (action.equals("myCouponInsert")) {
		String myCouponJson = jsonObject.get("mycoupon").getAsString();
		MyCoupon mycoupon = gson.fromJson(myCouponJson, MyCoupon.class);			
		int count = 0;
		count = myCouponDao.insert(mycoupon);
		pubTools.writeText(response, String.valueOf(count));
		
	}else if (action.equals("isUsedUpdate")) { //店家使用優惠券按鈕
		Integer couPonId = jsonObject.get("couPonId").getAsInt();
		Integer userId = jsonObject.get("userId").getAsInt();
		Boolean couPonIsUsed = jsonObject.get("couPonIsUsed").getAsBoolean();
		
		pubTools.showConsoleMsg("id: ", id.toString());
		pubTools.showConsoleMsg("couPonIsUsed: ", String.valueOf(couPonIsUsed));
		int count = 0;
		count = myCouponDao.isusedupdate(couPonId, userId, couPonIsUsed);
		pubTools.writeText(response, String.valueOf(count));
		
	}else if (action.equals("myCouponDelete")) {
		Integer userId = jsonObject.get("userId").getAsInt();
		Integer couPonId = jsonObject.get("couPonId").getAsInt();
		int count = 0;
		count = myCouponDao.delete(userId, couPonId);
		pubTools.writeText(response, String.valueOf(count));
		
	}else if (action.equals("getCouponById")) {
		Integer userId = jsonObject.get("userId").getAsInt();
		Integer couPonId = jsonObject.get("couPonId").getAsInt();
		List<MyCoupon> couponList = myCouponDao.getCouponById(userId, couPonId);
		pubTools.writeText(response, String.valueOf(couponList));
		
	}else if (action.equals("setcouPonIsUsedStatus")) {
		id = jsonObject.get("userId").getAsInt();
		Boolean couPonIsUsedStatus = jsonObject.get("couPonIsUsedStatus").getAsBoolean();
		pubTools.showConsoleMsg("userId: ", id.toString());
		pubTools.showConsoleMsg("couPonIsUsedStatus: ", String.valueOf(couPonIsUsedStatus));
		int count = 0;
		//count = MyCouponDao.setcouPonIsUsedStatus(id, couPonIsUsedStatus);
		pubTools.writeText(response, String.valueOf(count));
	}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (myCouponDao == null) {
			myCouponDao = new MyCouponDaoImpl();
		}
		List<MyCoupon> myCouponList = myCouponDao.getAll();
		pubTools.writeText(response, new Gson().toJson(myCouponList));
		List<String> strList = new ArrayList<String>();
		strList.add("3");
		strList.add("6");
		pubTools.writeText(response, new Gson().toJson(strList));
		List<MyCoupon> myCouponListById = myCouponDao.getAllById(3);
		pubTools.writeText(response, new Gson().toJson(myCouponListById));
		
	}

	
	

}
