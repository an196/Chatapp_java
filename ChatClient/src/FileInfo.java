
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
 
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;
 
    private String destinationDirectory;
    private String sourceDirectory;
    private String filename;
    private long fileSize;
    private int piecesOfFile;
    private int lastByteLength;
    private byte[] dataBytes;
    private String status;
    // Constructor
    // getter & setter
}