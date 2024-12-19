package org.example.ui;

import org.example.Main;
import org.example.data.CsvDatasheet;
import org.example.transaction.BaseTransaction;
import org.example.transaction.Transaction;
import org.example.transaction.TransactionService;
import org.example.user.User;
import org.example.user.UserService;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class BankApplication extends Application {

    private static final String OUTPUT_DIR = "fortprog-verwal.save-location";

    private final User user;
    private final UserService userService;
    private final TransactionService transactionService;

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
                System.err.println("Ungueltiger Betrag, so viel Patte hast du nicht! Neustart.");
                return getAmount();
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Bitte gib eine gueltige Zahl ein!");
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
            if(transactionService.save(transaction)){
                userService.processTransaction(transaction);
                System.out.println("Ueberweisung wird getaetigt");
            } else {
                System.err.println("Ueberweisung wurde nicht getaetigt");
            }
        }
        else if (antwort.equals("j")) {
            String nachricht = reader.readLine();
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), user, empfaenger.get(),
                    amount, Timestamp.valueOf(LocalDateTime.now()), nachricht);

            if(transactionService.save(transaction)){
                userService.processTransaction(transaction);
                System.out.println("Ueberweisung wird getaetigt");
            } else {
                System.err.println("Ueberweisung wurde nicht getaetigt");
            }

        }
        else {
            System.err.println("Bitte Ja / Nein eingeben. Keine Nachricht wird erstellt.");
        }
        onOpen();
    }

    public void listUsers() {
        System.out.println("Folgende User existieren:");
        userService.findAll().forEach(user -> System.out.println(user.toString()));
    }

    public void withdrawl() throws IOException {
        System.out.println("Aktuelles Guthaben: " + user.getGehalt());
        System.out.println("Wie viel Geld möchtest du abheben?");
        String menge = reader.readLine();
        try{
            double amount = Double.parseDouble(menge);
            if(amount > user.getGehalt()) {
                System.out.println("Du kannst nicht mehr Geld abheben als du auf dem Konto hast!");
            } else {
                user.setGehalt(user.getGehalt() - amount);
                System.out.println("\nDein neuer Kontostand beträgt: "+user.getGehalt()+"\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Eingabefehler!");
        }

    }

    private void printCsv(){/*
        if(System.getProperty(OUTPUT_DIR) == null){
            System.out.print("Bitte gib ein Verzeichnis zum Speichern an: ");
            try {
                System.setProperty(OUTPUT_DIR, reader.readLine().trim());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                printCsv();
            }
        }else{*/
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            File file = new File(System.getProperty("user.home")+"/banking", "Kontoauszug_"+
                    now.toString().replace(":", ".")+".csv");
            CsvDatasheet datasheet = new CsvDatasheet(file);
            for(Transaction transaction : transactionService.findAll()){
                if(transaction.getSender().equals(user))datasheet.append(transaction);
            }
            datasheet.save();
            System.out.println("[GREEN]Der Kontoauszug wurde erstellt! ("+file.getAbsolutePath()+")");
        //}
    }

    private void readCsv(){
        System.out.print("Bitte gib den Pfad einer Datei für die Überweisung an: ");
        try {
            File file = new File(reader.readLine());
            if(!file.exists()){
                System.out.println("[RED]Fehler! Diese Datei existiert nicht!");
                return;
            }
            CsvDatasheet datasheet = new CsvDatasheet(file);
            boolean success = datasheet.load();
            if(!success){
                System.out.println("[RED]Laden der Datei fehlgeschlagen!");
                return;
            }
            if(user.getGehalt() < datasheet.getLoadedTotalMoney()){
                System.out.println("[RED]Dein Guthaben von [YELLOW]"+user.getGehalt()+" [RED]unterschreitet die " +
                        "Transaktionsmenge von [YELLOW]"+datasheet.getLoadedTotalMoney()+"[RED]!");
                return;
            }
            for(BaseTransaction itr : datasheet.getAll()){
                userService.findByAttribute("name", itr.getReceiverMail()).ifPresentOrElse(user -> {
                    Transaction transaction = new Transaction(itr.getTransactionId(), user,
                            user, itr.getAmount(), itr.getDate(), itr.getPurposeMessage());
                    if(transactionService.save(transaction)){
                        userService.processTransaction(transaction);
                    }
                }, () -> System.err.println("[RED]Fehler! Dieser User wurde nicht gefunden!"));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen() throws IOException {
        System.out.println("\nWillkommen im Banking-Menue!");
        System.out.println("""
                Was moechtest du tun?
                Guthaben einsehen (0)
                Kontoauszuege (1)
                Einzelueberweisung (2)
                Massenueberweisung (3)
                Geld abheben (4)
                Kontoauszuege in CSV exportieren (5)
                Alle Nutzer auflisten (6)
                Zurueck ins Hauptmenue (7)
                Abmelden (8)
                """);
        System.out.print("Eingabe: ");
        String input = reader.readLine();
        switch (input) {
            case "0" -> System.out.println("Aktuelles Guthaben: " + user.getGehalt());
            case "1" -> {
                //TODO kontoauszüge
            }
            case "2" -> {
                try {
                    ueberweisen();
                } catch (IOException e) {
                    System.err.println("Input-Fehler!");
                }
            }
            case "3" -> {
                readCsv();
            }
            case "4" -> withdrawl();
            case "5" -> {
                printCsv();
            }
            case "6" -> listUsers();
            case "7" -> Main.setScreen(new MainScreen(10, user));
            case "8" -> logout();
            default -> System.out.println("Eingabefehler!");
        }
        onOpen();
    }

    @Override
    public void logout() throws IOException {
        Main.setScreen(new MainScreen(10));
    }

}
