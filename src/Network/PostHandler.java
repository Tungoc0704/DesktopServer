// Gói Network

package Network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ConnectDB.InsertTuple;
import ConnectDB.Lists;
import Model.User;

public class PostHandler implements HttpHandler {
	private static final String UPLOAD_DIR = "C:\\SERVER\\posts\\";

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("POST".equals(exchange.getRequestMethod())) {
			// Đọc dữ liệu:
			InputStream inputStream = exchange.getRequestBody();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder requestBody = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				requestBody.append(line);
			}

			// convert requestBody thanhf json
			JSONObject requestJson = (JSONObject) org.json.simple.JSONValue.parse(requestBody.toString());

			String action = (String) requestJson.get("action");
			if (action.equals("UPLOAD_POST")) {
				System.out.println("action post");
				Long uploadPerson = (Long) requestJson.get("uploadPerson");
				String caption = (String) requestJson.get("caption");
				String image_url = (String) requestJson.get("image_url"); // imageURL : C://download/....
				String created_at = (String) requestJson.get("created_at");

				// insert database into table posts:
				new InsertTuple().createPost(uploadPerson.intValue(), caption, validateImageUrl(image_url), created_at);

				JSONObject responseJSON = returnPost(uploadPerson.intValue(), caption, validateImageUrl(image_url),
						created_at);

				// thong bao phan hoi cho client:
				responsePost(responseJSON, exchange);

			} else if (action.equals("REQUEST_LOAD_POST")) {
				// return list post
				JSONArray listPost = new Lists().listPost();
				returnListPost(listPost, exchange);

			}
		}
	}

	public JSONObject returnPost(int uploadPerson, String caption, String image, String createdAt) {
		JSONObject postJSON = new JSONObject();
		try {
			List<User> users = new Lists().getUsers();
			for (User us : users) {
				if (us.getUserID() == uploadPerson) {

					JSONObject userJSONObject = new JSONObject();
					userJSONObject.put("userID", us.getUserID());
					userJSONObject.put("username", us.getUsername());
					userJSONObject.put("nickname", us.getName());
					userJSONObject.put("avatar", us.getProfile_picture());

					postJSON.put("action", "RETURN_POST");
					postJSON.put("uploadPerson", userJSONObject.toJSONString());
					postJSON.put("caption", caption);
					postJSON.put("image", image);
					postJSON.put("create_at", createdAt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return postJSON;
	}

	// tra response bai viet vua moi dang cua client:
	public void responsePost(JSONObject jsonObject, HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().set("Content-type", "application/json");
		exchange.sendResponseHeaders(200, (jsonObject.toJSONString()).getBytes().length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write((jsonObject.toJSONString()).getBytes());
		}
	}

	// return list post:
	public void returnListPost(JSONArray listPost, HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().set("Content-type", "application/json");
		exchange.sendResponseHeaders(200, (listPost.toJSONString()).getBytes().length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write((listPost.toJSONString()).getBytes());
		}
	}

	// xu li chuoi imageUrl ( chi lay ten image, k lay filePath):
	public String validateImageUrl(String imageUrl) {
		String[] stringArr = imageUrl.split("/");
		String validateUrl = stringArr[stringArr.length - 1];
		System.out.println("img: " + validateUrl);
		return validateUrl;
	}
}
