import org.example.mail.BBMail;
import org.example.mail.BBMailService;
import org.example.mail.PinnwandComment;
import org.example.transaction.Transaction;
import org.example.transaction.TransactionService;
import org.example.user.User;
import org.example.user.UserService;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ServiceTest {
    @Test
    void userTest() {
        UserService userService = new UserService();

        System.out.println(userService.findAll());
        userService.save(new User(UUID.randomUUID().toString(),
                UUID.randomUUID() + "@gmail.com", BCrypt.hashpw("123", BCrypt.gensalt()), 10000));
        System.out.println(userService.findAll());
    }

    @Test
    void postfachTest() {
        UserService userService = new UserService();
        User user = userService.findAll().getFirst();
        BBMailService mailService = new BBMailService(userService);


        List<BBMail> ownMail = mailService.findAll().stream()
                .filter(bbMail -> bbMail.getSender().equals(user) || bbMail.getRecipient().equals(user))
                .sorted(Comparator.comparing(mail -> ((BBMail) mail).getRecipient().getUsermail())
                        .thenComparing(mail -> ((BBMail) mail).getDate()))
                .toList();
        for (BBMail mail : ownMail) {
            System.out.println(mail);
        }
    }

    @Test
    void loginTest() {
        UserService userService = new UserService();

        System.out.println(userService.findAll());

        System.out.println(userService.validateUser("boris@gmail.com", "boris123"));
    }

    @Test
    void transactionTest() {
        UserService userService = new UserService();
        User sender = userService.findAll().get(0), recipient = userService.findAll().get(1);

        TransactionService transactionService = new TransactionService(userService);
//
//        for (int i = 0; i < 10; i++) {
//            transactionService.save(new Transaction(UUID.randomUUID().toString(),
//                    sender, recipient, 100, Timestamp.valueOf(LocalDateTime.now()), null));
//        }

        for (Transaction transaction : transactionService.findAll()) {
            System.out.println(transaction);
        }
    }

    @Test
    void mailTest() {
        UserService userService = new UserService();
        User sender = userService.findAll().get(0), recipient = userService.findAll().get(1);

        BBMailService mailService = new BBMailService(userService);

        for (int i = 0; i < 10; i++) {
            mailService.save(new BBMail(UUID.randomUUID().toString(), sender, recipient,
                    "Hallo Message!", Timestamp.valueOf(LocalDateTime.now())));
        }
    }

    @Test
    void pinnwandTest() {
        UserService userService = new UserService();
        User owner = userService.findAll().get(0), author = userService.findAll().get(1);
        BBMailService mailService = new BBMailService(userService);

        for (int i = 0; i < 10; i++) {
            mailService.savePinnwand(new PinnwandComment(UUID.randomUUID().toString(), owner, author,
                    "Hallo Message!", Timestamp.valueOf(LocalDateTime.now())));
        }

        System.out.println(mailService.getPinnwand());

//        for (PinnwandComment comment : mailService.getPinnwand()) {
//            System.out.println(comment);
//        }
    }

    @Test
    void mailFetchTest() {
        BBMailService mailService = new BBMailService(new UserService());
        for (BBMail mail : mailService.findAll()) {
            System.out.println(mail);
        }
    }
}
