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

import server.articleGood.ArticleGood;

@WebServlet("/ArticleServlet")
public class ArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; // 序列化標籤
	private final static String CONTENT_TYPE = "text/html; charset=utf-8"; // 編碼
	ArticleDao articleDao = null;

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

		// 宣告jsonObject
		JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);
		// Dao不為空值的話，取得實作方法內容
		if (articleDao == null) {
			articleDao = new ArticleDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);

		// 判斷client端行為1 > 取得所有資料庫資料(新進榜)
		if (action.equals("getAllById")) {
			 int loginUserId = jsonObject.get("loginUserId").getAsInt();
			List<Article> articles = articleDao.getAllById(loginUserId);
			writeText(response, gson.toJson(articles));
		}
		// 判斷client端行為2 > 取得所有資料庫資料(排行榜)
		else if (action.equals("getAllByIdRank")) {
			int loginUserId = jsonObject.get("loginUserId").getAsInt();
			List<Article> articles = articleDao.getAllByIdRank(loginUserId);
			writeText(response, gson.toJson(articles));
		}
		// 判斷client端行為3 > 取得所有資料庫資料(收藏榜)
		else if (action.equals("getAllByIdFavorite")) {
			int loginUserId = jsonObject.get("loginUserId").getAsInt();
			List<Article> articles = articleDao.getAllByIdFavorite(loginUserId);
			writeText(response, gson.toJson(articles));
		}
		// 判斷client端行為4 > insert點讚
		else if (action.equals("articleGoodInsert")) {
//			String articleGoodJson = jsonObject.get("articleGood").getAsString(); // 取得jsonObject
//			System.out.println("articleGoodJson = " + articleGoodJson);
//			Article articleGood = gson.fromJson(articleGoodJson, Article.class); // 將Json轉為commentGood型態
			int articleId = jsonObject.get("articleId").getAsInt();
			int loginUserId = jsonObject.get("loginUserId").getAsInt();
			int count = 0;
			if (action.equals("articleGoodInsert")) {
				count = articleDao.articleGoodInsert(articleId, loginUserId);
			}
			writeText(response, String.valueOf(count));

			// 判斷client端行為5 > 取消點讚
		} else if (action.equals("articleGoodDelete")) {
			System.out.println("articleGoodDelete: " + action);
			int articleId = jsonObject.get("articleId").getAsInt();
			int userId = jsonObject.get("userId").getAsInt();
			int count = articleDao.articleGoodDelete(articleId, userId);
			writeText(response, String.valueOf(count));
		}

		// 判斷client端行為6 > insert收藏
		else if (action.equals("articleFavoriteInsert")) {
//			String articleFavoriteJson = jsonObject.get("articleFavorite").getAsString(); // 取得jsonObject
//			System.out.println("articleFavoriteJson = " + articleFavoriteJson);
//			Article articleFavorite = gson.fromJson(articleFavoriteJson, Article.class); // 將Json轉為javaBean型態
			int articleId = jsonObject.get("articleId").getAsInt();
			int loginUserId = jsonObject.get("loginUserId").getAsInt();
			int count = 0;
			if (action.equals("articleFavoriteInsert")) {
				count = articleDao.articleFavoriteInsert(articleId, loginUserId);
			}
			writeText(response, String.valueOf(count));
		}

		// 判斷client端行為7 > 取消收藏
		else if (action.equals("articleFavoriteDelete")) {
			System.out.println("articleFavoriteDelete: " + action);
			int loginUserId = jsonObject.get("loginUserId").getAsInt();
			int articleId = jsonObject.get("articleId").getAsInt();
			int count = articleDao.articleFavoriteDelete(loginUserId, articleId);
			writeText(response, String.valueOf(count));
		}

		// 判斷client端行為8 > 判斷insert或Update(文章)
		else if (action.equals("articleInsert") || action.equals("articleUpdate") || action.equals("articleDelete")) {
			String articleJson = jsonObject.get("article").getAsString(); // 取得article內的Json字串
			System.out.println("articleJson = " + articleJson);
			Article article = gson.fromJson(articleJson, Article.class); // 將Json轉為article型態

			int count = 0;
			// insert(寫文章)
			if (action.equals("articleInsert")) {
				count = articleDao.insert(article);
				// Update(更新文章)
			} else if (action.equals("articleUpdate")) {
				count = articleDao.update(article);
			}
				//delete(刪除文章)
			else if (action.equals("articleDelete")) {
				count = articleDao.delete(article);
			}
			writeText(response, String.valueOf(count));
		}
		// 判斷client端行為 > Delete(刪除文章)
//		else if (action.equals("articleDelete")) {
//			int articleId = jsonObject.get("articleId").getAsInt();
//			int count = articleDao.delete(articleId);
//			writeText(response, String.valueOf(count)); 
//		}
		// 判斷client端行為9 > 查詢(顯示內文)findById
		else if (action.equals("findById")) {
			int articleId = jsonObject.get("articleId").getAsInt();
			int loginUserId = jsonObject.get("loginUserId").getAsInt();
			Article article = articleDao.findById(loginUserId, articleId);
			writeText(response, gson.toJson(article));
		}
		// 判斷client端行為1 > 其他
		else {
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
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		if (articleDao == null) {
//			articleDao = new ArticleDaoImpl();
//		}
//		List<Article> articles = articleDao.getAllById();
//		writeText(response, new Gson().toJson(articles));
//	}

}
