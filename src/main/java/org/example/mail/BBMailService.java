package org.example.mail;

import org.example.service.ModelService;
import org.example.user.User;
import org.example.user.UserService;
import org.example.util.ReceiverNotFoundException;
import org.example.util.SenderNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BBMailService implements ModelService<BBMail> {

    private final UserService userService;

    public BBMailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<BBMail> findById(String id) {
        for (BBMail bbMail : findAll()) {
            if (bbMail.getChatid().equals(id)) {
                return Optional.of(bbMail);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<BBMail> findByAttribute(String attr, String value) {
        return Optional.empty();
    }

    @Override
    public List<BBMail> findAll() {
        List<BBMail> mails = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM messages");
            while (resultSet.next()) {
                Optional<User> optionalSender = userService.findById(resultSet.getString("senderid"));
                Optional<User> optionalReceiver = userService.findById(resultSet.getString("receiverid"));
                if (optionalSender.isEmpty()) {
                    throw new SenderNotFoundException("Sender not found");
                }
                if (optionalReceiver.isEmpty()) {
                    throw new ReceiverNotFoundException("Receiver not found");
                }
                BBMail mail = new BBMail();
                mail.setChatid(resultSet.getString("chatid"));
                mail.setSender(optionalSender.get());
                mail.setRecipient(optionalReceiver.get());
                mail.setContent(resultSet.getString("content"));
                mail.setDate(Timestamp.valueOf(resultSet.getString("date")));
                mails.add(mail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (SenderNotFoundException e) {

        } catch (ReceiverNotFoundException e) {

        }
        return mails;
    }

    @Override
    public void save(BBMail bbMail) {

    }
}
