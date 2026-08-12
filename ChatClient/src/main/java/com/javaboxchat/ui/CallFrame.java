package com.javaboxchat.ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;

public class CallFrame extends JFrame {

    private JLabel lblUser;

    private JLabel lblStatus;

    private JLabel lblTime;

    private JButton btnMute;

    private JButton btnEnd;

    private Timer callTimer;

    private int elapsedSeconds = 0;


    public CallFrame(
            String username
    ) {

        setTitle(
                "Voice Call"
        );

        setSize(
                350,
                250
        );

        setLocationRelativeTo(
                null
        );

        setLayout(
                new BorderLayout()
        );

        JPanel center =
                new JPanel();

        center.setLayout(
                new GridLayout(
                        3,
                        1
                )
        );

        lblUser =
                new JLabel(
                        "Đang gọi: " + username,
                        SwingConstants.CENTER
                );

        lblUser.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        lblStatus =
                new JLabel(
                        "Đang kết nối...",
                        SwingConstants.CENTER
                );

        lblTime =
                new JLabel(
                        "00:00",
                        SwingConstants.CENTER
                );

        center.add(
                lblUser
        );

        center.add(
                lblStatus
        );

        center.add(
                lblTime
        );

        add(
                center,
                BorderLayout.CENTER
        );

        JPanel bottom =
                new JPanel();

        btnMute =
                new JButton(
                        "🎤 Tắt Mic"
                );

        btnEnd =
                new JButton(
                        "☎ Kết thúc"
                );

        bottom.add(
                btnMute
        );

        bottom.add(
                btnEnd
        );

        add(
                bottom,
                BorderLayout.SOUTH
        );

        setVisible(true);
    }

    public void startCallTimer() {

        System.out.println(
                "CALL TIMER START"
        );

        elapsedSeconds = 0;

        if (callTimer != null) {

            callTimer.stop();
        }

        lblTime.setText(
                "00:00"
        );

        callTimer =
                new Timer(
                        1000,
                        e -> {

                            elapsedSeconds++;

                            int minutes =
                                    elapsedSeconds / 60;

                            int seconds =
                                    elapsedSeconds % 60;

                            lblTime.setText(
                                    String.format(
                                            "%02d:%02d",
                                            minutes,
                                            seconds
                                    )
                            );
                        }
                );

        callTimer.start();
    }

    public void stopCallTimer() {

        if (callTimer != null) {

            callTimer.stop();

            callTimer = null;
        }
    }

    public JButton getBtnEnd() {

        return btnEnd;
    }

    public JButton getBtnMute() {

        return btnMute;
    }

    public void setStatus(
            String text
    ) {

        lblStatus.setText(
                text
        );
    }

}