package com.jaromin.history;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HistoryRepository {

    public String getPublicMessages() {
        return readLines()
                .filter(message -> message.getReceiver().equals("null") && message.getChannel().equals("null"))
                .map(History::getMessage)
                .collect(Collectors.joining("\n"));
    }

    public String getPrivateMessages(String sender, String receiver) {
        return readLines()
                .filter(message -> message.getSender().equals(receiver) && message.getReceiver().equals(sender))
                .map(History::getMessage)
                .collect(Collectors.joining("\n"));
    }

    public String getChannelMessages(String channel) {
        return readLines()
                .filter(message -> message.getChannel().equals(channel))
                .map(History::getMessage)
                .collect(Collectors.joining("\n"));
    }

    private synchronized Stream<History> readLines() {
        File file = new File("src/main/resources/history/history.txt");
        List<String> lines = new ArrayList<>();
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.stream().map(line -> line.split(Pattern.quote(" | ")))
                .map(strings -> new History(strings[0], strings[1], strings[2], strings[3]));
    }

    @AllArgsConstructor
    @Getter
    private static class History {
        private String sender;
        private String receiver;
        private String channel;
        private String message;
    }
}
