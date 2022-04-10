package com.jaromin.client;

import com.jaromin.message.MessageBuilder;
import com.jaromin.message.MessageReader;
import com.jaromin.message.MessageWriter;
import com.jaromin.server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class Client {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9]+");
    private final Socket clientSocket;
    private final Consumer<String> onText;
    private final Runnable readFromSocket;
    private final Runnable readFromConsole;

    public Client(String name) throws IOException {
        clientSocket = new Socket("localhost", Server.PORT);
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        printWriter.println(name);
        onText = text -> new MessageWriter(clientSocket).write(MessageBuilder.buildMessage(text, name));
        readFromConsole = () -> new TextReader(System.in, onText).read();
        readFromSocket = () -> new MessageReader(clientSocket).read();
        System.out.println(name);
    }

    public static void main(String[] args) throws IOException {
        String name = getUsername();
        new Client(name).start();
    }

    private void start() {
        new Thread(readFromSocket).start();
        Thread consoleMessageReader = new Thread(readFromConsole);
        consoleMessageReader.setDaemon(true);
        consoleMessageReader.start();
    }

    private static String getUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username and press enter");
        String name = scanner.nextLine();
        boolean nameIsIncorrect = !NAME_PATTERN.matcher(name).matches();
        while (nameIsIncorrect) {
            if (!NAME_PATTERN.matcher(name).matches()) {
                System.out.println(
                    "Provided username is incorrect. It should contain only letters and digits without spaces.");
            }
            name = scanner.nextLine();
            nameIsIncorrect = !NAME_PATTERN.matcher(name).matches();
        }
        return name;
    }
}
