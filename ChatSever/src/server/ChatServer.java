package server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
import java.util.*;
import lib.Helper;
import lib.Package;
import lib.UserInfo;
/**
 *
 * @author an9x0
 */
public class ChatServer {
    private int port;
    private Socket user = null;
    private Set<UserInfo> userList= new HashSet<>();
    private Set<UserInfo> userData= new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();
    protected ChatServerFrame chatServerFrame = null;
    private Helper helper = new Helper();
    
    
    public ChatServer(int port, ChatServerFrame chatServerFrame ) {
        this.port = port;
        this.chatServerFrame = chatServerFrame;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            
            File file = new File("D:\\server");
            boolean dirCreated = file.mkdir();
            
            while (true) {
                Socket socket = serverSocket.accept();
                
                System.out.println("New user connected");
                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
                
            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            String msg =  "Error in the server: " +  ex.getMessage()+ "\n";
            helper.UpdateChatPane(chatServerFrame.getMessageBoard(), msg);
            ex.printStackTrace();
        }
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(Package pk, UserThread excludeUser) throws IOException {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendPackage(pk);
            }
        }
    }
  
    void addUser(UserInfo user) {
        userList.add(user);
    }

    
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userList.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
            String msg =  "The user " + userName + " quitted \n";
            helper.UpdateChatPane(chatServerFrame.getMessageBoard(), msg);
        }
    }

    Set<String> getUserNames() {
        Set<String> list = new HashSet<String>();
        for(UserInfo aUser: userList){
            list.add(aUser.getUserName());
        }    
        return list;
    }

    int getPort(){
        return port;
    } 
    boolean hasUsers() {
        return !this.userList.isEmpty();
    }
   
    boolean Signup( UserInfo user) {
        for (UserInfo aUser : userList) {
            if (aUser.getUserName().equals(user.getUserName()) ) {
                return false;
            }
        }
        userData.add(user);
        return true;
    }
    
    boolean Signin(UserInfo user) {
        for (UserInfo aUser : userData) {
            if( aUser.Equal(user))
                return true;
        }
       
        return false;
    }
}
