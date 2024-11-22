package org.example;

import java.io.*;

import org.example.util.Color;

public class Main {


    public static void main(String[] args) {
        Color.init();
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("Title");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            while(reader.ready()){
                System.out.println(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();


    }
}