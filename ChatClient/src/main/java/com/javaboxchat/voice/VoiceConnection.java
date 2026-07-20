package com.javaboxchat.voice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VoiceConnection {

    private static final int PORT = 10000;

    private ServerSocket serverSocket;

    private Socket socket;

    public void startServer() {

        new Thread(() -> {

            try {

                serverSocket =
                        new ServerSocket(PORT);

                System.out.println(
                        "Voice Server Started..."
                );

                socket =
                        serverSocket.accept();

                System.out.println(
                        "Voice Connected!"
                );

            } catch (IOException e) {

                e.printStackTrace();
            }

        }).start();
    }

    public boolean connect(
            String ip
    ) {

        try {

            socket =
                    new Socket(
                            ip,
                            PORT
                    );

            System.out.println(
                    "Connected To "
                            + ip
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public Socket getSocket() {

        return socket;
    }

}