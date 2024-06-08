package com.example.utils;

import com.example.models.UserAccount;
import org.h2.store.Data;

import java.sql.*;
import java.util.Date;
import java.time.Instant;

public class DBUtils {

    public static  void save_user(Connection conn, String userName, String password) throws SQLException {

        try{
            String sql = "create table user(" +
                    "user_name varchar(255) UNIQUE ,\n" +
                    "    password varchar(255),\n" +
                    "    login_date varchar(255),\n" +
                    "    logout_date varchar(255)\n" +
                    ");";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
        } catch (Exception ignored){}

        String sql = "INSERT INTO user values ( ?, ? , ? , ?);";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, userName);
        pstm.setString(2, password);
        pstm.setString(3, "");
        pstm.setString(4, "");
        pstm.execute();
    }

    public static void logout(Connection conn, String userName) throws SQLException {

        Date date = (Date) Date.from(Instant.now());
        String sql = "update user set logout_date = ? where user_name = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, String.valueOf(date));
        pstm.setString(2, userName);
        pstm.execute();
    }

    public static UserAccount findUser(Connection conn, String userName, String password) throws SQLException {
        String sql = "Select a.user_name, a.password, a.login_date, a.logout_date from user a where a.user_name = ? and a.password = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, userName);
        pstm.setString(2, password);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            UserAccount user = new UserAccount();
            user.setUserName(userName);
            user.setPassword(password);
            user.setLogoutDate(rs.getString(rs.findColumn("logout_date")));

            Date date = (Date) Date.from(Instant.now());
            sql = "update user set login_date = ? where user_name = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, String.valueOf(date));
            pstm.setString(2, password);
            pstm.execute();

            user.setLoginDate(String.valueOf(date));
            return user;
        }
        return null;
    }

    public static UserAccount findUserByEmail(Connection conn, String userName) throws SQLException {
        String sql = "Select a.user_name, a.password from user a where a.user_name = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, userName);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            UserAccount user = new UserAccount();
            user.setPassword(rs.getString(rs.findColumn("password")));
            user.setUserName(userName);
            return user;
        }
        return null;
    }

}
