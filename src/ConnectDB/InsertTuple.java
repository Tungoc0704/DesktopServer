package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InsertTuple {

	// insert new account into table "users":
	public void insertAccount(String username, String hashPassword, String encryptEmail) {
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

			String insertSQL = "INSERT INTO users (username,password, email,profile_picture,bio, created_at,name) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement prepareStm = connection.prepareStatement(insertSQL);
			prepareStm.setString(1, username);
			prepareStm.setString(2, hashPassword);
			prepareStm.setString(3, encryptEmail);
			prepareStm.setString(4, "R.png");
			prepareStm.setString(5, null);
			prepareStm.setString(6, getCreatedAt());
			prepareStm.setString(7, username);

			prepareStm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// insert tin nhắn mới vào database:
	public void insertMessage(int senderID, int receiverID, String messageText, String sendtime, String messageType) {
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

			String insertMessageSQL = "	insert into messages(senderID, receiverID, message_text, send_time, messageType) values (?,?,?,?,?)";
			PreparedStatement prepareStm = connection.prepareStatement(insertMessageSQL);
			prepareStm.setInt(1, senderID);
			prepareStm.setInt(2, receiverID);
			prepareStm.setString(3, messageText);
			prepareStm.setString(4, sendtime);
			prepareStm.setString(5, messageType);
			prepareStm.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// insert follow
	public void insertFollower(int followerID, int followedID) {
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

			String insertFollowSQL = "insert into follows (followerID, followedID, follow_time) values (?,?,?)";
			PreparedStatement prepareStm = connection.prepareStatement(insertFollowSQL);
			prepareStm.setInt(1, followerID);
			prepareStm.setInt(2, followedID);
			prepareStm.setString(3, getCreatedAt());
			prepareStm.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// unfollow -> delete khỏi db:
	public void unfollower(int followerID, int followedID) {
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

			String deleteFollowSQL = "delete from follows where followerID =  ? and followedID = ? ";

			PreparedStatement stm = connection.prepareStatement(deleteFollowSQL);
			stm.setInt(1, followerID);
			stm.setInt(2, followedID);

			int rowsAffected = stm.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("User with user_id = ... has been deleted.");
			} else {
				System.out.println("No user found with user_id = ...");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// insert posts:
	public void createPost(int userID, String caption, String imageUrl, String createdAt) {
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

				String insertSQL = "insert into posts (userID, image_url, caption,created_at) values (?,?,?,?)";
				PreparedStatement prepareStm = connection.prepareStatement(insertSQL);
				prepareStm.setInt(1, userID);
				prepareStm.setString(2, imageUrl);
				prepareStm.setString(3, caption);
				prepareStm.setString(4, createdAt);
				prepareStm.executeUpdate();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// thoi gian tao newAccount:
	public String getCreatedAt() {
		// Lấy thời gian hiện tại
		LocalDateTime currentDateTime = LocalDateTime.now();

		// Định dạng thời gian theo kiểu "dd:mm:yyyy H:M:S"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss");
		String formattedTime = currentDateTime.format(formatter);
		return formattedTime;
	}

}
