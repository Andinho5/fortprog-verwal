package org.example.ui;

import org.example.Main;
import org.example.transaction.Transaction;
import org.example.transaction.TransactionService;
import org.example.user.User;
import org.example.user.UserService;
import org.example.util.ReceiverNotFoundException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class BankApplication extends Application {
    private User user;
    private UserService userService;
    private TransactionService transactionService;

    public BankApplication(User user) {
        super(user);
        this.user = user;
        userService = new UserService();
        transactionService = new TransactionService(userService);
    }

    private Optional<User> getUser() throws IOException {
        System.out.print("An wen moechtest du ueberweisen? ");
        String ueberweisung = reader.readLine();
        Optional<User> optionalUser = userService.findByAttribute("name", ueberweisung);
        if (optionalUser.isPresent()) {
            return optionalUser;
        }
        else {
            System.err.println("Kein User gefunden mit Namen " + ueberweisung + ".");
            System.out.print("Moechtest du neustarten? (j/n) ");
            String neustart = reader.readLine();
            if (neustart.equalsIgnoreCase("j")) {
                return getUser();
            }
            else {
                return Optional.empty();
            }
        }
    }

    private double getAmount() throws IOException {
        System.out.print("Wie viel moechtest du ueberweisen? ");
        try {
            String menge = reader.readLine();
            double amount = Double.parseDouble(menge);
            if (amount > user.getGehalt()) {
                System.err.println("Ungueltiger Betrag! Neustart.");
                return getAmount();
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("awdahdd");
            return getAmount();
        }
    }

    public void ueberweisen() throws IOException {
        Optional<User> empfaenger = getUser();
        empfaenger.ifPresentOrElse(user -> System.out.println("Es wurde ein User " + user.getUsermail() +
                " gefunden mit der ID " + user.getUserid()), () -> {
            try {
                onOpen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        double amount = getAmount();
        System.out.print("Moechtest du eine Nachricht anfuegen? (j/n) ");
        String antwort = reader.readLine();
        if (antwort.equals("n")) {
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), user, empfaenger.get(),
                    amount, Timestamp.valueOf(LocalDateTime.now()), null);
            transactionService.save(transaction);
            userService.processTransaction(transaction);
            System.out.println("Ueberweisung wird getaetigt");
        }
        else if (antwort.equals("j")) {
            String nachricht = reader.readLine();
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), user, empfaenger.get(),
                    amount, Timestamp.valueOf(LocalDateTime.now()), nachricht);
            transactionService.save(transaction);
            userService.processTransaction(transaction);
            System.out.println("Ueberweisung wird getaetigt");
        }
        else {
            System.err.println("Bitte Ja / Nein eingeben. Keine Nachricht wird erstellt.");
        }
        onOpen();
    }

    @Override
    public void onOpen() throws IOException {
        System.out.println("\nWillkommen im Banking-Menue!");
        System.out.println("""
                Welche Taetigkeit brauchst du?
                Guthaben einsehen (0)
                Kontoauszuege (1)
                Einzelueberweisung (2)
                Massenueberweisung (3)
                Kontoauszuege in CSV exportieren (4)
                Zurueck ins Hauptmenue (5)
                """);
        System.out.print("Eingabe: ");
        String input = reader.readLine();
        if (input.equals("0")) {
            System.out.println("Aktuelles Guthaben: " + user.getGehalt());
            onOpen();
        }
        else if (input.equals("2")) {
            try {
                ueberweisen();
            }
            catch (IOException e) {
                System.err.println("Input-Fehler!");
                ueberweisen();
            }
        }
        else if (input.equals("3")) {

        }
        else if (input.equals("4")) {

        }
        else if (input.equals("5")) {
            Main.setScreen(new MainScreen(10));
        }
        else {

        }
    }

    @Override
    public void takeInput(String string) {

    }


    @Override
    public void logout() {

    }

}
