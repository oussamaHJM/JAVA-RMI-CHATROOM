package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface chatInterface extends Remote {
    public List<userInterface> registerClient(userInterface client) throws RemoteException;
    boolean connect(String username, String password) throws RemoteException;
    void sendMessage(String username, String Message) throws RemoteException;
    List<userInterface> getOnlineUsers() throws RemoteException;
    userInterface searchOnlineUser(String username) throws RemoteException;
    boolean disconnect(String username) throws RemoteException;
}
