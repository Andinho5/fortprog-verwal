package org.example.user;

import org.example.service.ModelService;
import org.example.util.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements ModelService<User> {

    public boolean validateUser(String username, String password) {
        for (User user : findAll()) {

            if (user.getUsermail().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<User> findById(String id) {
        for (User user : findAll()) {
            System.out.println(user + ": " + user.getUserid());
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
    public void save(User user) {
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
            throw new RuntimeException(e);
        }
    }
}
