package com.javaboxchat.voice;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class VoiceReceiver
        implements Runnable {

    private final Socket socket;

    private final DataInputStream in;

    private final Speaker speaker;

    private volatile boolean running = true;


    public VoiceReceiver(
            Socket socket
    ) throws IOException {

        this.socket = socket;

        this.in =
                new DataInputStream(
                        socket.getInputStream()
                );

        this.speaker =
                new Speaker();

        try {

            speaker.start();

        } catch (Exception e) {

            throw new IOException(
                    "Không thể mở speaker",
                    e
            );
        }
    }


    @Override
    public void run() {

        try {

            while (running) {

                int length =
                        in.readInt();

                if (length <= 0) {

                    continue;
                }

                byte[] audioData =
                        new byte[length];

                in.readFully(
                        audioData
                );

                if (running) {

                    speaker.play(
                            audioData
                    );
                }
            }

        } catch (java.io.EOFException e) {

            System.out.println(
                    "Đầu bên kia đã đóng cuộc gọi."
            );

        } catch (IOException e) {

            if (running) {

                e.printStackTrace();
            }

        } finally {

            stop();
        }
    }


    public void stop() {

        if (!running) {

            return;
        }

        running = false;

        try {

            in.close();

        } catch (Exception ignored) {
        }

        speaker.stop();
    }
}