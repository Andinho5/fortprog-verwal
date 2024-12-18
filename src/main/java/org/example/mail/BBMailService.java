package org.example.mail;

import org.example.service.ModelService;
import org.example.user.User;
import org.example.user.UserService;
import org.example.util.ReceiverNotFoundException;
import org.example.util.SenderNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BBMailService implements ModelService<BBMail> {

    private final UserService userService;

    public BBMailService(UserService userService) {
        this.userService = userService;
    }

    public List<PinnwandComment> getPinnwand() {
        List<PinnwandComment> comments = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from pinwand");
            while (resultSet.next()) {
                Optional<User> optionalOwner = userService.findById(resultSet.getString("owner")),
                    optionalAuthor = userService.findById(resultSet.getString("author"));

                PinnwandComment comment = new PinnwandComment();
                comment.setCommentid(resultSet.getString("commentid"));
                optionalOwner.ifPresent(comment::setOwner);
                optionalAuthor.ifPresent(comment::setAuthor);
                comment.setContent(resultSet.getString("content"));
                comment.setDate(new Timestamp(Long.parseLong(resultSet.getString("date"))));
                comments.add(comment);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    public void savePinnwand(PinnwandComment comment) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into pinwand " +
                    "(commentid, owner, author, content, date) values (?, ?, ?, ?, ?)");
            statement.setString(1, comment.getCommentid());
            statement.setString(2, comment.getOwner().getUserid());
            statement.setString(3, comment.getAuthor().getUserid());
            statement.setString(4, comment.getContent());
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        if (attr.equals("id")) {
            for (BBMail bbMail : findAll()) {
                if (bbMail.getChatid().equals(value)) {
                    return Optional.of(bbMail);
                }
            }
        }
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
                Timestamp timestamp = new Timestamp(Long.parseLong(resultSet.getString("date")));
                mail.setDate(timestamp);
                mails.add(mail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (SenderNotFoundException e) {
            throw new RuntimeException("Sender nicht gefunden", e);
        } catch (ReceiverNotFoundException e) {
            throw new RuntimeException("Empfänger nicht gefunden", e);
        }
        return mails;
    }

    @Override
    public boolean save(BBMail bbMail) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into messages " +
                    "(chatid, senderid, receiverid, content, date) values (?, ?, ?, ?, ?)");
            statement.setString(1, bbMail.getChatid());
            statement.setString(2, bbMail.getSender().getUserid());
            statement.setString(3, bbMail.getRecipient().getUserid());
            statement.setString(4, bbMail.getContent());
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
