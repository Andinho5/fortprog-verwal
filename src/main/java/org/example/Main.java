package org.example;

import java.io.*;

import org.example.user.User;
import org.example.util.Color;
import org.example.user.UserService;

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
        System.out.println("Willkommen beim BalanceBuddy!");
//        UserService userService = new UserService();
//        System.out.println(userService.findAll());
//
//        userService.save(new User("7wd8a7wd", "pablo@gmail.com", "pablo123", 10000));
//        System.out.println(userService.findAll());
    }
}