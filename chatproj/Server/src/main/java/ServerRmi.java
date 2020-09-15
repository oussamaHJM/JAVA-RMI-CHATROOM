import metier.ChatroomImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerRmi {
    public static void main(String[] args) {
        try {
            ChatroomImpl serv = new ChatroomImpl();
            LocateRegistry.createRegistry(1099);
            ChatroomImpl chatroom = new ChatroomImpl();
            Naming.rebind("rmi://localhost:1099/CHR", chatroom);
            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
