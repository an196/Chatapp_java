package server;


import lib.RecieveFile;
import lib.Package;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import lib.FileInfo;
import lib.Helper;
import lib.UserInfo;
import lib.Services;
import lib.TranferFile;

/**
 *
 * @author an9x0
 */
public class UserThread extends Thread {

    private Socket socket = null;
    private ChatServer server;
    private PrintWriter writer;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Services service = new Services();
    private Helper helper = new Helper();
    private String msg = "empty";
    private String userName = "username";

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {

        try {

            // get the input stream from the connected socket
            InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it.
            ois = new ObjectInputStream(inputStream);

            // get the output stream from the socket.
            OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            oos = new ObjectOutputStream(outputStream);

            Package pk;
            UserInfo user = null;
            //Sign up
            String clientMessage,serverMessage = "empty";
            boolean isDisconnect = false;
            do {
                try {
                    pk = (Package) ois.readObject();
                    if (pk.getiService().equals(service.SIGNUP_SERVICE)) {
                        user = (UserInfo) pk.getiContent();
                        System.out.println("sign up name: " + user.getUserName());
                        if (server.Signup(user)) {
                            msg = "sign up name: [" + user.getUserName() + "] successful";
                            System.out.println(msg);

                            helper.UpdateChatPane(getMessageBoard(), msg);

                            userName = user.getUserName();
                            pk = new Package(service.SIGNUP_SERVICE, "true");
                            oos.writeObject(pk);
                           
                        } else {
                            pk = new Package(service.SIGNUP_SERVICE, "false");
                            oos.writeObject(pk);
                        }

                    } else if (pk.getiService().equals(service.SIGNIN_SERVICE)) {
                        user = (UserInfo) pk.getiContent();
                        System.out.println("sign in name: " + user.getUserName());

                        if (server.Signin(user)) {
                            userName = user.getUserName();
                            msg = "sign in name: " + user.getUserName() + "successful";
                            System.out.println();
                            helper.UpdateChatPane(getMessageBoard(), msg);
                            pk = new Package(service.SIGNIN_SERVICE, "true");
                            oos.writeObject(pk);
                            
                             printUsers();
                            server.addUser(user);

                            serverMessage = "New user connected: " + userName;
                            msg = "New user connected: " + userName;
                            helper.UpdateChatPane(getMessageBoard(), msg);
                            pk = new Package(service.MESSAGE_SERVICE, serverMessage);
                            server.broadcast(pk, this);
                            

                        } else {
                            pk = new Package(service.SIGNIN_SERVICE, "false");
                            oos.writeObject(pk);
                        }

                    } else if (pk != null && pk.getiService().equals(service.MESSAGE_SERVICE)) {

                        clientMessage = (String) pk.getiContent();
                        serverMessage = "[" + userName + "]: " + clientMessage;
                        System.out.println(serverMessage);
                        pk = new Package(service.MESSAGE_SERVICE, serverMessage);
                        server.broadcast(pk, this);

                    } else if (pk != null && pk.getiService().equals(service.FILE_SERVICE)) {

                        FileInfo fileInfo = (FileInfo) pk.getiContent();
                        if (fileInfo != null) {
                            RecieveFile rf = new RecieveFile();
                            rf.createFile(fileInfo);

                            msg = "[" + userName + "]: has sent a file " + fileInfo.getFilename() ;
                            helper.UpdateChatPane(getMessageBoard(), msg);

                            String sourcePath = "D:\\server\\" + fileInfo.getFilename();
                            String destination = "D:\\client\\";

                            TranferFile tranferfile = new TranferFile();
                            fileInfo = tranferfile.getFileInfo(sourcePath, destination);

                            pk = new Package(service.FILE_SERVICE, userName, fileInfo);
                            server.broadcast(pk, this);
                        }
                    } else if (pk != null && pk.getiService().equals(service.DISCONNECT_SERVICE)) {
                        clientMessage = (String) pk.getiContent();
                        serverMessage = "[" + userName + "]: has quitted";
                        helper.UpdateChatPane(getMessageBoard(), serverMessage);
                        pk = new Package(service.MESSAGE_SERVICE, serverMessage);
                        break;

                    }

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
                }

            } while (!isDisconnect);
            server.removeUser(userName, this);
            socket.close();
            serverMessage = userName + " has quitted.";
            pk = new Package(service.MESSAGE_SERVICE, serverMessage);
            server.broadcast(pk, this);

        } catch (IOException ex) {
            //System.out.println("Error in UserThread: " + ex.getMessage());

            msg = "[" + userName + "]: has disconnected";
            helper.UpdateChatPane(getMessageBoard(), msg);
            //ex.printStackTrace();
        } finally {
            // close all stream
            closeStream(ois);
            closeStream(oos);
        }
    }

    JTextPane getMessageBoard() {
        return server.chatServerFrame.getMessageBoard();
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            String messages = "Connected users: " + server.getUserNames();
            try {
                Package pk = new Package(service.MESSAGE_SERVICE, messages);
                oos.writeObject(pk);
            } catch (IOException ex) {
                Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                String msg = "No other users connected";
                Package pk = new Package(service.MESSAGE_SERVICE, msg);
                oos.writeObject(pk);
            } catch (IOException ex) {
                Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendPackage(Package pk) throws IOException {
        oos.writeObject(pk);
    }

    public void closeStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * close output stream
     *
     * @author viettuts.vn
     */
    public void closeStream(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
