package org.example.user;

import org.example.service.ModelService;
import org.example.transaction.Transaction;
import org.example.util.DBConnector;
import org.example.util.MailInvalidException;
import org.example.util.Regex;
import org.example.util.UserNameAlreadyUsedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserService implements ModelService<User> {

    public void processTransaction(Transaction transaction) {
        System.out.println(transaction);
        try {
            User sender = transaction.getSender(), receiver = transaction.getReceiver();
            PreparedStatement statement = connection.prepareStatement("update users set gehalt = ? where userid = ?");
            statement.setDouble(1, sender.getGehalt() - transaction.getAmount());
            statement.setString(2, sender.getUserid());
            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement("update users set gehalt = ? where userid = ?");
            statement2.setDouble(1, receiver.getGehalt() + transaction.getAmount());
            statement2.setString(2, receiver.getUserid());
            statement2.executeUpdate();
        }
        catch (SQLException e) {
        }
    }

    public boolean validateUser(String username, String password) {
        for (User user : findAll()) {
            if (user.getUsermail().equals(username) &&
                    user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean mailMatches(String mail) {
        return Regex.EMAIL.matcher(mail).find();
    }

    @Override
    public Optional<User> findById(String id) {
        for (User user : findAll()) {
            if (user.getUserid().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByAttribute(String attr, String value) {
        switch (attr) {
            case "name":
            case "username":
            case "mail":
            case "usermail":
                for (User user : findAll()) {
                    if (user.getUsermail().equals(value)) {
                        return Optional.of(user);
                    }
                }
                break;
            case "email":
                for (User user : findAll()) {
                    if (user.getUsermail().equals(value)) {
                        System.out.println(user);
                        return Optional.of(user);
                    }
                }
                break;
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                User user = new User();
                user.setUserid(resultSet.getString("userid"));
                user.setUsermail(resultSet.getString("usermail"));
                user.setPassword(resultSet.getString("password"));
                user.setGehalt(resultSet.getDouble("gehalt"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public boolean save(User user) throws UserNameAlreadyUsedException {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into users " +
                    "(userid, usermail, password, gehalt) values (?, ?, ?, ?)");
            statement.setString(1, user.getUserid());
            statement.setString(2, user.getUsermail());
            statement.setString(3, user.getPassword());
            statement.setDouble(4, user.getGehalt());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new UserNameAlreadyUsedException("Username already used");
        }
        return true;
    }
}
