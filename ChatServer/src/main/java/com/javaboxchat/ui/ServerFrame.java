package com.javaboxchat.ui;

import com.javaboxchat.server.ClientManager;
import com.javaboxchat.dao.UserDAO;
import com.javaboxchat.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServerFrame extends JFrame {

    private JTextArea txtLog;

    private DefaultListModel<String> onlineModel;

    private JList<String> onlineList;
    private JLabel lblUsername;
    private JLabel lblPhone;
    private JLabel lblStatus;

    private JButton btnDelete;
    private JButton btnLock;
    private JButton btnHistory;
    private JLabel lblBlocked;

    public ServerFrame() {

        setTitle(
                "Java Box Chat Server"
        );

        setSize(
                1000,
                600
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        txtLog =
                new JTextArea();

        txtLog.setEditable(false);

        txtLog.setFont(
                new Font(
                        "Consolas",
                        Font.PLAIN,
                        14
                )
        );

        onlineModel =
                new DefaultListModel<>();

        onlineList =
                new JList<>(onlineModel);

        onlineList.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        onlineList.addListSelectionListener(e -> {

            if (e.getValueIsAdjusting()) {
                return;
            }

            String selected =
                    onlineList.getSelectedValue();

            if (selected == null) {
                return;
            }

            String username =
                    selected
                            .replace("[ONLINE] ", "")
                            .replace("[OFFLINE] ", "");

            loadUserInfo(username);
        });

        JScrollPane userPane =
                new JScrollPane(
                        onlineList
                );

        userPane.setBorder(
                BorderFactory.createTitledBorder(
                        "USERS"
                )
        );

        JScrollPane logPane =
                new JScrollPane(
                        txtLog
                );

        logPane.setBorder(
                BorderFactory.createTitledBorder(
                        "SERVER LOG"
                )
        );

        JPanel infoPanel =
                new JPanel();

        infoPanel.setLayout(
                new BoxLayout(
                        infoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        infoPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "USER INFORMATION"
                )
        );

        lblUsername =
                new JLabel(
                        "Username: "
                );

        lblPhone =
                new JLabel(
                        "Phone: "
                );

        lblStatus =
                new JLabel(
                        "Status: "
                );

        btnLock =
                new JButton(
                        "Khóa tài khoản"
                );

        lblBlocked =
                new JLabel(
                        "Blocked: No"
                );
        btnLock.addActionListener(e -> {

            String selected =
                    onlineList.getSelectedValue();

            if (selected == null) {
                return;
            }

            String username =
                    selected
                            .replace("[ONLINE] ", "")
                            .replace("[OFFLINE] ", "");

            UserDAO dao =
                    new UserDAO();

            User user =
                    dao.getUserByUsername(
                            username
                    );

            dao.setBlocked(
                    username,
                    !user.isBlocked()
            );

            JOptionPane.showMessageDialog(
                    this,
                    user.isBlocked()
                            ? "Đã mở khóa tài khoản"
                            : "Đã khóa tài khoản"
            );

            loadUserInfo(
                    username
            );
        });

        infoPanel.add(lblUsername);
        infoPanel.add(Box.createVerticalStrut(10));

        infoPanel.add(lblPhone);
        infoPanel.add(Box.createVerticalStrut(10));

        infoPanel.add(lblStatus);
        infoPanel.add(Box.createVerticalStrut(20));

        infoPanel.add(btnLock);
        infoPanel.add(Box.createVerticalStrut(10));

        infoPanel.add(lblBlocked);

        JSplitPane topPane =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        userPane,
                        infoPanel
                );

        topPane.setDividerLocation(
                250
        );

        JSplitPane mainPane =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        topPane,
                        logPane
                );

        mainPane.setDividerLocation(
                250
        );

        add(
                mainPane,
                BorderLayout.CENTER
        );

        topPane.setDividerLocation(
                250
        );

        mainPane.setDividerLocation(
                250
        );

        add(
                mainPane,
                BorderLayout.CENTER
        );

        setVisible(true);
    }

    public void log(
            String message
    ) {

        SwingUtilities.invokeLater(() -> {

            txtLog.append(
                    message + "\n"
            );

            txtLog.setCaretPosition(
                    txtLog.getDocument()
                            .getLength()
            );
        });
    }

    public void updateUserList(
            List<String> users
    ) {

        SwingUtilities.invokeLater(() -> {

            onlineModel.clear();

            for (String user : users) {

                boolean online =
                        ClientManager.isOnline(user);

                String text =
                        (online ? "[ONLINE] " : "[OFFLINE] ")
                                + user;

                onlineModel.addElement(text);

                System.out.println(text);
            }
        });
    }
    public void loadUserInfo(
            String username
    ) {

        UserDAO dao =
                new UserDAO();

        User user =
                dao.getUserByUsername(
                        username
                );

        if (user == null) {
            return;
        }

        lblUsername.setText(
                "Username: "
                        + user.getUsername()
        );

        lblPhone.setText(
                "Phone: "
                        + user.getPhone()
        );

        lblStatus.setText(
                ClientManager.isOnline(
                        username
                )
                        ? "Status: ONLINE"
                        : "Status: OFFLINE"
        );
        lblBlocked.setText(
                user.isBlocked()
                        ? "Blocked: YES"
                        : "Blocked: NO"
        );
        if (user.isBlocked()) {

            btnLock.setText(
                    "Mở khóa tài khoản"
            );

        } else {

            btnLock.setText(
                    "Khóa tài khoản"
            );
        }
    }
}