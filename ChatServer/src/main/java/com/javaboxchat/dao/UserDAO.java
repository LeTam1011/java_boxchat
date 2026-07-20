package com.javaboxchat.dao;

import com.javaboxchat.database.DBConnection;
import com.javaboxchat.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public boolean register(
            String username,
            String password,
            String phone
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String checkUserSql =
                    "SELECT * FROM users WHERE username=?";

            PreparedStatement checkUserPs =
                    con.prepareStatement(
                            checkUserSql
                    );

            checkUserPs.setString(
                    1,
                    username
            );

            ResultSet rsUser =
                    checkUserPs.executeQuery();

            if (rsUser.next()) {

                return false;
            }

            String checkPhoneSql =
                    "SELECT * FROM users WHERE phone=?";

            PreparedStatement checkPhonePs =
                    con.prepareStatement(
                            checkPhoneSql
                    );

            checkPhonePs.setString(
                    1,
                    phone
            );

            ResultSet rsPhone =
                    checkPhonePs.executeQuery();

            if (rsPhone.next()) {

                return false;
            }

            String sql =
                    """
                    INSERT INTO users
                    (
                        username,
                        password,
                        phone
                    )
                    VALUES
                    (
                        ?,?,?
                    )
                    """;

            PreparedStatement ps =
                    con.prepareStatement(
                            sql
                    );

            ps.setString(
                    1,
                    username
            );

            ps.setString(
                    2,
                    password
            );

            ps.setString(
                    3,
                    phone
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }


    public boolean login(
            String username,
            String password
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM users WHERE username=? AND password=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public String findUserByPhone(
            String phone
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    SELECT username
                    FROM users
                    WHERE phone=?
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, phone);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getString(
                        "username"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<String> getAllUsers() {

        List<String> users =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT username FROM users";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                users.add(
                        rs.getString(
                                "username"
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return users;
    }
    public User getUserByUsername(
            String username
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    SELECT *
                    FROM users
                    WHERE username=?
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(
                    1,
                    username
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return new User(
                        rs.getString("username"),
                        rs.getString("phone"),
                        rs.getString("status"),
                        rs.getBoolean("blocked"),
                        rs.getString("avatar")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
    public void setBlocked(
            String username,
            boolean blocked
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    UPDATE users
                    SET blocked=?
                    WHERE username=?
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setBoolean(
                    1,
                    blocked
            );

            ps.setString(
                    2,
                    username
            );

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public boolean isBlocked(
            String username
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    SELECT blocked
                    FROM users
                    WHERE username=?
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(
                    1,
                    username
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getBoolean(
                        "blocked"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
    public boolean phoneExists(
            String phone
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT id FROM users WHERE phone=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(
                    1,
                    phone
            );

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}
