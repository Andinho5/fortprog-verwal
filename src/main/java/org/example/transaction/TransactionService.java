package org.example.transaction;

import org.example.service.ModelService;
import org.example.user.User;
import org.example.user.UserService;
import org.example.util.ReceiverNotFoundException;
import org.example.util.SenderNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionService implements ModelService<Transaction> {

    private UserService userService;

    public TransactionService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                Optional<User> optionalSender = userService.findById(resultSet.getString("sender"));
                Optional<User> optionalReceiver = userService.findById(resultSet.getString("receiver"));
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
                transaction.setPurposemessage(resultSet.getString("purposemessage"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (SenderNotFoundException e) {
            e.printStackTrace();
        } catch (ReceiverNotFoundException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public void save(Transaction transaction) {

    }
}
