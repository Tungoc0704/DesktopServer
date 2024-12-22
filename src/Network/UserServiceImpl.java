package Network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;

import Common.UserService;
import ConnectDB.Lists;
import ConnectDB.InsertTuple;
import DataSecurity.AccountSecurity;
import Model.User;
import Utils.EmailSender; // Class hỗ trợ gửi email
//import Utils.SmsSender;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {

	private static final String NUMERIC_CHARACTERS = "0123456789";

	public static String TEST_EMAIL = "";

	private AccountSecurity accountSecurity = new AccountSecurity();

	private String OTPCode = "";

	private String PASSWORD = "";
	private String USERNAME = "";
	private String EMAIL = "";

	public String getOTPCode() {
		return this.OTPCode;
	}

	public String getUsername() {
		return this.USERNAME;
	}

	public String getPassword() {
		return this.PASSWORD;
	}

	public String getEmail() {
		return this.EMAIL;
	}

	public UserServiceImpl() throws RemoteException {
		super();
	}
	@Override
	public String register(String username, String password, String emailOrPhone) throws RemoteException {
		System.out.println("Received register request: " + username);
		USERNAME = username;
		System.out.println("Password: " + password);
		PASSWORD = password;
		System.out.println("email or phone: " + emailOrPhone);
		EMAIL = emailOrPhone;

		JSONObject responseJSON = new JSONObject();

		// check duplicate username:
		try {
			List<User> listUser = new Lists().getUsers();
			for (User user : listUser) {
				if (user.getUsername().equalsIgnoreCase(username)) {
					responseJSON.put("status", "USERNAME_EXISTS");
					responseJSON.put("inform", "Username already exists !");
					return responseJSON.toJSONString();

				}
			}

			// gửi mã OTP để client xác nhận: (OTP có length = 6);
			OTPCode = generateSecureOTP(6);
			if (emailOrPhone.contains("@")) {
				try {
					TEST_EMAIL = emailOrPhone;
					EmailSender.sendOtpEmail("VERIFY CODE", "This is your verify code: " + OTPCode);
					responseJSON.put("status", "PENDING");
					responseJSON.put("inform", "OTP has been sent. Please verify!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			else {
//				SmsSender.sendOtpSms(emailOrPhone, otp);
//			}

			// check client đã nhập OTP đúng chưa ?:
			return responseJSON.toJSONString();

		} catch (SQLException e) {
			e.printStackTrace();
			responseJSON.put("status", "error");
			responseJSON.put("inform", "Error during registration !");
			return responseJSON.toJSONString();
		}

	}

	// generate OTP Code :
	public String generateSecureOTP(int length) {
		if (length <= 0) {
			throw new IllegalAccessError("OTP length must be greater than 0");
		}
		SecureRandom secureRandom = new SecureRandom();
		StringBuilder OTP = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int randomIndex = secureRandom.nextInt(NUMERIC_CHARACTERS.length());
			OTP.append(NUMERIC_CHARACTERS.charAt(randomIndex));
		}
		return OTP.toString();
	}

	@Override
	public String verifyOTP(String otp) throws Exception {
		System.out.println("OTP: " + otp);
		System.out.println("Generate code: " + getOTPCode());

		JSONObject verifyJSON = new JSONObject();
		if (getOTPCode().equals(otp)) {

			// call ham ma hoa password truoc khi call ham insertAccount();
			String hashPassword = accountSecurity.hashPassword(getPassword());
			String encryptEmail = accountSecurity.encryptEmail(getEmail(), accountSecurity.generateKey());

			// add new account into table users in DB:
			InsertTuple updateNewAccount = new InsertTuple();
			updateNewAccount.insertAccount(getUsername(), hashPassword, encryptEmail);
			System.out.println("Insert new account successfully");

			// send JSONString to client:
			verifyJSON.put("status", "SUCCESSFUL_REGISTRATION");
			verifyJSON.put("inform", "Registration is successful");

		} else {
			verifyJSON.put("status", "FAILURE_REGISTRATION");
			verifyJSON.put("inform", "Invalid OTP Code, registration is not successful");
		}
		return verifyJSON.toJSONString();
	}

}
