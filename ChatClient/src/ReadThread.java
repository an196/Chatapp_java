import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread is responsible for reading server's input and printing it
 * to the console.
 * It runs in an infinite loop until the client disconnects from the server.
 *
 * @author www.codejava.net
 */
public class ReadThread extends Thread {
	private ObjectInputStream objectInputStream = null;
	private Socket socket;
	private ChatClient client;

	public ReadThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
	}

	public void run() {
             InputStream inputStream;
            try {
                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);
            } catch (IOException ex) {
                Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
            }
                        // create a DataInputStream so we can read data from it.
                       
            while (true) {
                try {
                    String response = "error";
                    try {
                       
                        response = (String)objectInputStream.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("\n" + response);

                    // prints the username after displaying the server's message
                    if (client.getUserName() != null) {
                            this.client.chatClientFrame.getMessageBoard()
                                    .setText(this.client.chatClientFrame
                                            .getMessageBoard().getText()+  response+ "\n");
                        }
                } catch (IOException ex) {
                    System.out.println("Error reading from server: " + ex.getMessage());
                    this.client.chatClientFrame.getMessageBoard().setText(this.client.chatClientFrame.getMessageBoard().getText() +
                            "Error reading from server:" + ex.getMessage()+ "\n");
                    ex.printStackTrace();
                    break;
                }
            }
	}
}