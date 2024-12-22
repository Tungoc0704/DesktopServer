package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback extends Remote {

	void notifyClient(String notify) throws RemoteException;

}
