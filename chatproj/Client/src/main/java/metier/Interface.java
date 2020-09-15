package metier;

import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;
import rmi.chatInterface;
import rmi.userInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Interface extends JFrame implements Serializable {
    private JPanel mainPanel;
    private JButton connectButton;
    private  JTextField loginTextField;
    private  JPasswordField passwordField;
    private JList onlineUsersList;
    private JTextField messageTextField;
    private JButton sendButton;
    private JList messageList;
    private JLabel timeLabel;
    private JTextArea textArea1;
    private static UserImpl client;
    static chatInterface stub;
    private DefaultListModel dlmB;
    private DefaultListModel onlineUserModel;

    public Interface(final String title){
        super(title);
        try{
            stub = (chatInterface) Naming.lookup("rmi://localhost:1099/CHR");
            client = new UserImpl();
            client.setGui(this);
            client.setServer(stub);
        }catch (Exception e){
            e.printStackTrace();
        }
        dlmB = new DefaultListModel();
        onlineUserModel = new DefaultListModel();
        onlineUsersList.setModel(onlineUserModel);
        messageList.setModel(dlmB);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        this.setContentPane(mainPanel);
        this.pack();
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<userInterface> tmp = new ArrayList<userInterface>() ;
                try{
                    tmp = stub.getOnlineUsers();
                }catch(RemoteException ex){
                    ex.printStackTrace();
                }

                if(connectButton.getText().equals("Disconnect")){
                    connectButton.setText("connect");
                    try {
                        stub.disconnect(client.getUsername());
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    new Thread(client).start();
                    connectButton.setText("Disconnect");
                }
                loginTextField.setEnabled(false);
                passwordField.setEnabled(false);
            }
        });
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date startDate = new Date();
                /*if(timeLabel.getText().equals("")){
                    timeLabel.setText(""+startDate.getTime());
                }else{
                    long l = title.getTime();
                    if (delayed(timeLabel.getText())){

                    }
                }*/


                Thread background = new Thread(new Runnable() {
                    public void run() {
                        for (int i = 0; i <= 30; i++) {
                            try {
                                Thread.sleep(100);
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                            timeLabel.setText(Integer.toString(i));
                        }
                        try {
                            stub.disconnect(client.getUsername());
                            onlineUserModel.removeAllElements();
                            infoBox("You didn't send any message in the last 30 seconds","User disconnected");
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                background.start();

                try {
                    stub.sendMessage(client.getUsername(),messageTextField.getText());
                    messageTextField.setText("");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public JTextField getLoginTextField() {
        return loginTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        List<userInterface> onlineusers = new ArrayList<userInterface>();
        final JFrame frame = new Interface("My GUI Interface");
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    try {
                        stub.disconnect(client.getUsername());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
    }


    public synchronized void dislayOnlineUsers(List<userInterface> users){
        try {
            onlineUserModel.clear();
            for (userInterface usr : users) {
                onlineUserModel.addElement(usr.getUsername());
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public void displayMessage(String message){
        if (message != null)
            dlmB.addElement(message);
    }

    private boolean delayed(Date time){
        Date now = new Date();
        if((now.getTime() - time.getTime() / 1000) >= 30)
            return true;
        return false;
    }
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public JList getOnlineUsersList() {
        return onlineUsersList;
    }

    public void setOnlineUsersList(JList onlineUsersList) {
        this.onlineUsersList = onlineUsersList;
    }
}
