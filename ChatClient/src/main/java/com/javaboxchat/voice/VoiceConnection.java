package com.javaboxchat.voice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VoiceConnection {

    public static final int PORT = 10000;

    private ServerSocket serverSocket;

    private Socket socket;


    /**
     * Máy nhận cuộc gọi:
     * Mở ServerSocket và chờ máy gọi kết nối.
     */
    public void startListening() {

        try {

            serverSocket =
                    new ServerSocket(PORT);

            System.out.println(
                    "Voice listener started on port "
                            + PORT
            );

            socket =
                    serverSocket.accept();

            System.out.println(
                    "Voice connection accepted!"
            );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    /**
     * Máy gọi:
     * Kết nối trực tiếp tới IP của người nhận.
     */
    public boolean connect(
            String ip,
            int port
    ) {

        try {

            socket =
                    new Socket(
                            ip,
                            port
                    );

            System.out.println(
                    "Connected to voice peer: "
                            + ip
                            + ":"
                            + port
            );

            return true;

        } catch (IOException e) {

            e.printStackTrace();

            return false;
        }
    }


    public Socket getSocket() {

        return socket;
    }


    public boolean isConnected() {

        return socket != null
                && socket.isConnected()
                && !socket.isClosed();
    }


    public void close() {

        try {

            if (socket != null) {

                socket.close();
            }

            if (serverSocket != null) {

                serverSocket.close();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}