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
		Gson gson = new Gson();
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
		
	}else if (action.equals("myCouponDelete")) {
		Integer userId = jsonObject.get("userId").getAsInt();
		Integer couPonId = jsonObject.get("couPonId").getAsInt();
		int count = 0;
		count = myCouponDao.delete(userId, couPonId);
		pubTools.writeText(response, String.valueOf(count));
		
	}else if (action.equals("getCouponById")) {
		id = jsonObject.get("id").getAsInt();
		Integer userId = jsonObject.get("userId").getAsInt();
		List<Coupon> couponList = myCouponDao.getCouponById(id, userId);
		pubTools.writeText(response, String.valueOf(couponList));
		
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
