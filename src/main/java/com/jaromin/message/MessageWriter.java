package com.jaromin.message;

import com.jaromin.client.IgnoreHeaderObjectOutputStream;
import com.jaromin.message.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageWriter {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private IgnoreHeaderObjectOutputStream objectOutputStream;
    public MessageWriter(Socket clientSocket) {
        try {
            objectOutputStream = new IgnoreHeaderObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Creating output stream failed: " + e.getMessage());
        }
    }

    public void write (Message message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
