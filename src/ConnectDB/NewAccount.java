package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewAccount {

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
			prepareStm.setNString(1, username);
			prepareStm.setString(2, hashPassword);
			prepareStm.setString(3, encryptEmail);
			prepareStm.setString(4, null);
			prepareStm.setString(5, null);
			prepareStm.setString(6, null);
			prepareStm.setString(7, null);

			prepareStm.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// thoi gian tao newAccount:
	public String getCreatedAt() {
		// Lấy thời gian hiện tại
		LocalDateTime currentDateTime = LocalDateTime.now();

		// Định dạng thời gian theo kiểu "dd:mm:yyyy H:M:S"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
		String formattedTime = currentDateTime.format(formatter);
		return formattedTime;
	}

}
