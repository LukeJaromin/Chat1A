package com.jaromin.message;

import com.jaromin.client.IgnoreHeaderObjectInputStream;
import com.jaromin.message.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageReader {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private IgnoreHeaderObjectInputStream inputStream;

    public MessageReader(Socket socket) {
        try {
            inputStream = new IgnoreHeaderObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Creating input stream failed: " + e.getMessage());
        }
    }

    public void read() {
        Message message;
        try {
            while ((message = (Message) inputStream.readObject()) != null) {
                if (message.getAction() == Action.DOWNLOAD_FILE){
                    System.out.println("Downloading file to: " + message.getText());
                    try (FileOutputStream fileOutputStream = new FileOutputStream(message.getText())) {
                        fileOutputStream.write(message.getTransferredFile().getFileBytesArray());
                        System.out.println("File saved :" + message.getText());
                    } catch (IOException e){
                        logger.log(Level.INFO, "Error on saving file :" + e.getMessage());
                    }
                } else {
                    System.out.println(message.getText());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Read message failed: " + e.getMessage());
        }
    }
}
