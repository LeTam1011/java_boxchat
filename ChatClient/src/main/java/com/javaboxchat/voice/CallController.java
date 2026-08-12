package com.javaboxchat.voice;

import com.google.gson.Gson;
import com.javaboxchat.model.CallEnd;
import com.javaboxchat.ui.CallFrame;

import javax.swing.*;
import java.io.PrintWriter;

public class CallController {

    private CallFrame frame;

    private String currentUser;

    private String partner;

    private PrintWriter writer;

    private boolean muted = false;

    private VoiceConnection connection;

    private VoiceSender sender;

    private VoiceReceiver receiver;

    private Microphone microphone;

    private boolean audioStarted = false;

    private boolean callClosed = false;

    private long callStartTime;

    private final Gson gson =
            new Gson();


    public CallController(
            String currentUser,
            String partner,
            PrintWriter writer
    ) {

        this.currentUser = currentUser;

        this.partner = partner;

        this.writer = writer;

        frame =
                new CallFrame(
                        partner
                );

        initEvents();
    }


    // =====================================================
    // MÁY NHẬN CUỘC GỌI
    // =====================================================

    public void startListening() {

        connection =
                new VoiceConnection();

        frame.setStatus(
                "Đang chờ kết nối..."
        );

        new Thread(
                () -> {

                    try {

                        connection.startListening();

                        if (callClosed) {

                            return;
                        }

                        SwingUtilities.invokeLater(() -> {

                            if (callClosed) {

                                return;
                            }

                            frame.setStatus(
                                    "Đã kết nối"
                            );


                            startVoiceStreams();

                            callStartTime =
                                    System.currentTimeMillis();

                            frame.startCallTimer();

                        });

                    } catch (Exception e) {

                        if (!callClosed) {

                            e.printStackTrace();

                            SwingUtilities.invokeLater(() -> {

                                frame.setStatus(
                                        "Kết nối thất bại"
                                );

                            });
                        }
                    }

                },
                "VoiceListener"
        ).start();
    }


    // =====================================================
    // MÁY CHỦ ĐỘNG GỌI
    // =====================================================

    public boolean connectToPeer(
            String ip,
            int port
    ) {

        try {

            connection =
                    new VoiceConnection();

            boolean connected =
                    connection.connect(
                            ip,
                            port
                    );

            if (!connected) {

                return false;
            }

            if (callClosed) {

                return false;
            }

            frame.setStatus(
                    "Đã kết nối"
            );

            startVoiceStreams();

            callStartTime =
                    System.currentTimeMillis();

            frame.startCallTimer();

            return true;

        } catch (Exception e) {

            if (!callClosed) {

                e.printStackTrace();

                frame.setStatus(
                        "Kết nối thất bại"
                );
            }

            return false;
        }
    }


    // =====================================================
    // CÁC NÚT TRÊN CALL FRAME
    // =====================================================

    private void initEvents() {

        // -------------------------------------------------
        // MUTE
        // -------------------------------------------------

        frame.getBtnMute()
                .addActionListener(e -> {

                    if (callClosed) {

                        return;
                    }

                    muted = !muted;


                    if (muted) {

                        if (microphone != null) {

                            microphone.stop();
                        }

                        frame.getBtnMute()
                                .setText(
                                        "🎤 Bật Mic"
                                );

                        frame.setStatus(
                                "Đã tắt Mic"
                        );

                    } else {

                        startAudio();

                        frame.getBtnMute()
                                .setText(
                                        "🎤 Tắt Mic"
                                );

                        frame.setStatus(
                                "Đã kết nối"
                        );
                    }

                });


        // -------------------------------------------------
        // KẾT THÚC CUỘC GỌI
        // -------------------------------------------------

        frame.getBtnEnd()
                .addActionListener(e -> {

                    if (callClosed) {

                        return;
                    }

                    System.out.println(
                            "USER ENDED CALL"
                    );


                    /*
                     * BƯỚC 1:
                     * Báo cho người bên kia
                     */

                    sendCallEnd();


                    /*
                     * BƯỚC 2:
                     * Tự đóng cuộc gọi NGAY
                     */

                    closeCall();

                });
    }


    // =====================================================
    // KHỞI ĐỘNG AUDIO
    // =====================================================

    private void startVoiceStreams() {

        if (callClosed) {

            return;
        }

        try {

            sender =
                    new VoiceSender(
                            connection.getSocket()
                    );

            receiver =
                    new VoiceReceiver(
                            connection.getSocket()
                    );


            new Thread(
                    receiver,
                    "VoiceReceiver"
            ).start();


            startAudio();

        } catch (Exception e) {

            if (!callClosed) {

                e.printStackTrace();
            }
        }
    }


    // =====================================================
    // MICROPHONE
    // =====================================================

    private synchronized void startAudio() {

        if (callClosed) {

            return;
        }

        if (muted) {

            return;
        }

        if (audioStarted) {

            return;
        }

        if (sender == null) {

            return;
        }


        audioStarted = true;

        microphone =
                new Microphone();


        new Thread(
                () -> {

                    try {

                        microphone.start(
                                sender
                        );

                    } catch (Exception e) {

                        if (!callClosed) {

                            e.printStackTrace();
                        }

                    } finally {

                        synchronized (
                                CallController.this
                        ) {

                            audioStarted = false;
                        }
                    }

                },
                "Microphone"
        ).start();
    }


    // =====================================================
    // GỬI CALL_END
    // =====================================================

    private void sendCallEnd() {

        if (writer == null) {

            return;
        }

        if (partner == null) {

            return;
        }


        long endTime =
                System.currentTimeMillis();


        int durationSeconds =
                0;


        if (callStartTime > 0) {

            durationSeconds =
                    (int) (
                            (endTime - callStartTime)
                                    / 1000
                    );
        }


        CallEnd end =
                new CallEnd(
                        "CALL_END",
                        currentUser,
                        partner,
                        durationSeconds
                );


        String json =
                gson.toJson(
                        end
                );


        writer.println(
                json
        );

        writer.flush();


        System.out.println(
                "CALL_END SENT: "
                        + currentUser
                        + " -> "
                        + partner
                        + " | duration = "
                        + durationSeconds
                        + " seconds"
        );
    }


    // =====================================================
    // ĐÓNG CUỘC GỌI
    // =====================================================

    public synchronized void closeCall() {

        /*
         * Nếu đã đóng rồi thì không làm lại lần nữa.
         */

        if (callClosed) {

            return;
        }

        callClosed = true;


        System.out.println(
                "Closing call with "
                        + partner
        );


        // -------------------------------------------------
        // 1. Tắt microphone
        // -------------------------------------------------

        muted = true;

        audioStarted = false;


        if (microphone != null) {

            try {

                microphone.stop();

            } catch (Exception e) {

                e.printStackTrace();
            }

            microphone = null;
        }


        // -------------------------------------------------
        // 2. Dừng receiver
        // -------------------------------------------------

        if (receiver != null) {

            try {

                receiver.stop();

            } catch (Exception e) {

                e.printStackTrace();
            }

            receiver = null;
        }


        // -------------------------------------------------
        // 3. Dừng timer
        // -------------------------------------------------

        if (frame != null) {

            frame.stopCallTimer();
        }


        // -------------------------------------------------
        // 4. Đóng sender
        // -------------------------------------------------

        if (sender != null) {

            try {

                sender.close();

            } catch (Exception e) {

                e.printStackTrace();
            }

            sender = null;
        }


        // -------------------------------------------------
        // 5. Đóng VoiceConnection
        // -------------------------------------------------

        if (connection != null) {

            try {

                connection.close();

            } catch (Exception e) {

                e.printStackTrace();
            }

            connection = null;
        }


        // -------------------------------------------------
        // 6. Đóng CallFrame
        // -------------------------------------------------

        if (frame != null) {

            frame.dispose();
        }


        System.out.println(
                "CALL CLOSED"
        );
    }


    // =====================================================
    // GET FRAME
    // =====================================================

    public CallFrame getFrame() {

        return frame;
    }
}