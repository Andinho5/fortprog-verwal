package org.example.transaction;

import org.example.service.ModelService;
import org.example.user.User;
import org.example.user.UserService;
import org.example.util.InsufficientFundsException;
import org.example.util.ReceiverNotFoundException;
import org.example.util.SenderNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionService implements ModelService<Transaction> {

    private final UserService userService;

    public TransactionService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        for (Transaction transaction : findAll()) {
            if (transaction.getTransactionId().equals(id)) {
                return Optional.of(transaction);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> findByAttribute(String attr, String value) {
        if (attr.equals("date")) {
            for (Transaction transaction : findAll()) {
                if (transaction.getDate().compareTo(Date.valueOf(value)) == 0) {
                    return Optional.of(transaction);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM transactions");
            while (resultSet.next()) {
                Optional<User> optionalSender = userService.findByAttribute("mail", resultSet.getString("senderid"));
                Optional<User> optionalReceiver = userService.findByAttribute("mail", resultSet.getString("receiverid"));
                if (optionalSender.isEmpty()) {
                    throw new SenderNotFoundException("sender");
                }
                if (optionalReceiver.isEmpty()) {
                    throw new ReceiverNotFoundException("");
                }
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getString("transactionid"));
                transaction.setSender(optionalSender.get());
                transaction.setReceiver(optionalReceiver.get());
                transaction.setAmount(resultSet.getDouble("menge"));
                Timestamp timestamp = new Timestamp(Long.parseLong(resultSet.getString("date")));
                transaction.setDate(timestamp);
                transaction.setPurposeMessage(resultSet.getString("purposemessage"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (SenderNotFoundException e) {
            System.err.println("Sender not found");
        } catch (ReceiverNotFoundException e) {
            System.err.println("Receiver not found");
        }

        return transactions;
    }

    @Override
    public void save(Transaction transaction) {
        /*
        Safety-Checks
        */
        User sender = transaction.getSender(), receiver = transaction.getReceiver();
        if (transaction.getAmount() > sender.getGehalt()) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        try {
            PreparedStatement statement = connection.prepareStatement("insert into transactions " +
                    "(transactionid, senderid, receiverid, menge, date, purposemessage) values (?, ?, ?, ?, ?, ?)");
            statement.setString(1, transaction.getTransactionId());
            statement.setString(2, transaction.getSender().getUsermail());
            statement.setString(3, transaction.getReceiver().getUsermail());
            statement.setDouble(4, transaction.getAmount());
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(6, transaction.getPurposeMessage());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
