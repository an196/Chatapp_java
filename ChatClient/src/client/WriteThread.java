package client;


import lib.TranferFile;
import lib.FileInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.AncestorListener;
import lib.Package;
import lib.FileInfo;
import lib.Helper;
import lib.Services;
import lib.UserInfo;

/**
 * This thread is responsible for reading user's input and send it to the
 * server. It runs in an infinite loop until the user types 'bye' to quit.
 *
 * @author www.codejava.net
 */
public class WriteThread extends Thread {

    static private PrintWriter writer;
    static private Socket socket;
    static private ChatClient client;
    static private String msg = "";
    static private ObjectOutputStream oos  = null;
    private Services SERVICE =  new Services(); 
    private Helper helper = new Helper();
    
    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
        
    }

    public void run() {
        
        TriggerConnected();
        try {
            //
            OutputStream outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            
            
        } catch (IOException ex) {
            Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        JButton sendButton = client.chatClientFrame.getSendButton();
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
              
                msg = client.chatClientFrame.getMessage();
                if (msg.equals("")) {
                    return;
                }
                try {
                    System.out.println(msg);
                    
                    msg = "[me]:" + msg;
                    helper.UpdateChatPane(getMessageBoard(), msg);
                    //SEND MESSAGE
                    
                    Package pk = new Package(SERVICE.MESSAGE_SERVICE,msg);
                    oos.writeObject(pk);
                    
                    
                } catch (Exception e) {
                    System.out.println("Error while sendding messeger");
                    msg = "Error while sendding messeger ";
                    helper.UpdateChatPane(getMessageBoard(), msg);
                }
            }
        });

        JButton signupButton = client.chatClientFrame.getSignupButton();
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String userName = client.chatClientFrame.getUserName();
                String password = client.chatClientFrame.getPassword();
                
               SignUp(userName,password);
            }
        });
        
        JButton signinButton = client.chatClientFrame.getSigninButton();
        signinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String userName = client.chatClientFrame.getUserName();
                String password = client.chatClientFrame.getPassword();
                
               SignIn(userName,password);
            }
        });
        
        
        JButton sendFileButton = client.chatClientFrame.getSendFileButton();
        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    
                    System.out.println("{[me]: has sent a file " + client.chatClientFrame.getSourceName());
                    msg = "[me]: has sent a file: " + client.chatClientFrame.getSourceName();
                    helper.UpdateChatPane(getMessageBoard(), msg);
                     
                    msg = client.chatClientFrame.getMessage();
                    
                   
                    String sourcePath = client.chatClientFrame.getSourcePath();
                    String destination = "D:\\server\\";
                    

                    TranferFile tranferfile = new TranferFile();
                    FileInfo fileInfo = tranferfile.getFileInfo(sourcePath, destination);
                    
                    Package pk = new Package(SERVICE.FILE_SERVICE, fileInfo);
                    oos.writeObject(pk);
                    
                } catch (IOException ex) {
                    Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error while sendding messeger");
                } 
                
            }
        });
        JTextField txtMessage =  client.chatClientFrame.gettxtMessage();
        txtMessage.addKeyListener(new CustomKeyListener());
        

    }
    
    class CustomKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) {
        }
 
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                msg = client.chatClientFrame.getMessage();
                if (msg.equals("")) {
                    return;
                }
                
                System.out.println(msg);
                try {
                    Package pk = new Package(SERVICE.MESSAGE_SERVICE,msg);
                    oos.writeObject(pk);
                } catch (IOException ex) {
                    Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                msg = "[me]:" + msg;
                helper.UpdateChatPane(getMessageBoard(), msg);
                JTextField txtMessage =  client.chatClientFrame.gettxtMessage();
                txtMessage.setText("");
            }
        }
 
        public void keyReleased(KeyEvent e) {
        }
    }
    
    private void SignUp(String username, String pass){
        try {
            System.out.println("Send a sign up requet");
            UserInfo user = new UserInfo(username, pass);
            Package pk = new Package(SERVICE.SIGNUP_SERVICE, user);
            oos.writeObject(pk);
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
    } 
    
    private void SignIn(String username, String pass){
        try {
            System.out.println("Send a sign in requet");
            UserInfo user = new UserInfo(username, pass);
            Package pk = new Package(SERVICE.SIGNIN_SERVICE, user);
            oos.writeObject(pk);
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
    } 
    
    private void TriggerConnected(){
        client.chatClientFrame.gettxtHostName().setEnabled(false);
        client.chatClientFrame.gettxtPort().setEnabled(false);
        //client.chatClientFrame.getConnectButton().setEnabled(false);
        
        File file = new File("D:\\client");
        boolean dirCreated = file.mkdir();
    }   
    
    public void Disconnect(){
        msg = "Disconnected";
        helper.UpdateChatPane(getMessageBoard(), msg);
         Package pk = new Package(SERVICE.DISCONNECT_SERVICE, "BYE");
        try {
            oos.writeObject(pk);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    JTextPane getMessageBoard(){
        return client.chatClientFrame.getMessageBoard();
    }
}
