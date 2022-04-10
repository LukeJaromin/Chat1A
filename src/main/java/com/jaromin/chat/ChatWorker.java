package com.jaromin.chat;

import com.jaromin.client.IgnoreHeaderObjectOutputStream;
import com.jaromin.message.ChatMessageReader;
import com.jaromin.message.Message;
import com.jaromin.history.HistoryWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class ChatWorker implements Runnable {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Socket socket;
    private final ChatWorkers chatWorkers;
    private final IgnoreHeaderObjectOutputStream outputStream;
    private final HistoryWriter historyWriter = new HistoryWriter();

    public ChatWorker(Socket socket, ChatWorkers chatWorkers) throws IOException {
        this.socket = socket;
        this.chatWorkers = chatWorkers;
        this.outputStream = new IgnoreHeaderObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        new ChatMessageReader(socket, this::readMessage, () -> chatWorkers.remove(this)).read();
    }

    private void readMessage(Message message) {
        switch (message.getAction()) {
            case ADD_USER_TO_CHANNEL:
                chatWorkers.addUserToChannel(message);
                break;
            case CREATE_CHANNEL:
                chatWorkers.createChannel(message);
                break;
            case LEAVE_CHANNEL:
                chatWorkers.leaveChannel(message);
                break;
            case PRIVATE_MESSAGE:
                chatWorkers.broadcastToUser(message);
                historyWriter.writeToHistory(message);
                break;
            case CHANNEL_MESSAGE:
                chatWorkers.broadcastToChannel(message);
                historyWriter.writeToHistory(message);
                break;
            case PUBLIC_MESSAGE:
                chatWorkers.broadcast(message);
                historyWriter.writeToHistory(message);
                break;
            case SEND_FILE:
                chatWorkers.sendFile(message);
                break;
            case DOWNLOAD_FILE:
                chatWorkers.downloadFile(message);
                break;
            case HISTORY:
                chatWorkers.getHistory(message);
                break;
            case HISTORY_PRIVATE:
                chatWorkers.getPrivateHistory(message);
                break;
            case HISTORY_CHANNEL:
                chatWorkers.getHistoryFromChannel(message);
                break;
        }
    }

    public void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
