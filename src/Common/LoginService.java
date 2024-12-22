package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.security.auth.callback.Callback;

public interface LoginService extends Remote {
	void login(String username, String password, Common.Callback callback) throws RemoteException;
//	void updateProfile(String username, String newName, Cal)

}
