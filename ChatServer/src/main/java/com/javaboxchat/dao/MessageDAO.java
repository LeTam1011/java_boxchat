package com.javaboxchat.dao;

import com.javaboxchat.database.DBConnection;
import com.javaboxchat.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public List<Message> getConversation(
            String user1,
            String user2
    ) {

        List<Message> list =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    SELECT *
                    FROM messages
                    WHERE
                    (sender=? AND receiver=?)
                    OR
                    (sender=? AND receiver=?)
                    ORDER BY id
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, user1);
            ps.setString(2, user2);
            ps.setString(3, user2);
            ps.setString(4, user1);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                Message msg =
                        new Message(
                                "MESSAGE",
                                rs.getString("sender"),
                                rs.getString("receiver"),
                                rs.getString("content"),
                                rs.getString("timestamp"),
                                rs.getString("message_type")
                        );


                msg.setId(
                        rs.getInt("id")
                );


                msg.setRecalled(
                        rs.getBoolean("recalled")
                );

                msg.setFilePath(
                        rs.getString(
                                "file_path"
                        )
                );

                list.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public int saveMessage(
            String sender,
            String receiver,
            String content,
            String timestamp,
            String messageType,
            String filePath
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    INSERT INTO messages
                    (
                        sender,
                        receiver,
                        content,
                        timestamp,
                        message_type,
                        file_path
                    )
                    VALUES
                    (
                        ?,?,?,?,?,?
                    )
                    """;

            PreparedStatement ps =
                    con.prepareStatement(
                            sql,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, content);
            ps.setString(4, timestamp);
            ps.setString(5, messageType);
            ps.setString(6, filePath);

            ps.executeUpdate();

            ResultSet rs =
                    ps.getGeneratedKeys();

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return -1;
    }
    public void recallMessage(
            int id
    ) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    """
                    UPDATE messages
                    SET recalled = true
                    WHERE id = ?
                    """;

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

