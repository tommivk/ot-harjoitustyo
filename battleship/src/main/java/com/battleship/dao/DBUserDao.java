package com.battleship.dao;

import com.battleship.domain.User;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class DBUserDao implements UserDao {

    public List<User> getAll() throws SQLException {
        List<User> list = new ArrayList<User>();
        Connection db = DriverManager.getConnection("jdbc:sqlite:data.db");

        try {
            Statement s = db.createStatement();
            ResultSet r = s.executeQuery("SELECT * FROM Users");

            while (r.next()) {
                list.add(new User(r.getString("username")));
            }

            return list;
        } catch (SQLException e) {

        } finally {
            db.close();
        }
        return list;
    }

    public User login(String username) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:data.db");

        try {
            PreparedStatement p = db.prepareStatement("SELECT * FROM Users WHERE username = ?");
            p.setString(1, username);

            ResultSet r = p.executeQuery();
            if (!r.next()) {
                return null;
            }

            int id = Integer.valueOf(r.getString("id"));
            return new User(r.getString("username"), id);

        } catch (SQLException e) {
            System.out.println("Login error: " + e);
            return null;
        } finally {
            db.close();
        }

    }

    public User create(String username) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:data.db");

        try {

            PreparedStatement p = db.prepareStatement("INSERT INTO Users(username) VALUES (?)");
            p.setString(1, username);
            p.executeUpdate();
            System.out.print("New user ");
            System.out.print(username);
            System.out.println(" added");
            return new User(username);
        } catch (SQLException e) {
            System.out.println("Couldn't add new user" + e);
            return null;
        } finally {
            db.close();
        }
    }
}