package org.example.domain;

import java.sql.Timestamp;

public class MetaDataModel {

    private String id;
    private String fileName;
    private String dirPath;
    private Timestamp lastEditedTime;
    private String status;

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

    public void setId(String id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public void setLastEditedTime(Timestamp lastEditedTime) {
        this.lastEditedTime = lastEditedTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
