package server.commentGood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/CommentGoodServlet")
public class CommentGoodServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8"; // 編碼
	CommentGoodDao commentGoodDao = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 解碼
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonInput = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonInput.append(line);
		}
		// Debug
		System.out.println("input: " + jsonInput);
		System.out.println("MyNowTime: " + new SimpleDateFormat("yyyy-MM-DD hh:mm:ss").format(System.currentTimeMillis()));

		JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);
		// 取得Dao實作方法
		if (commentGoodDao == null) {
			commentGoodDao = new CommentGoodDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<CommentGood> CommentGoods = commentGoodDao.getAll();
			writeText(response, gson.toJson(CommentGoods));
		} 
		//留言點讚功能
		else if (action.equals("commentGoodInsert")) {
			String commentGoodJson = jsonObject.get("commentGood").getAsString();
			System.out.println("commentGoodJson = " + commentGoodJson);
			CommentGood commentGood = gson.fromJson(commentGoodJson, CommentGood.class);
			System.out.println("commentGood = " + commentGood);
			int count = 0;
			if (action.equals("commentGoodInsert")) {
				System.out.println("ActioncommentGood = " + action);
				count = commentGoodDao.insert(commentGood);
				System.out.println("Actioncount = " + count);
			} 		
			// 編碼寫出
			writeText(response, String.valueOf(count));
		} 
		//留言取消讚功能
		else if (action.equals("commentGoodDelete")) {
			int commentId = jsonObject.get("commentId").getAsInt();
			int userId = jsonObject.get("userId").getAsInt();
			int count = commentGoodDao.delete(commentId, userId);
			writeText(response, String.valueOf(count));
		} 
		
		else if (action.equals("findById")) {
			int id = jsonObject.get("commentGoodId").getAsInt();
			CommentGood commentGood = commentGoodDao.findById(id);
			writeText(response, gson.toJson(commentGood));
		} else {
			writeText(response, "");
		}
	}
	

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 印出資料 > Debug用
		System.out.println("output: " + outText);
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (commentGoodDao == null) {
			commentGoodDao = new CommentGoodDaoImpl();
		}
		List<CommentGood> commentGoods = commentGoodDao.getAll();
		writeText(response, new Gson().toJson(commentGoods));
	}

}
