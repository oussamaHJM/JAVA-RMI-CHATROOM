package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface userInterface extends Remote {
    void displayMessage(String message) throws RemoteException;
    String getUsername() throws  RemoteException;
    String getPassword() throws RemoteException;
}
