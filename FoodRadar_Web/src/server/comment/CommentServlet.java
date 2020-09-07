package server.comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;



@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	CommentDao commentDao;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		// 將輸入資料列印出來除錯用
		System.out.println("input: " + jsonIn);
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (commentDao == null) {
			commentDao = new CommentDaoImpl();
		}
		
		String action = jsonObject.get("action").getAsString();
		
		if (action.equals("getAll")) {
			List<Comment> comments = commentDao.getAll();
			writeText(response, gson.toJson(comments));
		} else if (action.equals("commentInsert") || action.equals("commentUpdate")) { 
			// 取得Comment內的Json字串
			String commentJson = jsonObject.get("comment").getAsString();
			System.out.println("commentJson = " + commentJson);

			// 將Json轉為Comment型態
			Comment comment = gson.fromJson(commentJson, Comment.class);

			int count = 0;
			// insert
			if (action.equals("commentInsert")) {
				count = commentDao.insert(comment);
			// Update
			} else if (action.equals("commentUpdate")) {
				count = commentDao.update(comment);
			}
			writeText(response, String.valueOf(count));
			// 判斷client端行為3 > Delete
		} else if (action.equals("commentDelete")) {
			int commentId = jsonObject.get("commentId").getAsInt();
			int count = commentDao.delete(commentId);
			writeText(response, String.valueOf(count));
			// 判斷client端行為4 > 查詢findById
		} else if (action.equals("findById")) {
			int id = jsonObject.get("commentId").getAsInt();
			Comment comment = commentDao.findById(id);
			writeText(response, gson.toJson(comment));
			// 判斷client端行為5 > 其他
		} else {
			writeText(response, "");
		}
		
	
	}
	
	// 將response轉成字串並寫(編碼)
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// Debug用
		System.out.println("output: " + outText);
		
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (commentDao == null) {
			commentDao = new CommentDaoImpl();
		}
		List<Comment> comments = commentDao.getAll();
		writeText(response, new Gson().toJson(comments));
	}

}
