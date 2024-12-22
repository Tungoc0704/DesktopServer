package Network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javax.security.auth.callback.Callback;

import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import Common.LoginService;
import ConnectDB.Lists;
import Model.User;

public class LoginServiceImpl extends UnicastRemoteObject implements LoginService {

	public LoginServiceImpl() throws RemoteException {
		super();

		// list User:

	}

	@Override
	public void login(String username, String password, Common.Callback callback) throws RemoteException {
		try {
			JSONObject notifyJSON = new JSONObject();

			String notify = "NOT FIND ACCOUNT";
			List<User> users = new Lists().getUsers();
			for (User user : users) {
				if (user.getUsername().equals(username)) {
					if (BCrypt.checkpw(password, user.getPassword()) == true) {
						notify = "APPROPRIATE ACCOUNT";
						int user_id = user.getUserID();
						notifyJSON.put("notify", notify);
						notifyJSON.put("user_id", user_id);
						callback.notifyClient(notifyJSON.toJSONString());
					} else {
						notify = "INCORRECT PASSWORD";
						notifyJSON.put("notify", notify);
						callback.notifyClient(notifyJSON.toJSONString());
					}
					break;
				}
			}

			if (notify.equals("NOT FIND ACCOUNT")) {
				notify = "INCORRECT USERNAME";
				notifyJSON.put("notify", notify);
				callback.notifyClient(notifyJSON.toJSONString());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
