package com.javaboxchat.ui;

import javax.swing.*;
import java.awt.*;

public class CallFrame extends JFrame {

    private JLabel lblUser;

    private JLabel lblStatus;

    private JLabel lblTime;

    private JButton btnMute;

    private JButton btnEnd;

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