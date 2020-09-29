package server.coupon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
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
import server.ress.Res;
import server.ress.ResDaoMySqlImpl;


@WebServlet("/CouponServlet")
public class CouponServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	private CouponDao couponDao = null;
       
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
		if (couponDao == null) {
			couponDao = new CouponDaoImpl();
		}		
		String action = jsonObject.get("action").getAsString();
		int id;
		Coupon coupon;
		byte[] image;

		if (action.equals("getAll")) {
			List<Coupon> coupons = couponDao.getAll();
			writeText(response, gson.toJson(coupons));
		} else if (action.equals("getAllEnable")) {
			List<Coupon> coupons = couponDao.getAllEnable();
			writeText(response, gson.toJson(coupons));
		} else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			id = jsonObject.get("id").getAsInt();
			
			System.out.println("id: " + id);
			
			int imageSize = jsonObject.get("imageSize").getAsInt();
			image = couponDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType("image/jpeg");
				response.setContentLength(image.length);
				os.write(image);}
		
		} else if (action.equals("couponInsert") || action.equals("couponUpdate")) {
			String couponJson = jsonObject.get("coupon").getAsString();
			
			coupon = gson.fromJson(couponJson, Coupon.class);
			image = null;
		
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			int count = 0;
			if (action.equals("couponInsert")) {
				count = couponDao.insert(coupon, image);
			} else if (action.equals("couponUpdate")) {
				count = couponDao.update(coupon, image);
			}
			writeText(response, String.valueOf(count));
		} else if (action.equals("couponDelete")) {
			int couponId = jsonObject.get("Id").getAsInt();
			int count = couponDao.delete(id);
			writeText(response, String.valueOf(count));
		} else if (action.equals("findById")) {
			id = jsonObject.get("couponid").getAsInt();
			List<Coupon> coupons = couponDao.findById(couPonId);	
			writeText(response, gson.toJson(coupons));
		} else if (action.equals("findById")) {
			id = jsonObject.get("id").getAsInt();
			coupon = couponDao.findById(id);
			writeText(response, gson.toJson(coupon));
		}else {
			writeText(response, "");
		}
	}
	private void writeText(HttpServletResponse response, String valueOf) {
		// TODO Auto-generated method stub
		
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
}
