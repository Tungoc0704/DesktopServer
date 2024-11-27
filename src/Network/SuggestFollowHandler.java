package Network;

import com.sun.net.httpserver.HttpHandler;

import ConnectDB.SuggestFollows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class SuggestFollowHandler implements HttpHandler {
	private SuggestFollows suggestFollows = new SuggestFollows();  // database

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
				// Đọc dữ liệu:
				InputStream inputStream = exchange.getRequestBody();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder requestBody = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					requestBody.append(line);
				}

				// Chuyển requestBody thành JSON
				JSONObject requestJson = (JSONObject) org.json.simple.JSONValue.parse(requestBody.toString());
				String action = (String) requestJson.get("action");
				Long userID = (Long) requestJson.get("userID");

				if ("REQUEST_SUGGEST_FOLLOWS".equals(action)) {
					try {
						// Query list suggestFollows trong db:
						JSONArray suggestedFollows = suggestFollows.getSuggestFollows(userID.intValue());

						// Tạo phản hồi JSON:
						JSONObject responseJSON = generateResponse(suggestedFollows);

						// Phản hồi JSON tới client:
						responseSuggestedFollows(responseJSON, exchange);
						System.out.println("Responsed suggestions...");

					} catch (SQLException e) {
						e.printStackTrace();
					}

				} else {
					// Xử lý yêu cầu không hợp lệ
					exchange.sendResponseHeaders(400, -1);
				}
			} else {
				// Phương thức không được hỗ trợ
				exchange.sendResponseHeaders(405, -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				exchange.sendResponseHeaders(500, -1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// Tạo phản hồi JSON:
	public JSONObject generateResponse(JSONArray suggestedFollows) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "RESPONSE_SUGGESTED_FOLLOWS");
		jsonObject.put("suggestedList", suggestedFollows);
		return jsonObject;

	}

	// Phản hồi tới client:
	public void responseSuggestedFollows(JSONObject jsonObject, HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().set("Content-type", "application/json");
		exchange.sendResponseHeaders(200, (jsonObject.toJSONString()).getBytes().length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write((jsonObject.toJSONString()).getBytes());
		}
	}

}
