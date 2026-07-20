package com.javaboxchat.fileserver;

import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    public static final int PORT = 10001;

    public static void start() {

        try {

            ServerSocket serverSocket =
                    new ServerSocket(PORT);

            System.out.println(
                    "===== FILE SERVER STARTED ====="
            );

            while (true) {

                Socket socket =
                        serverSocket.accept();

                new Thread(
                        new FileClientHandler(socket)
                ).start();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}