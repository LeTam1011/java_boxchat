package com.javaboxchat.server;

import com.google.gson.Gson;
import com.javaboxchat.dao.UserDAO;
import com.javaboxchat.model.OnlineUsersMessage;
import com.javaboxchat.util.ServerLogger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {

    private static final ConcurrentHashMap<String, ClientHandler>
            clients = new ConcurrentHashMap<>();

    public static void addClient(
            String username,
            ClientHandler handler
    ) {

        System.out.println(
                "UPDATE USER LIST"
        );

        System.out.println(
                "LOGIN = [" + username + "]"
        );

        clients.put(
                username,
                handler
        );

        if (ServerLogger.getFrame() != null) {

            ServerLogger.getFrame()
                    .updateUserList(
                            new UserDAO()
                                    .getAllUsers()
                    );
        }
    }

    public static void removeClient(
            String username
    ) {

        clients.remove(
                username
        );

        if (ServerLogger.getFrame() != null) {

            ServerLogger.getFrame()
                    .updateUserList(
                            new UserDAO()
                                    .getAllUsers()
                    );
        }
    }

    public static ClientHandler getClient(
            String username
    ) {
        return clients.get(username);
    }

    public static List<String> getOnlineUsers() {

        return new ArrayList<>(
                clients.keySet()
        );
    }

    public static void printOnlineUsers() {

        System.out.println("===== ONLINE =====");

        clients.forEach((username, handler) -> {
            System.out.println(username);
        });

        System.out.println("==================");
    }

    public static void broadcastOnlineUsers() {

        System.out.println(
                "Broadcast online users: "
                        + getOnlineUsers()
        );

        OnlineUsersMessage msg =
                new OnlineUsersMessage(
                        "ONLINE_USERS",
                        getOnlineUsers()
                );

        Gson gson = new Gson();

        String json = gson.toJson(msg);

        System.out.println(
                "Sending JSON: " + json
        );

        clients.forEach((u, handler) -> {
            handler.sendMessage(json);
        });
    }
    public static boolean isOnline(
            String username
    ) {
        return clients.containsKey(username);
    }
}