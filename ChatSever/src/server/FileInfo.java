package server;


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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDestinationDirectory() {
        return destinationDirectory;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getFilename() {
        return filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getPiecesOfFile() {
        return piecesOfFile;
    }

    public int getLastByteLength() {
        return lastByteLength;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public String getStatus() {
        return status;
    }

    public void setDestinationDirectory(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setPiecesOfFile(int piecesOfFile) {
        this.piecesOfFile = piecesOfFile;
    }

    public void setLastByteLength(int lastByteLength) {
        this.lastByteLength = lastByteLength;
    }

    public void setDataBytes(byte[] dataBytes) {
        this.dataBytes = dataBytes;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   
}