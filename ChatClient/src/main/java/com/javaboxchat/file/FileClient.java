package com.javaboxchat.file;

import java.io.*;
import java.net.Socket;

public class FileClient {

    public static String upload(
            String sender,
            String receiver,
            File file
    ) {

        try {

            Socket socket =
                    new Socket(
                            "192.168.1.10",
                            10001
                    );

            DataOutputStream out =
                    new DataOutputStream(
                            socket.getOutputStream()
                    );

            DataInputStream in =
                    new DataInputStream(
                            socket.getInputStream()
                    );

            out.writeUTF("UPLOAD");

            out.writeUTF(sender);

            out.writeUTF(receiver);

            out.writeUTF(file.getName());

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

            String status =
                    in.readUTF();

            if (!"OK".equals(status)) {

                fis.close();

                socket.close();

                return null;
            }

            String filePath =
                    in.readUTF();

            fis.close();

            socket.close();

            return filePath;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static File download(
            String serverFileName
    ) {

        try {

            Socket socket =
                    new Socket(
                            "192.168.1.10",
                            10001
                    );

            DataOutputStream out =
                    new DataOutputStream(
                            socket.getOutputStream()
                    );

            DataInputStream in =
                    new DataInputStream(
                            socket.getInputStream()
                    );

            out.writeUTF("DOWNLOAD");

            out.writeUTF(serverFileName);

            out.flush();

            long fileSize =
                    in.readLong();

            if (fileSize == -1) {

                socket.close();

                return null;
            }

            File folder =
                    new File("downloads");

            if (!folder.exists()) {

                folder.mkdirs();
            }

            File saveFile =
                    new File(
                            folder,
                            serverFileName
                    );

            FileOutputStream fos =
                    new FileOutputStream(
                            saveFile
                    );

            byte[] buffer =
                    new byte[4096];

            long remaining =
                    fileSize;

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

            socket.close();

            return saveFile;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

}