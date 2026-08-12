package com.javaboxchat.ui;

import com.javaboxchat.model.CallHistory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CallHistoryFrame extends JFrame {

    private final String currentUser;

    private JPanel historyPanel;

    public CallHistoryFrame(
            String currentUser,
            List<CallHistory> calls
    ) {

        this.currentUser = currentUser;

        setTitle("Lịch sử cuộc gọi");

        setSize(
                500,
                550
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        initComponents(calls);
    }


    private void initComponents(
            List<CallHistory> calls
    ) {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );


        JLabel title =
                new JLabel(
                        "📞 Lịch sử cuộc gọi"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        mainPanel.add(
                title,
                BorderLayout.NORTH
        );


        historyPanel =
                new JPanel();

        historyPanel.setLayout(
                new BoxLayout(
                        historyPanel,
                        BoxLayout.Y_AXIS
                )
        );


        if (calls == null ||
                calls.isEmpty()) {

            JLabel empty =
                    new JLabel(
                            "Chưa có lịch sử cuộc gọi."
                    );

            empty.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            14
                    )
            );

            empty.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            historyPanel.add(
                    Box.createVerticalStrut(
                            30
                    )
            );

            historyPanel.add(
                    empty
            );

        } else {

            for (CallHistory call : calls) {

                historyPanel.add(
                        createCallRow(call)
                );

                historyPanel.add(
                        Box.createVerticalStrut(
                                5
                        )
                );
            }
        }


        JScrollPane scroll =
                new JScrollPane(
                        historyPanel
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );


        mainPanel.add(
                scroll,
                BorderLayout.CENTER
        );


        setContentPane(
                mainPanel
        );
    }


    private JPanel createCallRow(
            CallHistory call
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                10,
                                5
                        )
                );


        row.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                Color.LIGHT_GRAY
                        ),

                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );


        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        80
                )
        );


        boolean outgoing =
                currentUser.equals(
                        call.getCaller()
                );


        String partner;

        if (outgoing) {

            partner =
                    call.getReceiver();

        } else {

            partner =
                    call.getCaller();
        }


        String direction;

        if (outgoing) {

            direction =
                    "📞 Gọi đi";

        } else {

            direction =
                    "📲 Cuộc gọi đến";
        }


        JLabel nameLabel =
                new JLabel(
                        direction
                                + "   "
                                + partner
                );


        nameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        int totalSeconds =
                call.getDurationSeconds();


        int minutes =
                totalSeconds / 60;


        int seconds =
                totalSeconds % 60;


        String duration =
                String.format(
                        "%02d:%02d",
                        minutes,
                        seconds
                );


        String date =
                formatDate(
                        call.getStartTime()
                );


        JLabel infoLabel =
                new JLabel(
                        "Thời lượng: "
                                + duration
                                + "    •    "
                                + date
                );


        infoLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );


        row.add(
                nameLabel,
                BorderLayout.NORTH
        );


        row.add(
                infoLabel,
                BorderLayout.CENTER
        );


        return row;
    }


    private String formatDate(
            String value
    ) {

        if (value == null) {

            return "";
        }

        try {

            LocalDateTime dateTime =
                    LocalDateTime.parse(
                            value
                                    .replace(
                                            " ",
                                            "T"
                                    )
                                    .replace(
                                            ".0",
                                            ""
                                    )
                    );


            return dateTime.format(
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy HH:mm"
                    )
            );

        } catch (Exception e) {

            return value;
        }
    }
}