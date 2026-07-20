package com.javaboxchat.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AvatarListRenderer
        extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {

        JLabel label =
                (JLabel) super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

        String text =
                value.toString();

        String username =
                text.replace("🟢 ", "")
                        .replace("⚫ ", "")
                        .replaceAll("\\(\\d+\\)", "")
                        .trim();

        File avatarFolder =
                new File("avatars");

        if (avatarFolder.exists()) {

            File[] files =
                    avatarFolder.listFiles();

            if (files != null) {

                for (File file : files) {

                    if (file.getName()
                            .startsWith(
                                    username + "."
                            )) {

                        ImageIcon icon =
                                new ImageIcon(
                                        file.getAbsolutePath()
                                );

                        Image image =
                                icon.getImage()
                                        .getScaledInstance(
                                                32,
                                                32,
                                                Image.SCALE_SMOOTH
                                        );

                        label.setIcon(
                                new ImageIcon(image)
                        );

                        break;
                    }
                }
            }
        }

        return label;
    }
}