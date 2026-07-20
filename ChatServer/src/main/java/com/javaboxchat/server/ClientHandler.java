package com.javaboxchat.server;

import com.google.gson.Gson;
import com.javaboxchat.dao.MessageDAO;
import com.javaboxchat.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.javaboxchat.model.Message;
import com.javaboxchat.model.CallRequest;
import com.javaboxchat.model.CallResponse;
import com.javaboxchat.model.CallEnd;

import com.javaboxchat.util.ServerLogger;

import com.javaboxchat.dao.UserDAO;
import com.javaboxchat.dao.FriendDAO;


public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    private final UserDAO userDAO = new UserDAO();

    private final MessageDAO messageDAO =
            new MessageDAO();

    private final FriendDAO friendDAO =
            new FriendDAO();

    private final Gson gson = new Gson();


    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {

            reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            String firstRequestJson =
                    reader.readLine();

            com.google.gson.JsonObject request =
                    gson.fromJson(
                            firstRequestJson,
                            com.google.gson.JsonObject.class
                    );

            String type =
                    request.get("type").getAsString();

            if ("REGISTER".equals(type)) {
                RegisterRequest registerRequest =
                        gson.fromJson(
                                firstRequestJson,
                                RegisterRequest.class
                        );

                if (userDAO.phoneExists(
                        registerRequest.getPhone()
                )) {

                    RegisterResponse response =
                            new RegisterResponse(
                                    "REGISTER_FAIL",
                                    false,
                                    "Số điện thoại đã được sử dụng!"
                            );

                    writer.println(
                            gson.toJson(response)
                    );

                    socket.close();
                    return;
                }

                if (userDAO.phoneExists(
                        registerRequest.getPhone()
                )) {

                    RegisterResponse response =
                            new RegisterResponse(
                                    "REGISTER_FAIL",
                                    false,
                                    "Số điện thoại đã được sử dụng!"
                            );

                    writer.println(
                            gson.toJson(response)
                    );

                    socket.close();
                    return;
                }

                boolean success =
                        userDAO.register(
                                registerRequest.getUsername(),
                                registerRequest.getPassword(),
                                registerRequest.getPhone()
                        );

                RegisterResponse response =
                        new RegisterResponse(
                                success
                                        ? "REGISTER_SUCCESS"
                                        : "REGISTER_FAIL",
                                success,
                                success
                                        ? "Đăng ký thành công"
                                        : "Username đã tồn tại"
                        );

                writer.println(
                        gson.toJson(response)
                );

                socket.close();
                return;
            }

            LoginRequest loginRequest =
                    gson.fromJson(
                            firstRequestJson,
                            LoginRequest.class
                    );

            if (userDAO.isBlocked(
                    loginRequest.getUsername()
            )) {

                writer.println(
                        "{\"type\":\"LOGIN_BLOCKED\"}"
                );

                socket.close();
                return;
            }

            boolean loginSuccess =
                    userDAO.login(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );

            if (!loginSuccess) {
                LoginResponse response =
                        new LoginResponse(
                                "LOGIN_FAIL",
                                false,
                                "Sai tài khoản hoặc mật khẩu"
                        );

                writer.println(
                        gson.toJson(response)
                );

                socket.close();
                return;
            }

            username =
                    loginRequest.getUsername();

            LoginResponse response =
                    new LoginResponse(
                            "LOGIN_SUCCESS",
                            true,
                            "Đăng nhập thành công"
                    );

            writer.println(
                    gson.toJson(response)
            );
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            ClientManager.addClient(
                    username,
                    this
            );
            ServerLogger.getFrame()
                    .updateUserList(
                            new UserDAO()
                                    .getAllUsers()
                    );

            ServerLogger.log(
                    username
                            + " đã đăng nhập"
            );
            ClientManager.broadcastOnlineUsers();



            System.out.println(
                    username + " đã online"
            );

            ClientManager.printOnlineUsers();

            String json;

            while ((json = reader.readLine()) != null) {

                System.out.println(
                        "JSON nhận được: " + json
                );
                System.out.println(
                        "Client nhận: "
                                + json
                );

                com.google.gson.JsonObject object =
                        gson.fromJson(
                                json,
                                com.google.gson.JsonObject.class
                        );

                String typeRequest =
                        object.get("type")
                                .getAsString();

// Lấy lịch sử chat
                if ("HISTORY_REQUEST".equals(typeRequest)) {
                    System.out.println(
                            "Đang lấy lịch sử chat"
                    );


                    HistoryRequest requestHistory =
                            gson.fromJson(
                                    json,
                                    HistoryRequest.class
                            );

                    HistoryResponse historyResponse =
                            new HistoryResponse(
                                    "HISTORY_RESPONSE",
                                    messageDAO.getConversation(
                                            requestHistory.getSender(),
                                            requestHistory.getReceiver()
                                    )
                            );
                    System.out.println(
                            "Số tin nhắn: "
                                    + messageDAO.getConversation(
                                    requestHistory.getSender(),
                                    requestHistory.getReceiver()
                            ).size()
                    );

                    writer.println(
                            gson.toJson(historyResponse)
                    );

                    continue;
                }

// Tin nhắn chat
                if ("CALL_REQUEST".equals(typeRequest)) {

                    CallRequest callRequest =
                            gson.fromJson(
                                    json,
                                    CallRequest.class
                            );

                    ClientHandler target =
                            ClientManager.getClient(
                                    callRequest.getReceiver()
                            );

                    if (target != null) {

                        target.sendMessage(
                                gson.toJson(request)
                        );

                    } else {

                        CallResponse callResponse =
                                new CallResponse(
                                        "CALL_OFFLINE",
                                        callRequest.getCaller(),
                                        callRequest.getReceiver(),
                                        false
                                );

                        sendMessage(
                                gson.toJson(callResponse)
                        );
                    }

                    continue;
                }
                if ("CALL_RESPONSE".equals(typeRequest)) {

                    CallResponse callResponse =
                            gson.fromJson(
                                    json,
                                    CallResponse.class
                            );

                    ClientHandler caller =
                            ClientManager.getClient(
                                    callResponse.getCaller()
                            );

                    if (caller != null) {

                        caller.sendMessage(
                                gson.toJson(callResponse)
                        );
                    }

                    continue;
                }
                if ("CALL_END".equals(typeRequest)) {

                    CallEnd end =
                            gson.fromJson(
                                    json,
                                    CallEnd.class
                            );

                    ClientHandler target =
                            ClientManager.getClient(
                                    end.getReceiver()
                            );

                    if (target != null) {

                        target.sendMessage(
                                gson.toJson(end)
                        );
                    }

                    continue;
                }
                if ("MESSAGE".equals(typeRequest)) {

                    System.out.println(
                            "MESSAGE RECEIVED FROM CLIENT = "
                                    + json
                    );

                    Message msg =
                            gson.fromJson(
                                    json,
                                    Message.class
                            );

                    msg.setMessageType(
                            "TEXT"
                    );

                    ServerLogger.log(
                            msg.getSender()
                                    + " -> "
                                    + msg.getReceiver()
                                    + ": "
                                    + msg.getContent()
                    );

                    int messageId =
                            messageDAO.saveMessage(
                                    msg.getSender(),
                                    msg.getReceiver(),
                                    msg.getContent(),
                                    msg.getTimestamp(),
                                    msg.getMessageType(),
                                    null
                            );

                    msg.setId(messageId);

                    sendMessage(
                            gson.toJson(msg)
                    );

                    ClientHandler target =
                            ClientManager.getClient(
                                    msg.getReceiver()
                            );

                    if (target != null) {

                        target.sendMessage(
                                gson.toJson(msg)
                        );
                    }

                    continue;
                }
                if ("FILE_MESSAGE".equals(typeRequest)) {

                    UploadFileMessage fileMsg =
                            gson.fromJson(
                                    json,
                                    UploadFileMessage.class
                            );

                    String timestamp =
                            java.time.LocalDateTime.now()
                                    .format(
                                            java.time.format.DateTimeFormatter.ofPattern(
                                                    "HH:mm dd/MM/yyyy"
                                            )
                                    );

                    Message msg =
                            new Message(
                                    "MESSAGE",
                                    fileMsg.getSender(),
                                    fileMsg.getReceiver(),
                                    fileMsg.getFileName(),
                                    timestamp,
                                    "FILE"
                            );

                    // Lưu đường dẫn file do FileServer trả về
                    msg.setFilePath(
                            fileMsg.getFilePath()
                    );

                    int messageId =
                            messageDAO.saveMessage(
                                    msg.getSender(),
                                    msg.getReceiver(),
                                    msg.getContent(),
                                    msg.getTimestamp(),
                                    msg.getMessageType(),
                                    msg.getFilePath()
                            );

                    msg.setId(messageId);

                    sendMessage(
                            gson.toJson(msg)
                    );

                    ClientHandler target =
                            ClientManager.getClient(
                                    msg.getReceiver()
                            );

                    if (target != null) {

                        target.sendMessage(
                                gson.toJson(msg)
                        );
                    }

                    continue;
                }
                if ("SEARCH_USER".equals(typeRequest)) {

                    SearchUserRequest searchRequest =
                            gson.fromJson(
                                    json,
                                    SearchUserRequest.class
                            );

                    String foundUser =
                            userDAO.findUserByPhone(
                                    searchRequest.getPhone()
                            );

                    SearchUserResponse searchResponse =
                            new SearchUserResponse(
                                    "SEARCH_USER_RESULT",
                                    foundUser != null,
                                    foundUser
                            );

                    writer.println(
                            gson.toJson(searchResponse)
                    );

                    continue;
                }
                if ("FRIEND_REQUEST".equals(typeRequest)) {

                    FriendRequestMessage friendRequest =
                            gson.fromJson(
                                    json,
                                    FriendRequestMessage.class
                            );
                    ServerLogger.log(
                            friendRequest.getSender()
                                    + " gửi lời mời kết bạn tới "
                                    + friendRequest.getReceiver()
                    );

                    System.out.println(
                            "Sender = " + friendRequest.getSender()
                    );

                    System.out.println(
                            "Receiver = " + friendRequest.getReceiver()
                    );

                    friendDAO.sendRequest(
                            friendRequest.getSender(),
                            friendRequest.getReceiver()
                    );

                    continue;
                }
                if ("GET_PENDING_REQUESTS".equals(typeRequest)) {
                    System.out.println(
                            "ĐÃ NHẬN GET_PENDING_REQUESTS"
                    );

                    PendingRequestsRequest pendingRequest =
                            gson.fromJson(
                                    json,
                                    PendingRequestsRequest.class
                            );

                    System.out.println(
                            "Username = "
                                    + pendingRequest.getUsername()
                    );

                    System.out.println(
                            friendDAO.getPendingRequests(
                                    pendingRequest.getUsername()
                            )
                    );

                    PendingRequestsResponse pendingResponse =
                            new PendingRequestsResponse(
                                    "PENDING_REQUESTS",
                                    friendDAO.getPendingRequests(
                                            pendingRequest.getUsername()
                                    )
                            );

                    System.out.println(
                            gson.toJson(pendingResponse)
                    );

                    writer.println(
                            gson.toJson(pendingResponse)
                    );

                    continue;
                }
                if ("ACCEPT_FRIEND".equals(typeRequest)) {

                    AcceptFriendRequest acceptRequest =
                            gson.fromJson(
                                    json,
                                    AcceptFriendRequest.class
                            );
                    ServerLogger.log(
                            acceptRequest.getReceiver()
                                    + " đã chấp nhận kết bạn với "
                                    + acceptRequest.getSender()
                    );

                    friendDAO.acceptRequest(
                            acceptRequest.getSender(),
                            acceptRequest.getReceiver()
                    );

                    System.out.println(
                            acceptRequest.getReceiver()
                                    + " đã chấp nhận "
                                    + acceptRequest.getSender()
                    );

                    continue;
                }
                if ("GET_FRIENDS".equals(typeRequest)) {

                    GetFriendsRequest friendsRequest =
                            gson.fromJson(
                                    json,
                                    GetFriendsRequest.class
                            );

                    List<String> friends =
                            friendDAO.getFriends(
                                    friendsRequest.getUsername()
                            );

                    System.out.println(
                            "Friends = " + friends
                    );

                    List<FriendInfo> result =
                            new ArrayList<>();

                    for (String friend : friends) {

                        result.add(
                                new FriendInfo(
                                        friend,
                                        ClientManager.isOnline(friend)
                                )
                        );
                    }

                    FriendsResponse friendsResponse =
                            new FriendsResponse(
                                    "FRIENDS_LIST",
                                    result
                            );

                    writer.println(
                            gson.toJson(friendsResponse)
                    );

                    continue;
                }
                if ("RECALL_MESSAGE".equals(typeRequest)) {

                    RecallMessageRequest recallRequest =
                            gson.fromJson(
                                    json,
                                    RecallMessageRequest.class
                            );
                    System.out.println(
                            "Thu hồi tin nhắn ID = "
                                    + recallRequest.getMessageId()
                    );

                    messageDAO.recallMessage(
                            recallRequest.getMessageId()
                    );

                    writer.println(
                            "{\"type\":\"RECALL_SUCCESS\"}"
                    );

                    continue;
                }

            }
        } catch (Exception e) {

            if (username != null) {

                ServerLogger.log(
                        username
                                + " đã đăng xuất"
                );

                ClientManager.removeClient(
                        username
                );


                ClientManager.broadcastOnlineUsers();
            }
        }
    }

    public void sendMessage(
            String message
    ) {
        writer.println(message);
    }

}
