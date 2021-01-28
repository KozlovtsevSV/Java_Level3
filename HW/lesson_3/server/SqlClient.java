package com.geekbrains.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SqlClient {

    private static Connection connection;
    //private static Statement statement;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:BD.db");
         } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNickname(String login, String password) {
        String result = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select UserName from Users where UserName=? and UserPassword=?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try (ResultSet set = preparedStatement.executeQuery()) {
                if (set.next()) {
                    result =  set.getString("UserName");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static Boolean setNickname(String login, String password, String newLogin) {
        Boolean result;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET UserName = ? where UserName=? and UserPassword=?");
            preparedStatement.setString(1, newLogin);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            result = preparedStatement.executeUpdate() >= 1;

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    public static Boolean insertMsg(String loginFrom, String loginTo, String Msg) {
        Boolean result;

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO Messages (FromUserID, ToUserID, Msg)"
                            +" SELECT Users.UserID AS FromUserID, ifnull(UsersTo.UserID, 0) AS ToUserID, ? AS Msg FROM Users"
                            + " Left Join Users AS UsersTo ON ? AND UsersTo.UserName = ? WHERE Users.UserName = ?");

            preparedStatement.setString(1, Msg);
            preparedStatement.setBoolean(2, (loginTo == "")? false: true);
            preparedStatement.setString(3, loginTo);
            preparedStatement.setString(4, loginFrom);

            result = preparedStatement.executeUpdate() >= 1;

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    public static ArrayList <HashMap> getMsg(String login, String password, Integer countMsg) {
        ArrayList <HashMap> result = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select Messages.ID AS ID, Users.UserName AS ToUser, UsersFrom.UserName AS FromUser, Messages.Msg AS Msg from Users \n" +
                    "Left Join Messages AS Messages ON Users.UserID = Messages.ToUserID Left Join Users AS UsersFrom ON \n" +
                    "Messages.FromUserID = UsersFrom.UserID where  Users.UserName=? and  Users.UserPassword=? and Messages.Msg IS NOT NULL \n" +
                    "UNION ALL select Messages.ID, ?, UsersFrom.UserName, Messages.Msg from Messages AS Messages \n" +
                    "Left Join Users AS UsersFrom ON Messages.FromUserID = UsersFrom.UserID WHERE Messages.ToUserID = 0 ORDER BY ID LIMIT ?");

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, login);
            preparedStatement.setInt(4, countMsg);
            try (ResultSet set = preparedStatement.executeQuery()) {
                while (set.next()) {
                    HashMap item = new HashMap();
                    item.put("ToUser",set.getString("ToUser"));
                    item.put("FromUser",set.getString("FromUser"));
                    item.put("Msg",set.getString("Msg"));
                    result.add(item);
                   }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

}
