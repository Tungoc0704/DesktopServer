package Network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Common.ProfileService;
import ConnectDB.ProfileQuery;

public class ProfileServiceImpl extends UnicastRemoteObject implements ProfileService {

	public ProfileServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public String detailProfile(int idProfile) throws RemoteException {
		System.out.println("id can xem profile: " + idProfile);

		// sau khi nhận được iduser -> return numfollowing và numfollower:
		int numFollowing = new ProfileQuery().countFollowing(idProfile);
		int numFollower = new ProfileQuery().countFollower(idProfile);
		int numPost = new ProfileQuery().countNumPost(idProfile);
		JSONArray posts = new ProfileQuery().getPosts(idProfile);
		JSONObject profileUser = new ProfileQuery().getProfileUser(idProfile);

		// gửi jsonObject.toJSONString cho client:
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("numFollowing", numFollowing);
		responseJSON.put("numFollower", numFollower);
		responseJSON.put("numPost", numPost);
		responseJSON.put("posts", posts);
		responseJSON.put("profileUser", profileUser);

		return responseJSON.toJSONString();
	}

}
