package metier;



import rmi.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class UserImpl extends UnicastRemoteObject implements userInterface, Runnable {

    private String username;
    private String password;
    private String message;
    private chatInterface server;
    private Interface gui;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGui(Interface gui) {
        this.gui = gui;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setServer(chatInterface server) {
        this.server = server;
    }

    public UserImpl() throws RemoteException {
        super();
    }

    public void displayMessage(String message) throws RemoteException {
        gui.displayMessage(message);
        System.out.println("message !!!!!!!!!!"+message);
    }

    public String getUsername() throws RemoteException {
        return this.username;
    }

    public String getPassword() throws RemoteException {
        return this.password;
    }

    public void run() {
        try {
            this.setUsername(gui.getLoginTextField().getText());
            this.setPassword(gui.getPasswordField().getText());
            server.registerClient(this);
            this.gui.dislayOnlineUsers(server.getOnlineUsers());
        }catch (Exception e){
            e.printStackTrace();
        }
        while (true){
            try {
                if (message != null)
                    server.sendMessage(this.username,message);
               // this.gui.dislayOnlineUsers(server.getOnlineUsers());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
