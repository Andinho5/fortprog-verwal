package org.example;

import org.example.user.UserService;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        System.out.println(userService.findAll());
    }
}