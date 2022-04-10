package com.jaromin.chat;

import com.jaromin.message.Message;

public interface ChatWorkers {

    void add(String username, ChatWorker chatWorker);

    void remove(ChatWorker chatWorker);

    void broadcast(Message text);

    void broadcastToUser(Message text);

    void broadcastToChannel(Message text);

    void addUserToChannel(Message text);

    void leaveChannel(Message text);

    void createChannel(Message text);

    void sendFile(Message message);

    void downloadFile(Message message);

    void getHistory(Message message);

    void getPrivateHistory(Message message);

    void getHistoryFromChannel(Message message);
}
