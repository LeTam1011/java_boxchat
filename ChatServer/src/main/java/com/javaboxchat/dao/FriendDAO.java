package com.javaboxchat.dao;

import com.javaboxchat.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {
    public List<String> getPendingRequests(
            String username
    ) {

        List<String> list =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    SELECT sender
                    FROM friends
                    WHERE receiver=?
                    AND status='PENDING'
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                list.add(
                        rs.getString("sender")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void sendRequest(
            String sender,
            String receiver
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    INSERT INTO friends
                    (
                        sender,
                        receiver,
                        status
                    )
                    VALUES
                    (
                        ?,?,
                        'PENDING'
                    )
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, sender);
            ps.setString(2, receiver);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void acceptRequest(
            String sender,
            String receiver
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    UPDATE friends
                    SET status='ACCEPTED'
                    WHERE sender=?
                    AND receiver=?
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, sender);
            ps.setString(2, receiver);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<String> getFriends(
            String username
    ) {

        List<String> list =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    SELECT
                    CASE
                        WHEN sender=? THEN receiver
                        ELSE sender
                    END AS friend_name
                    FROM friends
                    WHERE
                    (
                        sender=?
                        OR receiver=?
                    )
                    AND status='ACCEPTED'
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, username);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                list.add(
                        rs.getString(
                                "friend_name"
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}