import org.example.mail.BBMail;
import org.example.mail.BBMailService;
import org.example.transaction.Transaction;
import org.example.transaction.TransactionService;
import org.example.user.User;
import org.example.user.UserService;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceTest {
    @Test
    void userTest() {
        UserService userService = new UserService();

        System.out.println(userService.findAll());
        userService.save(new User(UUID.randomUUID().toString(),
                UUID.randomUUID() + "@gmail.com", "123", 10000));
        System.out.println(userService.findAll());
    }

    @Test
    void transactionTest() {
        UserService userService = new UserService();
        User sender = userService.findAll().get(0), recipient = userService.findAll().get(1);

        TransactionService transactionService = new TransactionService(userService);

//        System.out.println(transactionService.findAll());
//        for (int i = 0; i < 10; i++) {
//            transactionService.save(new Transaction(UUID.randomUUID().toString(),
//                    sender, recipient, 100, Timestamp.valueOf(LocalDateTime.now()), "Test" + LocalDateTime.now()));
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
    void mailFetchTest() {
        BBMailService mailService = new BBMailService(new UserService());
        for (BBMail mail : mailService.findAll()) {
            System.out.println(mail);
        }
    }
}
