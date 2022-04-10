package com.jaromin.history;

import com.jaromin.message.Message;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class HistoryWriter {

    public synchronized void writeToHistory(Message message) {
        try (FileWriter fw = new FileWriter("src/main/resources/history/history.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(HistoryConverter.convertMessage(message));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
