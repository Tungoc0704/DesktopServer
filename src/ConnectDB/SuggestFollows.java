package ConnectDB;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Model.User;

public class SuggestFollows {
	public JSONArray getSuggestFollows(int userID) throws SQLException {

		JSONArray suggestedList = new JSONArray();

		Connection connection = null;
		if (connection == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/desktopappdb";
				String nameConnect = "root";
				String pass = "Ngoc@123";
				connection = DriverManager.getConnection(url, nameConnect, pass);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		String followSQL = "select u.userID,u.username,u.password ,u.email ,u.profile_picture ,u.bio , u.created_at, u.name "
				+ "from users u " + "left join follows f1 on u.userID = f1.followedID" + " and f1.followerID = "
				+ userID + " left join follows f2 on u.userID = f2.followerID" + " and f2.followedID =  " + userID
				+ " where u.userID != " + userID + " and f1.followerID is null and f2.followedID is null";

		System.out.println("sql select: " + followSQL);
		java.sql.Statement statement = connection.createStatement();
		

		ResultSet rss = statement.executeQuery(followSQL);
		while (rss.next()) {

			JSONObject jsonObject = putJSONObject(rss.getInt("userID"), rss.getString("username"),
					rss.getString("password"), rss.getString("bio"), rss.getString("email"),
					rss.getString("created_at"), rss.getString("profile_picture"), rss.getString("name"));

			suggestedList.add(jsonObject);
		}

		return suggestedList;
	}

	public JSONObject putJSONObject(int id, String usn, String pw, String bio, String email, String createdAt,
			String avt, String name) {
		JSONObject suggestedFollow = null;
		try {
			suggestedFollow = new JSONObject();
			suggestedFollow.put("suggestedUsername", usn);
			suggestedFollow.put("suggestedPassword", pw);
			suggestedFollow.put("suggestedEmail", email);
			suggestedFollow.put("suggestedAvt", avt);
			suggestedFollow.put("suggestedBio", bio);
			suggestedFollow.put("suggestedCreated_At", createdAt);
			suggestedFollow.put("suggestedName", name);
			suggestedFollow.put("suggestedID", id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return suggestedFollow;
	}

}
