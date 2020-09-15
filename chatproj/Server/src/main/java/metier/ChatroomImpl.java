package metier;

import rmi.chatInterface;
import rmi.userInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatroomImpl extends UnicastRemoteObject implements chatInterface {

    private HashMap<String, String> users = new HashMap<String, String>();
    private List<userInterface> onlineUsers;

    public ChatroomImpl() throws RemoteException {
        onlineUsers = new ArrayList<userInterface>();
        try {
            users = copyUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //super();
    }

    public List<userInterface> registerClient(userInterface client){
        try {
            if (connect(client.getUsername(), client.getPassword())) {
                this.onlineUsers.add(client);
                //new userInterface().displa
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return this.onlineUsers;
    }

    public boolean connect(String username, String password) throws RemoteException {
        String value;
        for (String key : users.keySet())
        {
            if (key.equals(username) && users.get(key).equals(password)){
                System.out.println("Valid client");
                return true;
            }
        }
        return false;
    }

    public void sendMessage(String username, String message) throws RemoteException {
        String messageEntier = username+" >>>> "+message;
        System.out.println(getOnlineUsers().size());
        for (userInterface user: getOnlineUsers()){
            user.displayMessage(messageEntier);
        }
    }

    public synchronized List<userInterface> getOnlineUsers() throws RemoteException {
        return this.onlineUsers;
    }


    public userInterface searchOnlineUser(String username) throws RemoteException {
        for (userInterface usr : this.getOnlineUsers()){
            if (username == usr.getUsername())
                return usr;
        }
        return null;
    }

    public boolean disconnect(String username) {
        System.out.println("Preparing to disconnection");
        try {
            if (getUserindex(username)!= -1) {
                onlineUsers.remove(getUserindex(username));
                System.out.println("user disconnected");
            }
            else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public HashMap<String, String> copyUsers() throws IOException {
        String filePath = "users.txt";
        HashMap<String, String> usersF = new HashMap<String, String>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        while ((line = reader.readLine()) != null)
        {
            String[] parts = line.split(":", 2);
            if (parts.length >= 2)
            {
                String login = parts[0];
                String password = parts[1];
                usersF.put(login, password);
            } else {
                System.out.println("ignoring line: " + line);
            }
        }
        reader.close();
        return usersF;
    }

    public int getUserindex(String username){
        for (int i = 0; i < onlineUsers.size() ; i++){
            try {
                if ((onlineUsers.get(i).getUsername()).equals(username)){
                    System.out.println("user found on position :"+i);
                    return i;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void printAll(){
        for (String name: users.keySet()){
            String key = name.toString();
            String value = users.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    class notConnectedException extends Exception {
        public notConnectedException(String errorMessage) {
            super(errorMessage);
        }
    }
}

