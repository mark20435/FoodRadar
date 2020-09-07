package idv.Food.server.ArticleGood;

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

@WebServlet("/ArticleGoodServlet")
public class ArticleGoodServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	ArticleGoodDao articleGoodDao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonInput = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonInput.append(line);
		}
		// 將輸入資料列印出來除錯用
		System.out.println("input: " + jsonInput);

		JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);
		if (articleGoodDao == null) {
			articleGoodDao = new ArticleGoodDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<ArticleGood> articleGoods = articleGoodDao.getAll();
			writeText(response, gson.toJson(articleGoods));
		} else if (action.equals("articleGoodInsert") || action.equals("articleGoodUpdate")) {
			// 取得Json字串
			String articleGoodJson = jsonObject.get("articleGood").getAsString();
			System.out.println("articleGoodJson = " + articleGoodJson);

			// 將Json轉為commentGood型態
			ArticleGood articleGood = gson.fromJson(articleGoodJson, ArticleGood.class);

			int count = 0;
			if (action.equals("articleGoodInsert")) {
				count = articleGoodDao.insert(articleGood);
			} else if (action.equals("commentGoodUpdate")) {
				count = articleGoodDao.update(articleGood);
			}
			writeText(response, String.valueOf(count));
		} else if (action.equals("articleGoodDelete")) {
			int articleGoodId = jsonObject.get("articleGoodId").getAsInt();
			int count = articleGoodDao.delete(articleGoodId);
			writeText(response, String.valueOf(count));
		} else if (action.equals("findById")) {
			int id = jsonObject.get("articleGoodId").getAsInt();
			ArticleGood articleGood = articleGoodDao.findById(id);
			writeText(response, gson.toJson(articleGood));
		} else {
			writeText(response, "");
		}

	}
	
	
	//編碼方法
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 印出資料 > Debug用
		System.out.println("output: " + outText);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
