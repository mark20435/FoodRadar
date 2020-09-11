package server.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class PubTools {
	
	public final static String CONTENT_TYPE = "text/html; charset=utf-8";

	public void showConsoleMsg(String TAG ,String strMsg) {
//		String fromName = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
		StringBuilder sb = new StringBuilder();
		sb.append(TAG);
		sb.append(": ");
		sb.append(strMsg);
		System.out.println(sb.toString());		
	}
	
	public void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
		// System.out.println("output: " + outText);
	}
	
}
