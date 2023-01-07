package org.example.data;

import org.example.domain.MetaDataModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;

public class TransactionTaskThread implements Runnable {
    
    private final Database db;
    private final FileCopying fileCopy;
    private final Path sourceFilePath;
    private final Path targetFilePath;
    
    TransactionTaskThread(Path sourceFilePath, Path targetFilePath) {
        db = new Database();
        fileCopy = new FileCopying();
        this.sourceFilePath = sourceFilePath;
        this.targetFilePath = targetFilePath;
    }
    
    @Override
    public void run() {
        if (db.insertRowIntoTable(getMetaDataModelFromFile(sourceFilePath))) {
            fileCopy.createTextFile(sourceFilePath, targetFilePath);
        }
    }
    
    /*
     this function deals with defining the TextField of the table
     */
    private MetaDataModel getMetaDataModelFromFile(Path file) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            return new MetaDataModel(
                    file.toString() + attributes.lastModifiedTime(),
                    file.toFile().getName(),
                    file.toFile().getParent(),
                    new Timestamp(attributes.lastModifiedTime().toMillis()),
                    "READ"
            );
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
            throw new RuntimeException(ex);
        }
    }
}
