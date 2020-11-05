package server.ress;

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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import server.category.Category;
import server.img.Img;
import server.main.ImageUtil;
import server.ress.ResRating;

@WebServlet("/ResServlet")
public class ResServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	ResDao resDao = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//Gson gson = new Gson();
		Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("ResServlet.input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (resDao == null) {
			resDao = new ResDaoMySqlImpl();
		}
		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<Res> ress = resDao.getAll();
			writeText(response, gson.toJson(ress));
		} else if (action.equals("getAllEnable")) {
			int userId = jsonObject.get("userId").getAsInt();
			List<Res> ress = resDao.getAllEnable(userId);
			writeText(response, gson.toJson(ress));
		} else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = resDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType("image/jpeg");
				response.setContentLength(image.length);
				os.write(image);
			}
		} else if (action.equals("getCategories")) {
			List<Category> Categories = resDao.getCategories();
			writeText(response, gson.toJson(Categories));
		} else if (action.equals("findRatingByResIdAndUserId")) {
			int resId = jsonObject.get("resId").getAsInt();
			int userId = jsonObject.get("userId").getAsInt();
			ResRating resRating = resDao.findRatingByResIdAndUserId(resId, userId);
			writeText(response, gson.toJson(resRating));
		} else if (action.equals("resInsert") || action.equals("resUpdate")) {
			String resJson = jsonObject.get("res").getAsString();
			System.out.println("resJson = " + resJson);
			Res res = gson.fromJson(resJson, Res.class);
			byte[] image = null;

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
			int id = jsonObject.get("id").getAsInt();
			List<Res> ress = resDao.CategoryfindById(id);
			writeText(response, gson.toJson(ress));
		} else if (action.equals("findById")) {
			int id = jsonObject.get("id").getAsInt();
			Res res = resDao.findById(id);
			writeText(response, gson.toJson(res));
		} else if (action.equals("getImgByResId")) {
			int resId = jsonObject.get("resId").getAsInt();
			List<Img> imgs = resDao.getImgByResId(resId);
			writeText(response, gson.toJson(imgs));
		} else if (action.equals("insertResRating") || action.equals("updateResRating")) {
			String resRatingJson = jsonObject.get("resRating").getAsString();
			System.out.println("resRatingJson = " + resRatingJson);
			ResRating resRating = gson.fromJson(resRatingJson, ResRating.class);
			int count = 0;
			if (action.equals("insertResRating")) {
				count = resDao.insertResRating(resRating);
			} else if (action.equals("updateResRating")) {
				count = resDao.updateResRating(resRating);
			}
			writeText(response, String.valueOf(count));
		} else {
			writeText(response, "");
		}
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (resDao == null) {
			resDao = new ResDaoMySqlImpl();
		}
		List<Res> ress = resDao.getAll();
		writeText(response, new Gson().toJson(ress));
	}

}
