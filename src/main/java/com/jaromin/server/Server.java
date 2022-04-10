package com.jaromin.server;

import com.jaromin.chat.ChatWorker;
import com.jaromin.chat.ChatWorkers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server {

    public static final int PORT = 1999;

    private final ChatServerFactory factory = new DefaultChatServerFactory();
    private final Logger logger = factory.createLogger();
    private final ChatWorkers chatWorkers = factory.createChatWorkers();
    private final ExecutorService executorService = factory.createExecutorService();

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    private void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        logger.log("Server listening on port: " + PORT);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String name = reader.readLine();
            logger.log("New user connected with username: " + name);
            ChatWorker chatWorker = new ChatWorker(clientSocket, chatWorkers);
            chatWorkers.add(name, chatWorker);
            executorService.execute(chatWorker);
        }
    }
}
