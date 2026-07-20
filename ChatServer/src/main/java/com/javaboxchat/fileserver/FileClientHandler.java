package com.javaboxchat.fileserver;

import java.io.*;
import java.net.Socket;

public class FileClientHandler
        implements Runnable {

    private Socket socket;

    public FileClientHandler(
            Socket socket
    ) {

        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            DataInputStream in =
                    new DataInputStream(
                            socket.getInputStream()
                    );

            String command =
                    in.readUTF();

            switch (command) {

                case "UPLOAD":

                    uploadFile(in);

                    break;

                case "DOWNLOAD":

                    downloadFile(in);

                    break;

                default:

                    System.out.println(
                            "Unknown command: "
                                    + command
                    );

                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile(
            DataInputStream in
    ) throws Exception {

        String sender =
                in.readUTF();

        String receiver =
                in.readUTF();

        String originalName =
                in.readUTF();

        long size =
                in.readLong();

        File folder =
                new File(
                        "uploads"
                );

        if (!folder.exists()) {

            folder.mkdirs();
        }

        String serverName =
                java.util.UUID.randomUUID()
                        + "_"
                        + originalName;

        File saveFile =
                new File(
                        folder,
                        serverName
                );

        FileOutputStream fos =
                new FileOutputStream(
                        saveFile
                );

        byte[] buffer =
                new byte[4096];

        long remaining =
                size;

        while (remaining > 0) {

            int len =
                    in.read(
                            buffer,
                            0,
                            (int) Math.min(
                                    buffer.length,
                                    remaining
                            )
                    );

            if (len == -1) {

                break;
            }

            fos.write(
                    buffer,
                    0,
                    len
            );

            remaining -= len;
        }

        fos.close();

        DataOutputStream out =
                new DataOutputStream(
                        socket.getOutputStream()
                );

        out.writeUTF("OK");

        out.writeUTF(
                saveFile.getName()
        );

        out.flush();

        System.out.println(
                "UPLOAD SUCCESS : "
                        + originalName
        );

    }
    private void downloadFile(
            DataInputStream in
    )
            throws Exception {

        String serverFileName =
                in.readUTF();

        File file =
                new File(
                        "uploads",
                        serverFileName
                );

        DataOutputStream out =
                new DataOutputStream(
                        socket.getOutputStream()
                );

        if (!file.exists()) {

            out.writeLong(-1);

            out.flush();

            return;
        }

        out.writeLong(
                file.length()
        );

        FileInputStream fis =
                new FileInputStream(file);

        byte[] buffer =
                new byte[4096];

        int len;

        while ((len = fis.read(buffer)) != -1) {

            out.write(
                    buffer,
                    0,
                    len
            );
        }

        out.flush();

        fis.close();
    }
}