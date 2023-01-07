package org.example;

import org.example.data.DataRepositoryImpl;
import org.example.domain.DataRepository;
import org.example.domain.MetaDataModel;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Main {
    
    static Scanner scanner;
    static DataRepository dataRepository;
    
    public static void main(String[] args) {
        
        scanner = new Scanner(System.in);
        dataRepository = new DataRepositoryImpl(Executors.newFixedThreadPool(100));

        /*
        String sourceDir = "D:\\My Downloads\\checking";
        String targetDir = "D:\\My Downloads\\taget_check";
         */
        
        while (true) {
            char task = takeInput("press a for Making Transaction " +
                    "\npress b for getting list of all Transactions " +
                    "\npress c for getting total Transactions").charAt(0);
            switch (task) {
                case 'a':
                    makeTransaction();
                case 'b':
                    getAllTransaction();
                case 'c':
                    getTotalTransactionCount();
            }
        }
        
        
    }
    
    private static List<MetaDataModel> getAllTransaction() {
        List<MetaDataModel> lst = dataRepository.getAllTransactions();
        return lst;
    }
    
    private static void getTotalTransactionCount() {
        List<MetaDataModel> lst = dataRepository.getAllTransactions();
        System.out.println("Total number of transactions till now = " + lst.size());
    }
    
    private static void makeTransaction() {
        String sourceDir = takeInput("Enter the Source Directory Path");
        String targetDir = takeInput("Enter the Target Directory Path");
        try {
            dataRepository.createTransaction(sourceDir, targetDir);
        } catch (Exception ex) {
            System.out.println("This path is not a dir path");
        }
    }
    
    private static String takeInput(String ask) {
        System.out.println(ask);
        return scanner.nextLine();
    }
}


