package com.javaboxchat.ui;

import com.google.gson.Gson;
import com.javaboxchat.model.RegisterRequest;
import com.javaboxchat.model.RegisterResponse;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RegisterFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtPhone;

    private JButton btnRegister;
    private JButton btnBack;

    public RegisterFrame() {

        setTitle("Đăng ký tài khoản");

        setSize(400, 350);

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
                        8,
                        1,
                        10,
                        10
                )
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel lblTitle =
                new JLabel(
                        "ĐĂNG KÝ TÀI KHOẢN",
                        SwingConstants.CENTER
                );

        lblTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        txtUsername =
                new JTextField();

        txtPassword =
                new JPasswordField();

        txtPhone =
                new JTextField();

        btnRegister =
                new JButton(
                        "Đăng ký"
                );

        btnBack =
                new JButton(
                        "Quay lại"
                );

        panel.add(lblTitle);

        panel.add(
                new JLabel("Username")
        );

        panel.add(txtUsername);

        panel.add(
                new JLabel("Password")
        );

        panel.add(txtPassword);

        panel.add(
                new JLabel("Số điện thoại")
        );

        panel.add(txtPhone);

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                10
                        )
                );

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBack);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initEvents() {

        btnRegister.addActionListener(e -> {

            try {

                String username =
                        txtUsername.getText();

                String password =
                        new String(
                                txtPassword.getPassword()
                        );

                String phone =
                        txtPhone.getText();

                phone = phone.trim();

                if (!phone.matches("\\d{10}")) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Số điện thoại phải gồm đúng 10 chữ số!"
                    );

                    return;
                }

                Socket socket =
                        new Socket(
                                "localhoast",
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

                RegisterRequest request =
                        new RegisterRequest(
                                "REGISTER",
                                username,
                                password,
                                phone
                        );

                writer.println(
                        gson.toJson(request)
                );

                String responseJson =
                        reader.readLine();

                RegisterResponse response =
                        gson.fromJson(
                                responseJson,
                                RegisterResponse.class
                        );

                JOptionPane.showMessageDialog(
                        this,
                        response.getMessage()
                );

                if (response.isSuccess()) {

                    new LoginFrame();

                    dispose();
                }

                socket.close();

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Không kết nối được Server!"
                );
            }
        });

        btnBack.addActionListener(e -> {

            new LoginFrame();

            dispose();
        });
    }
}