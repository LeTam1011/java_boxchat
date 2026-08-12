package com.javaboxchat.ui;

import javax.swing.*;
import java.awt.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.javaboxchat.file.FileClient;
import com.javaboxchat.model.*;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import com.javaboxchat.voice.CallController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Paths;
import java.awt.Desktop;



public class ChatFrame extends JFrame {
    private JList<String> userList;

    private JPanel chatPanel;

    private JTextField txtReceiver;

    private JTextField txtMessage;

    private JButton btnSend;

    private String currentUser;

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private Gson gson = new Gson();

    private DefaultListModel<String> userModel;

    private JTextField txtPhoneSearch;

    private JButton btnSearchFriend;

    private JButton btnAddFriend;

    private JButton btnFriendRequests;

    private String lastDate = "";

    private JButton btnFile;

    private JButton btnCall;

    private JButton btnCallHistory;

    private JButton btnAvatar;

    private JLabel lblAvatar;

    private CallController callController;

    private final java.util.Map<String, Integer>
            unreadMap =
            new java.util.HashMap<>();

    public ChatFrame(
            Socket socket,
            BufferedReader reader,
            PrintWriter writer,
            String currentUser
    ) {

        this.socket = socket;

        this.reader = reader;

        this.writer = writer;

        this.currentUser = currentUser;

        setTitle("Java Box Chat");

        setSize(900, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        loadAvatar();

        System.out.println("INIT EVENTS");

        initEvents();

        startReceiveThread();

        GetFriendsRequest request =
                new GetFriendsRequest(
                        "GET_FRIENDS",
                        currentUser
                );

        writer.println(
                gson.toJson(request)
        );

        setVisible(true);
    }

    private void initComponents() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        // Danh sách user
        userModel =
                new DefaultListModel<>();

        userList =
                new JList<>(userModel);

        userList.setCellRenderer(
                new AvatarListRenderer()
        );

        JScrollPane leftPane =
                new JScrollPane(userList);

        leftPane.setPreferredSize(
                new Dimension(
                        200,
                        0
                )

        );

        // Khung chat
        chatPanel =
                new JPanel();

        chatPanel.setBackground(
                new Color(
                        245,
                        245,
                        245
                )
        );

        chatPanel.setLayout(
                new BoxLayout(
                        chatPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane centerPane =
                new JScrollPane(chatPanel);

        // Khung gửi tin nhắn
        JPanel bottomPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                10,
                                10
                        )
                );

        txtReceiver =
                new JTextField();

        txtMessage =
                new JTextField();

        btnSend =
                new JButton(
                        "Gửi"
                );

        bottomPanel.add(
                new JLabel("Người nhận:")
        );

        bottomPanel.add(
                txtReceiver
        );

        bottomPanel.add(
                txtMessage
        );

        bottomPanel.add(
                btnSend
        );

        mainPanel.add(
                leftPane,
                BorderLayout.WEST
        );

        mainPanel.add(
                centerPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);
        JPanel topPanel =
                new JPanel(
                        new FlowLayout()
                );

        txtPhoneSearch =
                new JTextField(15);

        btnSearchFriend =
                new JButton("Tìm");

        btnAddFriend =
                new JButton("Kết bạn");

        btnAddFriend.setEnabled(false);

        btnFriendRequests =
                new JButton(
                        "Lời mời kết bạn"

                );
        System.out.println("BUTTON CREATED");

        btnFile =
                new JButton("📎");

        btnCall = new JButton("📞 Gọi");

        btnCallHistory =
                new JButton("📞 Lịch sử gọi");

        bottomPanel.add(btnFile);

        bottomPanel.add(btnCall);

        bottomPanel.add(btnCallHistory);

        btnAvatar =
                new JButton(
                        "Đổi Avatar"
                );
        lblAvatar =
                new JLabel();

        lblAvatar.setPreferredSize(
                new Dimension(
                        60,
                        60
                )
        );

        lblAvatar.setBorder(
                BorderFactory.createLineBorder(
                        Color.GRAY
                )
        );

        topPanel.add(
                lblAvatar
        );

        topPanel.add(
                btnAvatar
        );

        topPanel.add(
                btnFriendRequests
        );

        topPanel.add(
                new JLabel("SĐT:")
        );

        topPanel.add(
                txtPhoneSearch
        );

        topPanel.add(
                btnSearchFriend
        );

        topPanel.add(
                btnAddFriend
        );


        mainPanel.add(
                topPanel,
                BorderLayout.NORTH
        );
    }

    private void startReceiveThread() {

        new Thread(() -> {

            try {

                String json;

                while ((json = reader.readLine()) != null) {

                    System.out.println(
                            "Received: " + json
                    );

                    JsonObject object =
                            gson.fromJson(
                                    json,
                                    JsonObject.class
                            );

                    if (object == null ||
                            !object.has("type")) {

                        System.out.println(
                                "JSON không có type: "
                                        + json
                        );

                        continue;
                    }

                    String type =
                            object.get("type")
                                    .getAsString();


                    // =====================================================
                    // ONLINE USERS
                    // =====================================================

                    if ("ONLINE_USERS".equals(type)) {

                        GetFriendsRequest request =
                                new GetFriendsRequest(
                                        "GET_FRIENDS",
                                        currentUser
                                );

                        writer.println(
                                gson.toJson(request)
                        );

                        writer.flush();

                        continue;
                    }


                    // =====================================================
                    // HISTORY
                    // =====================================================

                    if ("HISTORY_RESPONSE".equals(type)) {

                        HistoryResponse response =
                                gson.fromJson(
                                        json,
                                        HistoryResponse.class
                                );

                        SwingUtilities.invokeLater(() -> {

                            chatPanel.removeAll();

                            lastDate = "";

                            for (Message msg :
                                    response.getMessages()) {

                                if ("FILE".equals(
                                        msg.getMessageType()
                                )) {

                                    addFileMessage(
                                            msg.getSender(),
                                            msg.getContent(),
                                            msg.getFilePath()
                                    );

                                } else {

                                    addBubbleMessage(
                                            msg.getId(),
                                            msg.getSender(),
                                            msg.getContent(),
                                            msg.getTimestamp(),
                                            msg.isRecalled()
                                    );
                                }
                            }

                            chatPanel.revalidate();
                            chatPanel.repaint();
                        });

                        continue;
                    }


                    // =====================================================
                    // SEARCH USER
                    // =====================================================

                    if ("SEARCH_USER_RESULT".equals(type)) {

                        SearchUserResponse response =
                                gson.fromJson(
                                        json,
                                        SearchUserResponse.class
                                );

                        SwingUtilities.invokeLater(() -> {

                            if (response.isFound()) {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "Tìm thấy user: "
                                                + response.getUsername()
                                );

                                btnAddFriend.setEnabled(
                                        true
                                );

                                btnAddFriend.putClientProperty(
                                        "targetUser",
                                        response.getUsername()
                                );

                            } else {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "Không tìm thấy user"
                                );

                                btnAddFriend.setEnabled(
                                        false
                                );
                            }
                        });

                        continue;
                    }


                    // =====================================================
                    // PENDING FRIEND REQUESTS
                    // =====================================================

                    if ("PENDING_REQUESTS".equals(type)) {

                        PendingRequestsResponse response =
                                gson.fromJson(
                                        json,
                                        PendingRequestsResponse.class
                                );

                        SwingUtilities.invokeLater(() -> {

                            RequestListFrame frame =
                                    new RequestListFrame(
                                            response.getRequests(),
                                            currentUser,
                                            writer
                                    );

                            frame.setVisible(true);
                        });

                        continue;
                    }


                    // =====================================================
                    // FRIENDS LIST
                    // =====================================================

                    if ("FRIENDS_LIST".equals(type)) {

                        FriendsResponse response =
                                gson.fromJson(
                                        json,
                                        FriendsResponse.class
                                );

                        SwingUtilities.invokeLater(() -> {

                            userModel.clear();

                            for (FriendInfo friend :
                                    response.getFriends()) {

                                if (friend.isOnline()) {

                                    userModel.addElement(
                                            "🟢 "
                                                    + friend.getUsername()
                                    );

                                } else {

                                    userModel.addElement(
                                            "⚫ "
                                                    + friend.getUsername()
                                    );
                                }
                            }
                        });

                        continue;
                    }


                    // =====================================================
                    // RECALL
                    // =====================================================

                    if ("RECALL_SUCCESS".equals(type)) {

                        String selectedUser =
                                txtReceiver.getText();

                        if (!selectedUser.isEmpty()) {

                            HistoryRequest request =
                                    new HistoryRequest(
                                            "HISTORY_REQUEST",
                                            currentUser,
                                            selectedUser
                                    );

                            writer.println(
                                    gson.toJson(request)
                            );

                            writer.flush();
                        }

                        continue;
                    }


                    // =====================================================
                    // ================= CALL REQUEST =====================
                    // =====================================================

                    if ("CALL_REQUEST".equals(type)) {

                        CallRequest request =
                                gson.fromJson(
                                        json,
                                        CallRequest.class
                                );

                        System.out.println(
                                "================================"
                        );

                        System.out.println(
                                "INCOMING CALL"
                        );

                        System.out.println(
                                "Caller: "
                                        + request.getCaller()
                        );

                        System.out.println(
                                "Receiver: "
                                        + request.getReceiver()
                        );

                        System.out.println(
                                "Current user: "
                                        + currentUser
                        );

                        System.out.println(
                                "================================"
                        );


                        SwingUtilities.invokeLater(() -> {

                            int option =
                                    JOptionPane.showConfirmDialog(
                                            this,

                                            request.getCaller()
                                                    + " đang gọi cho bạn.",

                                            "📞 Cuộc gọi đến",

                                            JOptionPane.YES_NO_OPTION,

                                            JOptionPane.INFORMATION_MESSAGE
                                    );

                            boolean accepted =
                                    option == JOptionPane.YES_OPTION;


                            if (accepted) {

                                System.out.println(
                                        "CALL ACCEPTED"
                                );


                                /*
                                 * QUAN TRỌNG:
                                 * Phải lưu vào biến thành viên
                                 * callController
                                 */
                                callController =
                                        new CallController(
                                                currentUser,
                                                request.getCaller(),
                                                writer
                                        );


                                callController.startListening();


                                CallResponse response =
                                        new CallResponse(
                                                "CALL_RESPONSE",
                                                request.getCaller(),
                                                currentUser,
                                                true
                                        );

                                writer.println(
                                        gson.toJson(response)
                                );

                                writer.flush();

                                System.out.println(
                                        "CALL_RESPONSE SENT: "
                                                + gson.toJson(response)
                                );

                            } else {

                                System.out.println(
                                        "CALL REJECTED"
                                );

                                CallResponse response =
                                        new CallResponse(
                                                "CALL_RESPONSE",
                                                request.getCaller(),
                                                currentUser,
                                                false
                                        );

                                writer.println(
                                        gson.toJson(response)
                                );

                                writer.flush();

                                System.out.println(
                                        "CALL_RESPONSE SENT: "
                                                + gson.toJson(response)
                                );
                            }

                        });

                        continue;
                    }


                    // =====================================================
                    // ================= CALL RESPONSE ====================
                    // =====================================================

                    if ("CALL_CONNECT_INFO".equals(type)) {

                        CallConnectInfo info =
                                gson.fromJson(
                                        json,
                                        CallConnectInfo.class
                                );

                        System.out.println(
                                "================================"
                        );

                        System.out.println(
                                "CALL CONNECT INFO"
                        );

                        System.out.println(
                                "Partner: "
                                        + info.getPartner()
                        );

                        System.out.println(
                                "IP: "
                                        + info.getIp()
                        );

                        System.out.println(
                                "Port: "
                                        + info.getPort()
                        );

                        System.out.println(
                                "================================"
                        );


                        SwingUtilities.invokeLater(() -> {

                            /*
                             * QUAN TRỌNG:
                             * Dùng biến callController của ChatFrame
                             */
                            callController =
                                    new CallController(
                                            currentUser,
                                            info.getPartner(),
                                            writer
                                    );


                            boolean connected =
                                    callController.connectToPeer(
                                            info.getIp(),
                                            info.getPort()
                                    );


                            if (!connected) {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "Không thể kết nối cuộc gọi!"
                                );

                                callController = null;

                                return;
                            }


                            System.out.println(
                                    "VOICE CONNECTED"
                            );


                            callController.getFrame()
                                    .setStatus(
                                            "Đã kết nối"
                                    );

                        });

                        continue;
                    }

                    // =====================================================
                    // ================ CALL CONNECT INFO =================
                    // =====================================================

                    if ("CALL_CONNECT_INFO".equals(type)) {

                        CallConnectInfo info =
                                gson.fromJson(
                                        json,
                                        CallConnectInfo.class
                                );

                        System.out.println(
                                "================================"
                        );

                        System.out.println(
                                "CALL CONNECT INFO"
                        );

                        System.out.println(
                                "Partner: "
                                        + info.getPartner()
                        );

                        System.out.println(
                                "IP: "
                                        + info.getIp()
                        );

                        System.out.println(
                                "Port: "
                                        + info.getPort()
                        );

                        System.out.println(
                                "================================"
                        );

                        SwingUtilities.invokeLater(() -> {

                            callController =
                                    new CallController(
                                            currentUser,
                                            info.getPartner(),
                                            writer
                                    );

                            boolean connected =
                                    callController.connectToPeer(
                                            info.getIp(),
                                            info.getPort()
                                    );

                            if (!connected) {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "Không thể kết nối cuộc gọi!"
                                );

                                callController = null;

                                return;
                            }

                            System.out.println(
                                    "VOICE CONNECTED"
                            );

                        });

                        continue;
                    }

                    // =====================================================
                    // ==================== CALL END ======================
                    // =====================================================

                    if ("CALL_END".equals(type)) {

                        CallEnd end =
                                gson.fromJson(
                                        json,
                                        CallEnd.class
                                );

                        System.out.println(
                                "CALL ENDED BY: "
                                        + end.getCaller()
                        );

                        SwingUtilities.invokeLater(() -> {

                            /*
                             * Đóng CallFrame NGAY
                             */
                            if (callController != null) {

                                callController.closeCall();

                                callController = null;
                            }


                            /*
                             * Sau khi CallFrame đã đóng
                             * mới hiện thông báo
                             */
                            JOptionPane.showMessageDialog(
                                    this,

                                    end.getCaller()
                                            + " đã ngắt kết nối cuộc gọi.",

                                    "Cuộc gọi kết thúc",

                                    JOptionPane.INFORMATION_MESSAGE
                            );

                        });

                        continue;
                    }

                    // =====================================================
                    // ================= CALL OFFLINE =====================
                    // =====================================================

                    if ("CALL_OFFLINE".equals(type)) {

                        SwingUtilities.invokeLater(() -> {

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Người dùng hiện không online."
                            );

                        });

                        continue;
                    }

                    if ("CALL_HISTORY_RESPONSE".equals(type)) {

                        CallHistoryResponse response =
                                gson.fromJson(
                                        json,
                                        CallHistoryResponse.class
                                );

                        SwingUtilities.invokeLater(() -> {

                            showCallHistory(
                                    response.getCalls()
                            );

                        });

                        continue;
                    }


                    // =====================================================
                    // ====================== MESSAGE =====================
                    // =====================================================

                    if ("MESSAGE".equals(type)) {

                        Message msg =
                                gson.fromJson(
                                        json,
                                        Message.class
                                );


                        SwingUtilities.invokeLater(() -> {

                            boolean isMine =
                                    currentUser.equals(
                                            msg.getSender()
                                    );


                            if ("FILE".equals(
                                    msg.getMessageType()
                            )) {

                                addFileMessage(
                                        msg.getSender(),
                                        msg.getContent(),
                                        msg.getFilePath()
                                );

                            } else {

                                addBubbleMessage(
                                        msg.getId(),
                                        msg.getSender(),
                                        msg.getContent(),
                                        msg.getTimestamp(),
                                        msg.isRecalled()
                                );
                            }

                        });

                        continue;
                    }


                    // =====================================================
                    // UNKNOWN TYPE
                    // =====================================================

                    System.out.println(
                            "Unknown message type: "
                                    + type
                    );
                }

            } catch (Exception e) {

                e.printStackTrace();

                System.out.println(
                        "Mất kết nối Server"
                );
            }

        }).start();
    }


    private void initEvents() {
        btnSend.addActionListener(e -> {

            try {

                String receiver =
                        txtReceiver.getText();

                String content =
                        txtMessage.getText();

                if (content.isEmpty()) {
                    return;
                }

                String timestamp =
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "HH:mm dd/MM/yyyy"
                                        )
                                );

                Message message =
                        new Message(
                                "MESSAGE",
                                currentUser,
                                receiver,
                                content,
                                timestamp,
                                "TEXT"
                        );

                writer.println(
                        gson.toJson(message)
                );

                txtMessage.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnCall.addActionListener(e -> {

            String receiver =
                    txtReceiver.getText().trim();

            if (receiver.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn người cần gọi!"
                );

                return;
            }

            CallRequest request =
                    new CallRequest(
                            "CALL_REQUEST",
                            currentUser,
                            receiver
                    );

            System.out.println(
                    "SEND CALL REQUEST: "
                            + gson.toJson(request)
            );

            writer.println(
                    gson.toJson(request)
            );

            writer.flush();
        });

        userList.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {

                            String selectedUser =
                                    userList.getSelectedValue();

                            if (selectedUser == null) {
                                return;
                            }

                            selectedUser =
                                    selectedUser
                                            .replace("🟢 ", "")
                                            .replace("⚫ ", "")
                                            .trim();

                            unreadMap.remove(
                                    selectedUser
                            );

                            refreshFriendList();

                            txtReceiver.setText(
                                    selectedUser
                            );

                            chatPanel.removeAll();
                            chatPanel.revalidate();
                            chatPanel.repaint();

                            lastDate = "";

                            HistoryRequest request =
                                    new HistoryRequest(
                                            "HISTORY_REQUEST",
                                            currentUser,
                                            selectedUser
                                    );

                            writer.println(
                                    gson.toJson(request)
                            );
                            txtMessage.requestFocus();
                        }
                    }
                }
        );
        txtMessage.addActionListener(e -> {
            btnSend.doClick();
        });
        btnSearchFriend.addActionListener(e -> {

            String phone =
                    txtPhoneSearch.getText();

            SearchUserRequest request =
                    new SearchUserRequest(
                            "SEARCH_USER",
                            phone
                    );

            writer.println(
                    gson.toJson(request)
            );
        });
        btnAddFriend.addActionListener(e -> {

            String targetUser =
                    (String) btnAddFriend.getClientProperty(
                            "targetUser"
                    );

            if (targetUser == null) {
                return;
            }

            FriendRequestMessage request =
                    new FriendRequestMessage(
                            "FRIEND_REQUEST",
                            currentUser,
                            targetUser
                    );

            String json =
                    gson.toJson(request);

            System.out.println(
                    "Send Friend Request: "
                            + json
            );

            writer.println(json);
        });
        btnFriendRequests.addActionListener(e -> {

            System.out.println(
                    "CLICK FRIEND REQUESTS"
            );

            PendingRequestsRequest request =
                    new PendingRequestsRequest(
                            "GET_PENDING_REQUESTS",
                            currentUser
                    );

            String json =
                    gson.toJson(request);

            System.out.println(
                    "SEND = " + json
            );

            writer.println(json);
        });
        btnFile.addActionListener(e -> {

            if (txtReceiver.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn người nhận!"
                );

                return;
            }

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showOpenDialog(this);

            if (result != JFileChooser.APPROVE_OPTION) {

                return;
            }

            File file =
                    chooser.getSelectedFile();

            try {

                String filePath =
                        FileClient.upload(
                                currentUser,
                                txtReceiver.getText(),
                                file
                        );

                if (filePath == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Upload thất bại!"
                    );

                    return;
                }

                UploadFileMessage message =
                        new UploadFileMessage(
                                "FILE_MESSAGE",
                                currentUser,
                                txtReceiver.getText(),
                                file.getName(),
                                filePath
                        );

                writer.println(
                        gson.toJson(message)
                );

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể gửi file!"
                );
            }
        });
        btnAvatar.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showOpenDialog(this);

            if (result !=
                    JFileChooser.APPROVE_OPTION) {

                return;
            }

            File file =
                    chooser.getSelectedFile();

            try {

                File avatarFolder =
                        new File(
                                "avatars"
                        );

                if (!avatarFolder.exists()) {

                    avatarFolder.mkdirs();
                }

                String extension =
                        file.getName()
                                .substring(
                                        file.getName()
                                                .lastIndexOf(".")
                                );

                String avatarName =
                        currentUser
                                + extension;

                Files.copy(
                        file.toPath(),
                        Paths.get(
                                avatarFolder.getAbsolutePath(),
                                avatarName
                        ),
                        StandardCopyOption.REPLACE_EXISTING
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Đổi avatar thành công!"
                );
                loadAvatar();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });
        btnCallHistory.addActionListener(e -> {

            requestCallHistory();

        });
    }
    private void refreshFriendList() {

        for (int i = 0;
             i < userModel.size();
             i++) {

            String user =
                    userModel.get(i);

            String username =
                    user
                            .replace("🟢 ", "")
                            .replace("⚫ ", "")
                            .replaceAll(
                                    "\\(\\d+\\)",
                                    ""
                            )
                            .trim();

            int unread =
                    unreadMap.getOrDefault(
                            username,
                            0
                    );

            String icon =
                    user.startsWith("🟢")
                            ? "🟢 "
                            : "⚫ ";

            if (unread > 0) {

                userModel.set(
                        i,
                        icon
                                + username
                                + " ("
                                + unread
                                + ")"
                );

            } else {

                userModel.set(
                        i,
                        icon
                                + username
                );
            }
        }
    }


    private void addBubbleMessage(
            int messageId,
            String sender,
            String content,
            String timestamp,
            boolean recalled
    ) {
        if (timestamp == null ||
                timestamp.length() < 6) {

            timestamp = "--:-- 00/00/0000";
        }

        String date =
                timestamp.substring(6);

        if (!date.equals(lastDate)) {

            addDateSeparator(date);

            lastDate = date;
        }

        boolean isMine =
                sender.equals(currentUser);

        JPanel wrapper =
                new JPanel(
                        new FlowLayout(
                                isMine
                                        ? FlowLayout.RIGHT
                                        : FlowLayout.LEFT
                        )
                );

        wrapper.setOpaque(false);

        wrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        120
                )
        );

        JPanel bubble =
                new JPanel();

        bubble.putClientProperty(
                "messageId",
                messageId
        );

        bubble.setLayout(
                new BoxLayout(
                        bubble,
                        BoxLayout.Y_AXIS
                )
        );

        bubble.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220,220,220)
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        bubble.setOpaque(true);

        if (isMine) {

            bubble.setBackground(
                    new Color(
                            220,
                            248,
                            198
                    )
            );

        } else {

            bubble.setBackground(
                    Color.WHITE
            );
        }

        if (recalled) {

            content =
                    "<i>[Tin nhắn đã được thu hồi]</i>";

        }

        JLabel lblMessage =
                new JLabel(
                        "<html>"
                                + content
                                + "</html>"
                );

        lblMessage.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        String time =
                timestamp.length() >= 5
                        ? timestamp.substring(0, 5)
                        : "--:--";

        JLabel lblTime =
                new JLabel(time);

        lblTime.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        lblTime.setForeground(
                Color.GRAY
        );

        bubble.add(lblMessage);
        bubble.add(Box.createVerticalStrut(3));
        bubble.add(lblTime);

        if (isMine && !recalled) {

            JPopupMenu popup =
                    new JPopupMenu();

            JMenuItem recallItem =
                    new JMenuItem(
                            "Thu hồi"
                    );

            recallItem.addActionListener(e -> {

                RecallMessageRequest request =
                        new RecallMessageRequest(
                                "RECALL_MESSAGE",
                                messageId
                        );

                writer.println(
                        gson.toJson(request)
                );

            });

            popup.add(recallItem);

            bubble.setComponentPopupMenu(
                    popup
            );
        }

        wrapper.add(bubble);

        chatPanel.add(wrapper);

        chatPanel.revalidate();
        chatPanel.repaint();

        SwingUtilities.invokeLater(() -> {

            JScrollBar bar =
                    ((JScrollPane)
                            chatPanel.getParent()
                                    .getParent())
                            .getVerticalScrollBar();

            bar.setValue(
                    bar.getMaximum()
            );
        });
    }

    private void addDateSeparator(
            String date
    ) {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        JLabel lbl =
                new JLabel(
                        "──────── "
                                + date
                                + " ────────"
                );

        lbl.setForeground(
                Color.GRAY
        );

        panel.add(lbl);

        chatPanel.add(panel);
    }

    private void addFileMessage(
            String sender,
            String fileName,
            String filePath
    ) {

        boolean isMine =
                sender.equals(currentUser);

        JPanel wrapper =
                new JPanel(
                        new FlowLayout(
                                isMine
                                        ? FlowLayout.RIGHT
                                        : FlowLayout.LEFT
                        )
                );

        wrapper.setOpaque(false);

        JButton btnFile =
                new JButton(
                        "📎 " + fileName
                );

        btnFile.addActionListener(e -> {

            try {

                File file =
                        FileClient.download(
                                filePath
                        );

                if (file == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Không tìm thấy file trên Server!"
                    );

                    return;
                }

                Desktop.getDesktop()
                        .open(file);

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể mở file!"
                );
            }
        });

        wrapper.add(btnFile);

        chatPanel.add(wrapper);

        chatPanel.revalidate();
        chatPanel.repaint();
    }
    private void loadAvatar() {

        try {

            File avatarFolder =
                    new File(
                            "avatars"
                    );

            if (!avatarFolder.exists()) {
                return;
            }

            File[] files =
                    avatarFolder.listFiles();

            if (files == null) {
                return;
            }

            for (File file : files) {

                if (file.getName().startsWith(
                        currentUser + "."
                )) {

                    ImageIcon icon =
                            new ImageIcon(
                                    file.getAbsolutePath()
                            );

                    Image image =
                            icon.getImage()
                                    .getScaledInstance(
                                            60,
                                            60,
                                            Image.SCALE_SMOOTH
                                    );

                    lblAvatar.setIcon(
                            new ImageIcon(image)
                    );

                    return;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void requestCallHistory() {

        CallHistoryRequest request =
                new CallHistoryRequest(
                        "CALL_HISTORY_REQUEST",
                        currentUser
                );

        writer.println(
                gson.toJson(request)
        );

        writer.flush();

        System.out.println(
                "REQUEST CALL HISTORY: "
                        + currentUser
        );
    }

    private void showCallHistory(
            List<CallHistory> calls
    ) {

        CallHistoryFrame frame =
                new CallHistoryFrame(
                        currentUser,
                        calls
                );

        frame.setVisible(true);
    }

    public JPanel getChatPanel() {
        return chatPanel;
    }

    public JTextField getTxtReceiver() {
        return txtReceiver;
    }

    public JTextField getTxtMessage() {
        return txtMessage;
    }

    public JButton getBtnSend() {
        return btnSend;
    }


}
