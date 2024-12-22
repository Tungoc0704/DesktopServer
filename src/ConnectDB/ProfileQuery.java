package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ProfileQuery {

	// query so nguoi dang theo doi iduser = ...
	public int countFollower(int idUser) {

		int numFollowing = 0;
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

		try {
			String numFollowerSQL = "select\r\n" + "	count(followedID) as 'numFollowing' " + " from\r\n"
					+ "	follows f2\r\n" + "where\r\n" + "	f2.followedID = " + idUser + " group by\r\n"
					+ "	f2.followedID";
			Statement stm = connection.createStatement();
			ResultSet rss = stm.executeQuery(numFollowerSQL);

			if (rss.next()) {
				numFollowing = rss.getInt("numFollowing");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return numFollowing;
	}

	// query so nguoi do userID theo dõi:
	public int countFollowing(int idUser) {
		int numFollowing = 0;
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

		String numFollowingSQL = "select count(followerID) as 'numFollower' from follows f where f.followerID = "
				+ idUser + " group by f.followerID ";
		Statement stm;
		try {
			stm = connection.createStatement();
			ResultSet rss = stm.executeQuery(numFollowingSQL);
			if (rss.next()) {
				numFollowing = rss.getInt("numFollower");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numFollowing;
	}

	public JSONObject getProfileUser(int idUser) {
		JSONObject profileUser = new JSONObject();
		try {
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

			String queryUser = "select * from users where userID = " + idUser;
			Statement stm = connection.createStatement();
			ResultSet rss = stm.executeQuery(queryUser);
			while (rss.next()) {
				profileUser.put("userID", rss.getInt("userID"));
				profileUser.put("username", rss.getString("username"));
				profileUser.put("password", rss.getString("password"));
				profileUser.put("email", rss.getString("email"));
				profileUser.put("profile_picture", rss.getString("profile_picture"));
				profileUser.put("bio", rss.getString("bio"));
				profileUser.put("created_at", rss.getString("created_at"));
				profileUser.put("name", rss.getString("name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return profileUser;
	}

	// lấy các bài post của userid
	public JSONArray getPosts(int userID) {
		JSONArray listPost = new JSONArray();

		try {
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

			String queryUser = "select * from posts where userID = " + userID;
			Statement stm = connection.createStatement();
			ResultSet rss = stm.executeQuery(queryUser);
			while (rss.next()) {
				JSONObject post = new JSONObject();
				post.put("postID", rss.getInt("post_id"));
				post.put("userID", rss.getString("userID"));
				post.put("image_url", rss.getString("image_url"));
				post.put("caption", rss.getString("caption"));
				post.put("created_at", rss.getString("created_at"));
				listPost.add(post);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPost;
	}

	// so luong bai post:
	public int countNumPost(int userID) {
		int numPost = 0;
		try {
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

			String countPostSQL = "select count(post_id) as 'numPost' from posts\r\n" + "where userID  = " + userID;
			Statement stm = connection.createStatement();
			ResultSet rss = stm.executeQuery(countPostSQL);
			if (rss.next()) {
				numPost = rss.getInt("numPost");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return numPost;

	}

}
