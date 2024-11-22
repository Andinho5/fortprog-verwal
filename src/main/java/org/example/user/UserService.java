package org.example.user;

import org.example.service.ModelService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements ModelService<User> {

    private Connection connection;

    public UserService() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:./src/main/resources/testat_db.db");
        } catch (SQLException e) {
            throw new RuntimeException("Could not connect to database: ", e);
        }
    }

    @Override
    public User findById(String id) {
        return null;
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
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
