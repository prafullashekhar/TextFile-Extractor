package org.example;

import org.example.data.DataRepositoryImpl;
import org.example.data.Database;
import org.example.domain.DataRepository;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        DataRepository dataRepository = new DataRepositoryImpl(new Database());

        Scanner sc = new Scanner(System.in);
//        while(true){
            String s = sc.next();
            dataRepository.createTransaction(s);
//        }

//        dataRepository.createTransaction("D:\\My Downloads\\checking\\fafasfa");

    }
}


