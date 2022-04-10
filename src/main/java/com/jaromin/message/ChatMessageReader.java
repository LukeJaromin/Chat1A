package com.jaromin.message;

import com.jaromin.client.IgnoreHeaderObjectInputStream;
import com.jaromin.message.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ChatMessageReader {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Consumer<Message> onMessage;
    private IgnoreHeaderObjectInputStream reader;
    private Runnable onClose;

    public ChatMessageReader(Socket socket, Consumer<Message> onMessage, Runnable onClose) {
        this.onMessage = onMessage;
        this.onClose = onClose;
        try {
            reader = new IgnoreHeaderObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        Message message;
        try {
            while ((message = (Message) reader.readObject()) != null) {
                onMessage.accept(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (onClose != null) {
                onClose.run();
            }
        }
    }
}
