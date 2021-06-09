
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextField;

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
    static private ObjectOutputStream objectOutputStream  = null;
    static private String MESSAGE_SERVICE = "MESSAGE_SERVICE"; 
    
    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
        System.out.println("herer");

    }

    public void run() {
        //String userName = console.readLine("\nEnter your name: ");
        String userName = client.chatClientFrame.getNameUser().getText();
        
        try {
            //
            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            client.setUserName(userName);
            
            objectOutputStream.writeObject(userName);
            
            
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
                    
                    client.chatClientFrame.getMessageBoard()
                            .setText(client.chatClientFrame.getMessageBoard()
                                    .getText() + "[me]:" + msg + "\n");
                    
                    //SEND MESSAGE
                    msg = MESSAGE_SERVICE + "-" + msg;
                    objectOutputStream.writeObject(msg);
                    
                    
                } catch (Exception e) {
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
                    objectOutputStream.writeObject(msg);
                } catch (IOException ex) {
                    Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                client.chatClientFrame.getMessageBoard()
                        .setText(client.chatClientFrame.getMessageBoard()
                                .getText() + "[me]:" + msg + "\n");
                
                JTextField txtMessage =  client.chatClientFrame.gettxtMessage();
                txtMessage.setText("");
            }
        }
 
        public void keyReleased(KeyEvent e) {
        }
    }
}
