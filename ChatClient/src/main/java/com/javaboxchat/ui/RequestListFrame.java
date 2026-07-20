package com.javaboxchat.ui;

import com.google.gson.Gson;
import com.javaboxchat.model.AcceptFriendRequest;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.List;

public class RequestListFrame extends JFrame {

    private JList<String> requestList;
    private JButton btnAccept;
    private String currentUser;

    public RequestListFrame(
            List<String> requests,
            String currentUser,
            PrintWriter writer
    ) {

        setTitle("Lời mời kết bạn");

        setSize(300,400);

        setLocationRelativeTo(null);

        DefaultListModel<String> model =
                new DefaultListModel<>();

        for (String user : requests) {

            System.out.println(
                    "REQUEST = " + user
            );

            model.addElement(user);
        }

        requestList =
                new JList<>(model);

        btnAccept =
                new JButton("Đồng ý");

        setLayout(
                new BorderLayout()
        );

        add(
                new JScrollPane(requestList),
                BorderLayout.CENTER
        );

        add(
                btnAccept,
                BorderLayout.SOUTH
        );

        setVisible(true);

        btnAccept.addActionListener(e -> {

            String sender =
                    requestList.getSelectedValue();

            if (sender == null) {
                return;
            }

            Gson gson =
                    new Gson();

            AcceptFriendRequest request =
                    new AcceptFriendRequest(
                            "ACCEPT_FRIEND",
                            sender,
                            currentUser
                    );

            writer.println(
                    gson.toJson(request)
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Đã chấp nhận kết bạn"
            );
        });
    }

    public JList<String> getRequestList() {
        return requestList;
    }

    public JButton getBtnAccept() {
        return btnAccept;
    }
}