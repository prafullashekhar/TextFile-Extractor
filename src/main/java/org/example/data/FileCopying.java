package org.example.data;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
    This class contains all the logic of copying
    one text file from one directory to another,
    creating new directory and all.
 */
public class FileCopying {
    
    public void createTextFile(Path sourceFilePath, Path targetFilePath) {
        
        // creating the parent directories if not created
        try {
            Files.createDirectories(targetFilePath.getParent());
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
            throw new RuntimeException(ex);
        }
        
        String targetPath = targetFilePath.toAbsolutePath().toString();
        
        /*
            Test file is already existing then append _n in the name of text file
            like if hello.txt exists then put target file to path hello_n.txt
         */
        while (Files.exists(targetFilePath)) {
            targetPath = targetPath.substring(0, targetPath.length() - 4) + "_n.txt";
            targetFilePath = Paths.get(targetPath);
        }
        txtCopy(sourceFilePath, targetFilePath);
    }
    
    
    /*
    This function copy one text file from source path to target path
    This is done with file stream native method
    which I found to be the fastest
     */
    private void txtCopy(Path sourceFilePath, Path targetFilePath) {
        try (InputStream inputStream = Files.newInputStream(sourceFilePath); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        
             OutputStream outputStream = Files.newOutputStream(targetFilePath); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            byte[] buffer = new byte[4096];
            int numBytes;
            while ((numBytes = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, numBytes);
            }
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
            throw new RuntimeException(ex);
        }
    }
    
    
}
