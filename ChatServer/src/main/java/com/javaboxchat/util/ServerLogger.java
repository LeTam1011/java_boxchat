package com.javaboxchat.util;

import com.javaboxchat.ui.ServerFrame;

public class ServerLogger {

    private static ServerFrame frame;

    public static void setFrame(
            ServerFrame serverFrame
    ) {
        frame = serverFrame;
    }

    public static ServerFrame getFrame() {
        return frame;
    }

    public static void log(
            String message
    ) {

        System.out.println(message);

        if (frame != null) {

            frame.log(message);
        }
    }
}