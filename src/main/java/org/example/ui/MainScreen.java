package org.example.ui;

import org.example.Main;
import org.example.user.User;
import org.example.user.UserService;
import org.example.util.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainScreen implements Screen {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int width;
    String username;
    String password;
    String userid;
    User user;

    private UserService userService;


    public MainScreen(int width) {
        this.width = width;
        this.userService = new UserService();
    }

    @Override
    public void onOpen() throws IOException {
        System.out.println("Willkommen im Hauptmenü vom BalanceBuddy!");
        System.out.println("Melde dich an!\n");
        login();
    }

    @Override
    public void takeInput(String string) {

    }

    public void login() throws IOException {
        System.out.print("Benutzername: ");
        username = reader.readLine();
        System.out.print("Passwort: ");
        password = reader.readLine();

        if(userService.validateUser(username, password)){//insert DB-Query here with userid
            user = new User(userid, username, password, 1000); //switch with correct user info from database
            System.out.println("Hallo "+username+" !");
            chooseApplication();
        } else {
            System.out.println(Color.RED+"Fehler beim Login!"+ Color.RESET);

        }
    }

    public void chooseApplication() throws IOException {
        System.out.println("\nMöchtest du die Banking-App oder dein Postfach aufrufen?");
        String choice = reader.readLine();
        if(choice.contains("ank")){
            Main.setScreen(new BankApplication(user));
        } else if(choice.contains("ost")){
            Main.setScreen(new MailApplication(user));
        } else {
            System.out.println("Eingabefehler, bitte versuche es erneut!");
            chooseApplication();
        }
    }

    public void centerText(String text) {
        int padding = (width - text.length()) / 2;
        String format = "%" + (padding + text.length()) + "s";
        System.out.println(String.format(format, text).concat(" ".repeat(width - padding - text.length())));
    }
}
