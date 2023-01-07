package org.example.domain;

import java.sql.Timestamp;

public class MetaDataModel {
    
    private final String id;
    private final String fileName;
    private final String dirPath;
    private final Timestamp lastEditedTime;
    private final String status;
    
    public MetaDataModel(String id, String fileName, String dirPath, Timestamp lastEditedTime, String status) {
        this.id = id;
        this.fileName = fileName;
        this.dirPath = dirPath;
        this.lastEditedTime = lastEditedTime;
        this.status = status;
    }
    
    public String getId() {
        return id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getDirPath() {
        return dirPath;
    }
    
    public Timestamp getLastEditedTime() {
        return lastEditedTime;
    }
    
    public String getStatus() {
        return status;
    }
    
}
