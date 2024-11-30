package org.example.transaction;

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

public class TransactionService implements ModelService<Transaction> {

    private final UserService userService;

    public TransactionService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        for (Transaction transaction : findAll()) {
            if (transaction.getTransactionid().equals(id)) {
                return Optional.of(transaction);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> findByAttribute(String attr, String value) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM transactions");
            while (resultSet.next()) {
                Optional<User> optionalSender = userService.findById(resultSet.getString("senderid"));
                Optional<User> optionalReceiver = userService.findById(resultSet.getString("receiverid"));
                if (optionalSender.isEmpty()) {
                    throw new SenderNotFoundException("sender");
                }
                if (optionalReceiver.isEmpty()) {
                    throw new ReceiverNotFoundException("");
                }
                Transaction transaction = new Transaction();
                transaction.setTransactionid(resultSet.getString("transactionid"));
                transaction.setSender(optionalSender.get());
                transaction.setReceiver(optionalReceiver.get());
                transaction.setMenge(resultSet.getDouble("menge"));
                transaction.setDate(Timestamp.valueOf(LocalDateTime.parse(resultSet.getString("date"))));
                transaction.setPurposemessage(resultSet.getString("purposemessage"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (SenderNotFoundException e) {
            System.err.println("Sender not found");
            e.printStackTrace();
        } catch (ReceiverNotFoundException e) {
            System.err.println("Receiver not found");
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public void save(Transaction transaction) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into transactions " +
                    "(transactionid, senderid, receiverid, menge, date, purposemessage) values (?, ?, ?, ?, ?, ?)");
            statement.setString(1, transaction.getTransactionid());
            statement.setString(2, transaction.getSender().getUsermail());
            statement.setString(3, transaction.getReceiver().getUsermail());
            statement.setDouble(4, transaction.getMenge());
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(6, transaction.getPurposemessage());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
