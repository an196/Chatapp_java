/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;
import lib.FileInfo;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author an9x0
 */
public class TranferFile{
    private Socket client;
    private String sourceFilePath = "empty";
    private String destinationDir = "D:\\server\\";
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    
    
    public TranferFile() {
        
    }
    
    
    
     public void sendFile(String sourceFilePath, String destinationDir) {
        
         
        try {
            
            ois = new ObjectInputStream(client.getInputStream());
            // get file info
            FileInfo fileInfo = getFileInfo(sourceFilePath, destinationDir);
            oos.writeObject(fileInfo);
            fileInfo = (FileInfo) ois.readObject();
            if (fileInfo != null) {
                System.out.println(fileInfo.getStatus());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // close all stream
            closeStream(oos);
            closeStream(ois);
        }
    }
      
    /**
     * get source file info
     * 
     * @author viettuts.vn
     * @param sourceFilePath
     * @param destinationDir
     * @return FileInfo
     */
    public FileInfo getFileInfo(String sourceFilePath, String destinationDir) {
        FileInfo fileInfo = null;
        BufferedInputStream bis = null;
        try {
            File sourceFile = new File(sourceFilePath);
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            fileInfo = new FileInfo();
            byte[] fileBytes = new byte[(int) sourceFile.length()];
            // get file info
            bis.read(fileBytes, 0, fileBytes.length);
            fileInfo.setFilename(sourceFile.getName());
            fileInfo.setDataBytes(fileBytes);
            fileInfo.setDestinationDirectory(destinationDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeStream(bis);
        }
        return fileInfo;
    }
      
    /**
     * close socket
     * 
     * @author viettuts.vn
     */
    public void closeSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * close input stream
     * 
     * @author viettuts.vn
     */
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
