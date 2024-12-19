package org.example.ui;

import org.example.Main;
import org.example.mail.BBMail;
import org.example.mail.BBMailService;
import org.example.mail.PinnwandComment;
import org.example.user.User;
import org.example.user.UserService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MailApplication extends Application {

    private User user;
    private UserService userService;
    private BBMailService mailService;

    public MailApplication(User user) {
        super(user);
        this.user = user;
        userService = new UserService();
        mailService = new BBMailService(userService);
    }

    private void writePinComment() throws IOException {
        System.out.println("Wem moechtest du einen Pinnwandkommentar schreiben? ");
        String input_user = reader.readLine();
        Optional<User> optionalUser = userService.findByAttribute("name", input_user);
        if (optionalUser.isPresent()) {
            System.out.println("User mit Namen \"" + input_user + "\" wurde gefunden.\nWas magst du ihm schreiben?");
            String comment = reader.readLine();
            mailService.savePinnwand(new PinnwandComment(UUID.randomUUID().toString(),
                    optionalUser.get(), user, comment, Timestamp.valueOf(LocalDateTime.now())));
            System.out.println("Kommentar erstellt.");
        }
        else {
            System.out.println("Leider kein User gefunden. Moechtest du neustarten? (j/n) ");
            String antwort = reader.readLine();
            if (antwort.equals("j")) {
                writePinComment();
            }
            else {
                System.out.println("Abbruch.");
            }
        }
    }

    private void seeOtherPin() throws IOException {
        System.out.println("Wessen Pinnwand moechtest du sehen? ");
        String input_user = reader.readLine();
        Optional<User> optionalUser = userService.findByAttribute("name", input_user);
        if (optionalUser.isPresent()) {
            System.out.println("User mit Namen \"" + input_user + "\" wurde gefunden.\nPinnwand wird gedruckt: ");
            List<PinnwandComment> otherComments = mailService.getPinnwand().stream()
                    .filter(comment -> comment.getOwner().equals(optionalUser.get())).toList();
            for (PinnwandComment comment : otherComments) {
                System.out.println(comment);
            }
            System.out.println("\n\n");
        }
        else {
            System.out.println("Leider kein User gefunden. Moechtest du neustarten? (j/n) ");
            String antwort = reader.readLine();
            if (antwort.equals("j")) {
                seeOtherPin();
            }
            else {
                System.out.println("Abbruch.");
            }
        }
    }

    private void writeDM() throws IOException {
        System.out.println("Wem moechtest du direkt schreiben? ");
        String input_user = reader.readLine();
        Optional<User> optionalUser = userService.findByAttribute("name", input_user);
        if (optionalUser.isPresent()) {
            System.out.println("User mit Namen \"" + input_user + "\" wurde gefunden.\nWas magst du ihm schreiben? ");
            String comment = reader.readLine();
            mailService.save(new BBMail(UUID.randomUUID().toString(), user, optionalUser.get(),
                    comment, Timestamp.valueOf(LocalDateTime.now())));
        }
        else {
            System.out.println("Leider kein User gefunden. Moechtest du neustarten? (j/n) ");
            String antwort = reader.readLine();
            if (antwort.equals("j")) {
                seeOtherPin();
            }
            else {
                System.out.println("Abbruch.");
            }
        }
    }

    @Override
    public void onOpen() throws IOException {
        System.out.println("Willkommen im Postfach");
        System.out.println("""
                Was moechtest du tun?
                Eigene Pinnwand einsehen (0)
                Andere Pinnwand ansehen (1)
                Pinnwandkommentar schreiben (2)
                DM schreiben (3)
                Posteingang aufrufen (4)
                Zurueck ins Hauptmenue (5)
                """);
        System.out.print("Eingabe: ");
        String input = reader.readLine();
        switch (input) {
            case "0" -> {
                List<PinnwandComment> ownedComments = mailService.getPinnwand().stream()
                        .filter(comment -> comment.getOwner().equals(user))
                        .toList();
                for (PinnwandComment comment : ownedComments) {
                    System.out.println(comment);
                }
            }
            case "1" -> seeOtherPin();
            case "2" -> {
                writePinComment();
            }
            case "3" -> {
                writeDM();
            }
            case "4" -> {
                List<BBMail> ownMail = mailService.findAll().stream()
                        .filter(bbMail -> bbMail.getSender().equals(user) || bbMail.getRecipient().equals(user))
                        .sorted(Comparator.comparing(mail -> ((BBMail) mail).getRecipient().getUsermail())
                                .thenComparing(mail -> ((BBMail) mail).getDate()))
                        .toList();
                for (BBMail mail : ownMail) {
                    System.out.println(mail);
                }
                onOpen();
            }
            case "5" -> {
                Main.setScreen(new MainScreen(10, user));
            }
            default -> {
            }
        }
        onOpen();
    }

    @Override
    public void takeInput(String string) {

    }


    @Override
    public void logout() throws IOException {
        Main.setScreen(new MainScreen(10));
    }
}
