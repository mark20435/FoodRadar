package server.resAddress;

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

import server.ress.Res;
import server.ress.ResDaoMySqlImpl;


@WebServlet("/ResAddressServlet")
public class ResAddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	ResAddressDao resAddressDao = null ;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (resAddressDao == null) {
			resAddressDao = new ResAddressDaoImpl();
		}
		List<ResAddress> resAddress = resAddressDao.getRes();
		writeText(response, new Gson().toJson(resAddress));
	}


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
		if (resAddressDao == null) {
			resAddressDao = new ResAddressDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();
		
		if (action.equals("getRes")) {
			List<ResAddress> resAddresses = resAddressDao.getRes();
			writeText(response, gson.toJson(resAddresses));	
		} else {
			writeText(response, "");
		}
	}


	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);		
	}

}
