package org.example.ui;

import org.example.Main;
import org.example.user.User;
import org.example.user.UserService;
import org.example.util.Color;
import org.example.util.MailInvalidException;
import org.example.util.UserNameAlreadyUsedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class MainScreen implements Screen {
    protected BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private int width;
    private User user;

    private UserService userService;


    public MainScreen(int width) {
        this.width = width;
        this.userService = new UserService();
    }

    public MainScreen(int width, User user) throws IOException {
        this.width = width;
        this.user = user;
        this.userService = new UserService();
        chooseApplication();
    }

    @Override
    public void onOpen() throws IOException {
        System.out.println("Willkommen im Hauptmenue vom BalanceBuddy!");
        System.out.print("Hast du bereits ein Konto? (j/n/q)\t");
        String antwort = reader.readLine();

        if (antwort.equals("j")) {
            login();
        }
        else if (antwort.equals("q")) {
            System.exit(0);
        }
        else {
            register();
        }
    }

    @Override
    public void takeInput(String string) {

    }

    public void login() throws IOException {
        System.out.print("Mailadresse: ");
        String usermail = reader.readLine();
        System.out.print("Passwort: ");
        String password = reader.readLine();

        if (userService.validateUser(usermail, password)) { //insert DB-Query here with userid
            userService.findByAttribute("name", usermail).ifPresent(user1 -> user = user1); //switch with correct user info from database
            System.out.println("Hallo " + usermail + " !");
            chooseApplication();
        }
        else {
            System.out.println(Color.RED + "Fehler beim Login!" + Color.RESET);
        }
    }

    private void register() throws IOException {
        System.out.print("Wie lautet deine Mailadresse? ");
        String usermail = reader.readLine();
        System.out.print("Wie lautet dein Passwort? ");
        String password = reader.readLine();

        try {
            usermail = usermail.trim();
            if (userService.mailMatches(usermail)) {
                userService.save(new User(UUID.randomUUID().toString(), usermail, password, 1000.0d));
                System.out.println("\nMelde dich nun an!");
                login();
            }
            else {
                //TODO zurückleiten zu register damit programm nicht zuende
                throw new MailInvalidException("Mail entspricht nicht Richtlinien!");
            }
        } catch (UserNameAlreadyUsedException e) {
            System.err.println("Fehler beim Registrieren des Users " + usermail + ", dieser wird schon benutzt");
            System.out.print("Moechtest du von vorne starten? (j/n) ");
            String antwort = reader.readLine();
            if (antwort.equals("j")) {
                register();
            }
            else {
                onOpen();
            }
        } catch (MailInvalidException e) {
            System.err.println(e.getMessage());
        }
    }

    public void chooseApplication() throws IOException {
        System.out.println("\nMoechtest du die Banking-App (ank) oder dein Postfach (ost) aufrufen?");
        String choice = reader.readLine();
        if (choice.contains("ank")) {
            Main.setScreen(new BankApplication(user));
        } else if (choice.contains("ost")) {
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
