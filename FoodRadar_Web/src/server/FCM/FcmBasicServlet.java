package server.FCM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class FcmBasicServlet
 */
@WebServlet("/FcmBasicServlet")
public class FcmBasicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
	// client送來的token
	private String registrationToken = "";
	// 儲存所有client送來的token,類別或實體變數不會有執行緒安全性， synchronizedset執行緒安全性
	private static final Set<String> registrationTokens = Collections.synchronizedSet(new HashSet<>());
    
	@Override
	public void init() throws ServletException {
		// 私密金鑰檔案可以儲存在專案以外
		// File file = new File("/path/to/firsebase-java-privateKey.json");
		// 私密金鑰檔案也可以儲存在專案內
		try (InputStream in = getServletContext().getResourceAsStream("/my-foodradarapi1016-firebase-adminsdk-jlmsm-7f134274ac.json")) {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(in))
					.build();
			FirebaseApp.initializeApp(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		String action = jsonObject.get("action").getAsString();
		switch (action) {
		case "register":
			// 將client送來的token儲存在單一變數與set內，為了之後 單一/群組 發送訊息
			registrationToken = jsonObject.get("registrationToken").getAsString();
			registrationTokens.add(registrationToken);
			System.out.println("token: " + registrationToken + "\ntokens: " + registrationTokens);
			writeText(response, "Registration token is received!");
			break;
		case "singleFcm":
			sendSingleFcm(jsonObject, registrationToken);
			writeText(response, "Single FCM is sent!");
			break;
		case "groupFcm":
			sendGroupFcm(jsonObject, registrationTokens);
			writeText(response, "Group FCMs are sent!");
			break;
		case "subscriberFcm": // 發送訂閱
			sendSubscriber(jsonObject.get("topic").getAsString());
			writeText(response, "Group FCMs are sent!");
			break;
		default:
			break;
		}
	}
	private void sendSingleFcm(JsonObject jsonObject, String registrationToken) {
		String title = jsonObject.get("title").getAsString();
		String body = jsonObject.get("body").getAsString();
		String data = jsonObject.get("data").getAsString();

		// 主要設定訊息標題與內容，client app一定要在背景時才會自動顯示
		Notification notification = Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build();
		// 發送notification message
		Message message = Message.builder()
				.setNotification(notification) // 設定client app在背景時會自動顯示訊息
				.putData("data", data) // 設定自訂資料，user點擊訊息時方可取值
				.setToken(registrationToken)
				.build();

		String messageId = "";
		try {
			messageId = FirebaseMessaging.getInstance().send(message);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
		System.out.println("messageId: " + messageId);
	}
	private void sendGroupFcm(JsonObject jsonObject, Set<String> registrationTokens) {
		String title = jsonObject.get("title").getAsString();
		String body = jsonObject.get("body").getAsString();
		String data = jsonObject.get("data").getAsString();

		Notification notification = Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build();

		MulticastMessage message = MulticastMessage.builder()
				.setNotification(notification)
				.putData("data", data)
				.addAllTokens(registrationTokens)//多個裝置的token
				.build();
		BatchResponse response;
		try {
			response = FirebaseMessaging.getInstance().sendMulticast(message);
		    System.out.println(response.getSuccessCount() + " messages were sent successfully");
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}
	
	private void sendSubscriber(String topic) {
		// 主要設定訊息標題與內容，client app一定要在背景時才會自動顯示
		Notification notification = Notification.builder()
				.setTitle("健康資訊")
				.setBody("新冠肺炎今日確診數為0")
				.build();
		// 發送notification message
		Message message = Message.builder()
				.setNotification(notification) // 設定client app在背景時會自動顯示訊息
				.putData("data", "附註：請大家勤洗手") // 設定自訂資料，user點擊訊息時方可取值
				.setTopic(topic)
				.build();
		// Send a message to the devices subscribed to the provided topic.
		String response;
		try {
			response = FirebaseMessaging.getInstance().send(message);
			System.out.println("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}

	}
	
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
		// System.out.println("output: " + outText);
	}
	protected void doGet(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
		rp.setContentType(CONTENT_TYPE);
		PrintWriter out = rp.getWriter();
		out.println("<h3>FCM Basic Demo</h3>");
	}
}
