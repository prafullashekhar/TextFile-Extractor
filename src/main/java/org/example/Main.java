package org.example;

import org.example.data.DataRepositoryImpl;
import org.example.data.Database;
import org.example.data.FileCopying;
import org.example.domain.DataRepository;
import org.example.domain.MetaDataModel;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    static Scanner scanner;
    
    public static void main(String[] args) throws IOException {
        
        scanner = new Scanner(System.in);
        DataRepository dataRepository = new DataRepositoryImpl(new Database(), new FileCopying());

        /*
        String sourceDir = "D:\\My Downloads\\checking";
        String targetDir = "D:\\My Downloads\\taget_check";
         */

//        while (true) {
        String sourceDir = takeInput("Enter the Source Directory Path");
        String targetDir = takeInput("Enter the Target Directory Path");
        try {
            dataRepository.createTransaction(sourceDir, targetDir);
        } catch (java.nio.file.NoSuchFileException ex) {
            System.out.println("This path is not a dir path");
            System.out.println(ex.getCause());
        }
//        }
        
        List<MetaDataModel> lst = dataRepository.getAllTransactions();
        System.out.println("Total number of transactions till now = " + lst.size());
        
    }
    
    public static String takeInput(String ask) {
        System.out.println(ask);
        return scanner.nextLine();
    }
}


