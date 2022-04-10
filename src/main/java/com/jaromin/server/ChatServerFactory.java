package com.jaromin.server;

import com.jaromin.chat.ChatWorkers;
import java.util.concurrent.ExecutorService;

public interface ChatServerFactory {

    ChatWorkers createChatWorkers();

    ExecutorService createExecutorService();

    Logger createLogger();

}