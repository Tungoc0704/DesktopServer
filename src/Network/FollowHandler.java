package Network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ConnectDB.InsertTuple;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.io.OutputStream;

public class FollowHandler implements HttpHandler {
	private InsertTuple insertTuple = new InsertTuple();

	@Override
	public void handle(HttpExchange exchange) {
		try {
			// Chỉ xử lý yêu cầu POST
			if ("POST".equals(exchange.getRequestMethod())) {
				// Đọc dữ liệu JSON từ client
				JSONParser parser = new JSONParser();
				JSONObject requestJson = (JSONObject) parser
						.parse(new InputStreamReader(exchange.getRequestBody(), "utf-8"));

				String action = (String) requestJson.get("action");
				if ("FOLLOW_USER".equals(action)) {
					int userID = ((Long) requestJson.get("followerID")).intValue();
					int idFollowed = ((Long) requestJson.get("followedID")).intValue();

					// Nếu state == "Follow":
					if (((String) requestJson.get("state")).equalsIgnoreCase("Follow")) {
						// Xử lý follow (lưu vào database table follows)
						insertTuple.insertFollower(userID, idFollowed);
						
						// gửi phản hồi cho client:
						sendResponse(exchange, 200, "Follow succesfully");
					}
					// Nếu state == "Unfollow":
					else if (((String) requestJson.get("state")).equalsIgnoreCase("Unfollow")) {
						// remove khỏi database:
						insertTuple.unfollower(userID, idFollowed);
						
						// thông báo unfollow thành công:
						sendResponse(exchange, 200, "Unfollow successfully");
					}
				} else {
					sendResponse(exchange, 400, "Invalid action");
				}
			} else {
				exchange.sendResponseHeaders(405, -1); // Method Not Allowed
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				exchange.sendResponseHeaders(500, -1); // Internal Server Error
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// Helper method để gửi phản hồi
	private void sendResponse(HttpExchange exchange, int statusCode, String message) throws Exception {
		JSONObject responseJson = new JSONObject();
		responseJson.put("action", "FOLLOW_RESPONSE");
		responseJson.put("status", message);
		String response = responseJson.toJSONString();

		exchange.sendResponseHeaders(statusCode, response.length());
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response.getBytes("utf-8"));
		}
	}
}
