package com.javaboxchat.client;

import com.google.gson.Gson;
import com.javaboxchat.model.LoginRequest;
import com.javaboxchat.model.LoginResponse;
import com.javaboxchat.model.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.javaboxchat.model.RegisterRequest;
import com.javaboxchat.model.RegisterResponse;

import javax.swing.*;

public class MainClient {
    public static void main(String[] args) {

        try {

            Socket socket =
                    new Socket("localhost", 9999);

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

            Scanner scanner =
                    new Scanner(System.in);

            Gson gson =
                    new Gson();

            System.out.println("===== JAVA BOX CHAT =====");

            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");

            System.out.print("Chọn: ");

            int choice =
                    Integer.parseInt(
                            scanner.nextLine()
                    );

            System.out.print("Username: ");
            String username =
                    scanner.nextLine();

            System.out.print("Password: ");
            String password =
                    scanner.nextLine();

            System.out.print("Số điện thoại: ");
            String phone =
                    scanner.nextLine();


            if (choice == 1) {
                LoginRequest loginRequest =
                        new LoginRequest(
                                "LOGIN",
                                username,
                                password
                        );

                writer.println(
                        gson.toJson(loginRequest)
                );
            } else if (choice == 2) {
                RegisterRequest registerRequest =
                        new RegisterRequest(
                                "REGISTER",
                                username,
                                password,
                                phone
                        );

                writer.println(
                        gson.toJson(registerRequest)
                );
            }


            String responseJson =
                    reader.readLine();

            com.google.gson.JsonObject responseObject =
                    gson.fromJson(
                            responseJson,
                            com.google.gson.JsonObject.class
                    );

            String responseType =
                    responseObject.get("type")
                            .getAsString();

            if ("REGISTER_SUCCESS".equals(responseType)) {
                RegisterResponse response =
                        gson.fromJson(
                                responseJson,
                                RegisterResponse.class
                        );

                System.out.println(
                        response.getMessage()
                );

                socket.close();
                return;
            }

            if ("REGISTER_FAIL".equals(responseType)) {
                RegisterResponse response =
                        gson.fromJson(
                                responseJson,
                                RegisterResponse.class
                        );

                System.out.println(
                        response.getMessage()
                );

                socket.close();
                return;
            }

            LoginResponse response =
                    gson.fromJson(
                            responseJson,
                            LoginResponse.class
                    );

            if (!response.isSuccess()) {
                System.out.println(
                        response.getMessage()
                );

                socket.close();
                return;
            }

            System.out.println(
                    response.getMessage()
            );
            if ("LOGIN_BLOCKED".equals(
                    response.getType()
            )) {

                JOptionPane.showMessageDialog(
                        null,
                        "Tài khoản đã bị khóa!"
                );

                return;
            }

            // Thread nhận tin nhắn
            new Thread(() -> {

                try {

                    String json;

                    while ((json = reader.readLine()) != null) {

                        Message receiveMsg =
                                gson.fromJson(
                                        json,
                                        Message.class
                                );

                        System.out.println(
                                "\n"
                                        + receiveMsg.getSender()
                                        + ": "
                                        + receiveMsg.getContent()
                        );

                        System.out.print("Người nhận: ");
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Mất kết nối tới server!"
                    );
                }

            }).start();

            while (true) {

                System.out.print("Người nhận: ");
                String receiver =
                        scanner.nextLine();

                System.out.print("Nội dung: ");
                String content =
                        scanner.nextLine();

                String timestamp =
                        java.time.LocalDateTime.now()
                                .format(
                                        java.time.format.DateTimeFormatter.ofPattern(
                                                "HH:mm dd/MM/yyyy"
                                        )
                                );

                Message message =
                        new Message(
                                "MESSAGE",
                                username,
                                receiver,
                                content,
                                timestamp,
                                "TEXT"
                        );

                writer.println(
                        gson.toJson(message)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
