package server.article;

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

@WebServlet("/ArticleServlet")
public class ArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; // 序列化標籤
	private final static String CONTENT_TYPE = "text/html; charset=utf-8"; // 編碼
	ArticleDao articleDao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); //解碼
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonInput = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonInput.append(line);
		}
		// 印出資料 > Debug用
		// System.out.println("input: " + jsonInput);

		// 宣告jsonObject
		JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);
		// Dao不為空值的話，取得實作方法內容
		if (articleDao != null) {
			articleDao = new ArticleDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();

		// 判斷client端行為1 > 取得資料庫資料
		if (action.equals("getAllById")) {
//			int articleId = jsonObject.get("articleId").getAsInt();
			List<Article> articles = articleDao.getAllById();
			writeText(response, gson.toJson(articles));
			// 判斷client端行為2 > insert或Update
		} else if (action.equals("articleInsert") || action.equals("articleUpdate")) { 
			// 取得article內的Json字串
			String articleJson = jsonObject.get("article").getAsString();
			System.out.println("articleJson = " + articleJson);

			// 將Json轉為article型態
			Article article = gson.fromJson(articleJson, Article.class);

			int count = 0;
			// insert
			if (action.equals("articleInsert")) {
				count = articleDao.insert(article);
			// Update
			} else if (action.equals("articleUpdate")) {
				count = articleDao.update(article);
			}
			writeText(response, String.valueOf(count));
			// 判斷client端行為3 > Delete
		} else if (action.equals("articleDelete")) {
			int articleId = jsonObject.get("articleId").getAsInt();
			int count = articleDao.delete(articleId);
			writeText(response, String.valueOf(count));
			// 判斷client端行為4 > 查詢findById
		} else if (action.equals("findById")) {
			int id = jsonObject.get("articleId").getAsInt();
			Article article = articleDao.findById(id);
			writeText(response, gson.toJson(article));
			// 判斷client端行為5 > 其他
		} else {
			writeText(response, "");
		}
	}

	// 將response轉成字串並寫出
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 印出資料 > Debug用
		System.out.println("output: " + outText);
	}

	// 網頁(測試)用
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (articleDao == null) {
			articleDao = new ArticleDaoImpl();
		}
		List<Article> articles = articleDao.getAll();
		writeText(response, new Gson().toJson(articles));
	}

}
