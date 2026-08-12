package com.javaboxchat.voice;

import javax.sound.sampled.*;

public class Speaker {

    private SourceDataLine speaker;

    public void start() throws Exception {

        AudioFormat format =
                AudioConfig.getFormat();

        DataLine.Info info =
                new DataLine.Info(
                        SourceDataLine.class,
                        format
                );

        speaker =
                (SourceDataLine)
                        AudioSystem.getLine(info);

        speaker.open(format);

        speaker.start();

        System.out.println(
                "Speaker started"
        );
    }

    public void play(
            byte[] audioData
    ) {

        if (speaker != null) {

            speaker.write(
                    audioData,
                    0,
                    audioData.length
            );
        }
    }

    public void stop() {

        if (speaker != null) {

            speaker.drain();

            speaker.stop();

            speaker.close();

            speaker = null;
        }

        System.out.println(
                "Speaker stopped"
        );
    }
}