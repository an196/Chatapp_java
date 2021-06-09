import java.net.*;
import java.io.*;


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
        private  Socket socket = null;    
        
	public ChatClient(String hostname, int port,ChatClientFrame chatClientFrame) {
		this.hostname = hostname;
		this.port = port;
                this.chatClientFrame = chatClientFrame;
	}

	public void execute() {
		try {
			socket = new Socket(hostname, port);

			System.out.println("Connected to the chat server");
                        chatClientFrame.getMessageBoard().setText(chatClientFrame.getMessageBoard().getText() +  "Connected to the chat server \n" );
                        new ReadThread(socket, this).start();
                        new WriteThread(socket, this).start();
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

}