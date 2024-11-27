package Network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mindrot.jbcrypt.BCrypt;

import ConnectDB.Lists;
import DataSecurity.AccountSecurity;
import Model.User;

public class HandleLogin {
	private JSONObject json = new JSONObject();
//	public static List<Integer> onlineClients = new ArrayList<Integer>();
	private AccountSecurity accSecurity = new AccountSecurity();

	public void handleLogin(JSONObject jsonObject, DatagramSocket serverSocket, int port, InetAddress addr) {
		try {
			String username = (String) jsonObject.get("username");
			System.out.println("U: " + username);
			String password = (String) jsonObject.get("password");
			System.out.println("P: " + password); /// password lúc nayf client gửi đến chưa mã hóa

			String notify = "NOT FIND ACCOUNT";
			List<User> users = new Lists().getUsers();
			for (User user : users) {
				System.out.println("ID user: " + user.getUserID());
				if (user.getUsername().equals(username)) {
					System.out.println("pw: " + user.getPassword());
					if (BCrypt.checkpw(password, user.getPassword()) == true) {
						System.out.println("pw of user: " + user.getPassword());
						notify = "APPROPRIATE ACCOUNT";
						json.put("notify", notify);
						json.put("user_id", user.getUserID());

					} else {
						notify = "INCORRECT PASSWORD";
						json.put("notify", notify);
					}
					break;
				}
			}

			if (notify.equals("NOT FIND ACCOUNT")) {
				notify = "INCORRECT USERNAME";
				json.put("notify", notify);
			}
			responseLogin(serverSocket, json.toJSONString(), addr, port);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void responseLogin(DatagramSocket sSocket, String responseData, InetAddress addr, int port) {
		try {
			byte data[] = responseData.getBytes();
			DatagramPacket responsePacket = new DatagramPacket(data, data.length, addr, port);
			sSocket.send(responsePacket);
			System.out.println("Server responded login");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
