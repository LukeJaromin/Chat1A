package com.jaromin.server;

import com.jaromin.chat.ChatWorkers;
import com.jaromin.chat.MapChatWorkers;
import com.jaromin.chat.SynchronizedChatWorkersProxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultChatServerFactory implements ChatServerFactory {

    private static final int NUMBER_OF_THREADS = 1024;
    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(getClass().getName());

    @Override
    public ChatWorkers createChatWorkers() {
        return new SynchronizedChatWorkersProxy(new MapChatWorkers());
    }

    @Override
    public ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    @Override
    public Logger createLogger() {
        return logger::info;
    }

}
