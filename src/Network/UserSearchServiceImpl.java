package Network;

import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import Common.UserSearchService;
import ConnectDB.Lists;
import Model.User;

public class UserSearchServiceImpl extends UnicastRemoteObject implements UserSearchService {

	private List<User> users;

	public UserSearchServiceImpl() throws Exception {
		super();
	}

	@Override
	public List<User> searchUsers() {
		try {
			users = new Lists().getUsers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
}
