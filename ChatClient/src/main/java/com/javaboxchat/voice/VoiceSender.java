package com.javaboxchat.voice;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VoiceSender {

    private final Socket socket;

    private final DataOutputStream out;

    public VoiceSender(
            Socket socket
    ) throws IOException {

        this.socket = socket;

        this.out =
                new DataOutputStream(
                        socket.getOutputStream()
                );
    }

    public void send(
            byte[] data
    ) throws IOException {

        out.writeInt(
                data.length
        );

        out.write(
                data
        );

        out.flush();
    }

    public void close() {

        try {

            out.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}