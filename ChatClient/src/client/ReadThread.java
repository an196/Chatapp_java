package client;

import lib.Package;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import lib.FileInfo;
import lib.Helper;
import lib.RecieveFile;
import lib.Services;

/**
 * This thread is responsible for reading server's input and printing it to the
 * console. It runs in an infinite loop until the client disconnects from the
 * server.
 *
 * @author www.codejava.net
 */
public class ReadThread extends Thread {

    private ObjectInputStream ois = null;
    private Socket socket = null;
    private ChatClient client = null;
    private Services SERVICE = new Services();
    private Helper helper = new Helper();
    
    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
    }

    public void run() {
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
             ois = new ObjectInputStream(inputStream);

        } catch (IOException ex) {
            Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        // create a DataInputStream so we can read data from it.
        
        client.setUserName(client.chatClientFrame.getUserName());
        while (true) {
            try {
               
                String response = "error";
                try {
                    Package pk = (Package) ois.readObject();
                    if (pk.getiService().equals(SERVICE.MESSAGE_SERVICE)) {
                        response = (String) pk.getiContent();
                        
                        if (client.getUserName() != null) {
                            String msg = response;
                            helper.UpdateChatPane(getMessageBoard(), msg);
                            
                        }
                    } else if (pk.getiService().equals(SERVICE.SIGNUP_SERVICE)) {
                        
                        response = (String) pk.getiContent();
                        if (response.equals("false")) {
                            String msg = "Sign up false";
                            helper.UpdateChatPane(getMessageBoard(), msg);
                           
                        } else{
                            String msg =  "Sign up successful";
                            helper.UpdateChatPane(getMessageBoard(), msg);
                            
                        }
                    
                    }else if (pk.getiService().equals(SERVICE.SIGNIN_SERVICE)) {
                        
                        response = (String) pk.getiContent();
                        if (response.equals("false")) {
                            String msg =  "Sign in false"  ;
                            helper.UpdateChatPane(getMessageBoard(), msg);
                            
                            System.out.println("Sign in false");
                        } else{
                            String msg =  "Sign in successful" ;
                            helper.UpdateChatPane(getMessageBoard(), msg);
                            
                            System.out.println("Sign in true");
                            allowChat(client.chatClientFrame);
                        }
                    }
                    else if (pk.getiService().equals(SERVICE.FILE_SERVICE)) {
                        FileInfo fileInfo = (FileInfo) pk.getiContent();
                        String userName = (String) pk.getiUserName();
                        if (fileInfo != null) {
                            RecieveFile rf = new RecieveFile();
                            rf.createFile(fileInfo);
                            
                            String msg = "["+ userName + "]: has sent a file " + fileInfo.getFilename();
                            helper.UpdateChatPane(getMessageBoard(), msg);
                          
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("\n" + response);

                // prints the username after displaying the server's message
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
               
                String msg = "Error reading from server:" + ex.getMessage();
                helper.UpdateChatPane(getMessageBoard(), msg);
                ex.printStackTrace();
                break;
            }
        }
    }
    JTextPane getMessageBoard(){
        return client.chatClientFrame.getMessageBoard();
    }
    
     public void Disconnect(){
        try {
            ois.close();
        } catch (IOException ex) {
            Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    void allowChat(JFrame frame){
        client.chatClientFrame.gettxtMessage().setEnabled(true);
        client.chatClientFrame.gettxtFilePath().setEnabled(true);
        client.chatClientFrame.getSendButton().setEnabled(true);
        client.chatClientFrame.getBrowserButton().setEnabled(true);
        client.chatClientFrame.getSendFileButton().setEnabled(true);
        client.chatClientFrame.getUserNameTextField().setEnabled(false);
        client.chatClientFrame.getPasswordTextField().setEnabled(false);
        client.chatClientFrame.getSignupButton().setEnabled(false);
        client.chatClientFrame.getSigninButton().setEnabled(false);
    }
}
