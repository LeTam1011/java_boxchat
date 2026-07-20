package com.javaboxchat.server;

import com.javaboxchat.dao.UserDAO;
import com.javaboxchat.fileserver.FileServer;
import com.javaboxchat.ui.ServerFrame;
import com.javaboxchat.util.ServerLogger;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;

public class MainServer {

    public static final int PORT = 9999;

    public static void main(String[] args) {

        ServerFrame serverFrame =
                new ServerFrame();

        ServerLogger.setFrame(
                serverFrame
        );


        UserDAO userDAO =
                new UserDAO();

        serverFrame.updateUserList(
                userDAO.getAllUsers()
        );

        File dir =
                new File("uploads");

        if (!dir.exists()) {

            dir.mkdirs();
        }

        ServerLogger.log(
                "Folder uploads ready: "
                        + dir.getAbsolutePath()
        );

        try {

            ServerSocket serverSocket =
                    new ServerSocket(PORT);

            ServerLogger.log(
                    "Server đang chạy cổng "
                            + PORT
            );

            new Thread(
                    FileServer::start
            ).start();

            while (true) {

                Socket socket =
                        serverSocket.accept();

                ServerLogger.log(
                        "Client kết nối: "
                                + socket.getInetAddress()
                );

                ClientHandler handler =
                        new ClientHandler(
                                socket
                        );

                new Thread(
                        handler
                ).start();
            }

        } catch (Exception e) {

            e.printStackTrace();

            ServerLogger.log(
                    "Lỗi Server: "
                            + e.getMessage()
            );
        }
    }
}