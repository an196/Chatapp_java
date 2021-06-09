package client;

import lib.Services;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import lib.Package;
import lib.UserInfo;
/**
 * This is the chat client program.
 * Type 'bye' to terminte the program.
 *
 * @author www.codejava.net
 */
public class ChatClient {
	private String hostname;
	private int port;
	private String userName;
        protected ChatClientFrame chatClientFrame = null;
        private Socket socket = null;    
        private ReadThread readThread = null; 
        private WriteThread writeThread = null; 
        
	public ChatClient(String hostname, int port, ChatClientFrame chatClientFrame) {
		this.hostname = hostname;
		this.port = port;
                this.chatClientFrame = chatClientFrame;
	}

	public void execute() {
		try {
			socket = new Socket(hostname, port);

			System.out.println("Connected to the chat server");
                        
                        chatClientFrame.getMessageBoard().setText(chatClientFrame.getMessageBoard().getText() +  "Connected to the chat server \n" );
                        chatClientFrame.getConnectButton().setText("Disconnect" );
                        
                        readThread = new ReadThread(socket, this);
                        readThread.start();
                        writeThread = new WriteThread(socket, this);
                        writeThread.start();

                          
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
                         chatClientFrame.getMessageBoard().setText(chatClientFrame.getMessageBoard().getText() +  "Server not found: \n" );
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
                         chatClientFrame.getMessageBoard().setText(chatClientFrame.getMessageBoard().getText() +  "I/O Error: \n" );
		}

	}

	void setUserName(String userName) {
		this.userName = userName;
	}

	String getUserName() {
		return this.userName;
	}
        
        public void readThread(){
            new ReadThread(socket, this).start();
        }
        
        public void writeThread(){
            new WriteThread(socket, this).start();
        }
        
        public Socket getSocket(){
            return socket;
        }
      
        
        public void closeSocket(){
            try {
                writeThread.Disconnect();
                readThread.Disconnect();
                writeThread.stop();
                readThread.stop();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}