package com.javaboxchat.ui;

import javax.swing.*;
import java.awt.*;
import com.google.gson.Gson;
import com.javaboxchat.model.LoginRequest;
import com.javaboxchat.model.LoginResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JButton btnLogin;
    private JButton btnRegister;

    public LoginFrame() {

        setTitle("Java Box Chat");

        setSize(400, 300);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        initEvents();

        setVisible(true);
    }

    private void initComponents() {

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(
                        5,
                        1,
                        10,
                        10
                )
        );

        JLabel lblTitle =
                new JLabel(
                        "JAVA BOX CHAT",
                        SwingConstants.CENTER
                );

        lblTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        txtUsername =
                new JTextField();

        txtPassword =
                new JPasswordField();

        btnLogin =
                new JButton(
                        "Đăng nhập"
                );

        btnRegister =
                new JButton(
                        "Đăng ký"
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        panel.add(lblTitle);

        panel.add(txtUsername);

        panel.add(txtPassword);

        panel.add(btnLogin);

        panel.add(btnRegister);

        add(panel);
    }

    private void initEvents() {
        btnLogin.addActionListener(e -> {

            try {

                String username =
                        txtUsername.getText();

                String password =
                        new String(
                                txtPassword.getPassword()
                        );

                Socket socket =
                        new Socket(
                                "10.199.135.126",
                                9999
                        );

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()
                                )
                        );

                PrintWriter writer =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true
                        );

                Gson gson =
                        new Gson();

                LoginRequest request =
                        new LoginRequest(
                                "LOGIN",
                                username,
                                password
                        );

                writer.println(
                        gson.toJson(request)
                );

                String responseJson =
                        reader.readLine();

                System.out.println(
                        "SERVER RESPONSE = "
                                + responseJson
                );

                if (responseJson == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Server không phản hồi!"
                    );

                    socket.close();
                    return;
                }

                if (responseJson.contains(
                        "LOGIN_BLOCKED"
                )) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Tài khoản đã bị khóa!"
                    );

                    socket.close();
                    return;
                }

                LoginResponse response =
                        gson.fromJson(
                                responseJson,
                                LoginResponse.class
                        );

                if (response.isSuccess()) {

                    JOptionPane.showMessageDialog(
                            this,
                            response.getMessage()
                    );

                    ChatFrame chatFrame =
                            new ChatFrame(
                                    socket,
                                    reader,
                                    writer,
                                    username
                            );

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            response.getMessage()
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Không kết nối được Server!"
                );
            }
        });
        btnRegister.addActionListener(e -> {

            new RegisterFrame();

            dispose();
        });
    }


    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnRegister() {
        return btnRegister;
    }
}
