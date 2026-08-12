package com.javaboxchat.voice;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;

public class Microphone {

    private TargetDataLine microphone;

    private volatile boolean running = false;

    private final Object lock =
            new Object();


    public void start(
            VoiceSender sender
    ) {

        synchronized (lock) {

            if (running) {

                System.out.println(
                        "Microphone is already running"
                );

                return;
            }

            running = true;
        }


        try {

            AudioFormat format =
                    AudioConfig.getFormat();

            DataLine.Info info =
                    new DataLine.Info(
                            TargetDataLine.class,
                            format
                    );

            TargetDataLine line =
                    (TargetDataLine)
                            AudioSystem.getLine(
                                    info
                            );

            synchronized (lock) {

                microphone = line;
            }

            line.open(format);

            line.start();

            System.out.println(
                    "Microphone started"
            );


            byte[] buffer =
                    new byte[
                            AudioConfig.BUFFER_SIZE
                            ];


            while (isRunning()) {

                int bytesRead =
                        line.read(
                                buffer,
                                0,
                                buffer.length
                        );

                if (bytesRead > 0
                        && isRunning()) {

                    byte[] audioData =
                            new byte[
                                    bytesRead
                                    ];

                    System.arraycopy(
                            buffer,
                            0,
                            audioData,
                            0,
                            bytesRead
                    );

                    try {

                        sender.send(
                                audioData
                        );

                    } catch (java.net.SocketException e) {

                        System.out.println(
                                "Voice connection đã đóng."
                        );

                        break;
                    }
                }
            }

        } catch (Exception e) {

            if (isRunning()) {

                e.printStackTrace();
            }

        } finally {

            closeLine();
        }
    }


    private boolean isRunning() {

        synchronized (lock) {

            return running;
        }
    }


    public void stop() {

        synchronized (lock) {

            if (!running
                    && microphone == null) {

                return;
            }

            running = false;
        }

        closeLine();
    }


    private void closeLine() {

        TargetDataLine line;

        synchronized (lock) {

            line = microphone;

            microphone = null;
        }

        if (line != null) {

            try {

                line.stop();

            } catch (Exception ignored) {
            }

            try {

                line.close();

            } catch (Exception ignored) {
            }
        }

        System.out.println(
                "Microphone stopped"
        );
    }
}