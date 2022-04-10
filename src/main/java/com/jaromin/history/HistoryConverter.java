package com.jaromin.history;

import com.jaromin.message.Message;

import java.io.StringBufferInputStream;

public class HistoryConverter {

    public static String convertMessage(Message message) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message.getSender());
        stringBuilder.append(" | ");
        stringBuilder.append(message.getReceiver());
        stringBuilder.append(" | ");
        stringBuilder.append(message.getChannel());
        stringBuilder.append(" | ");
        stringBuilder.append(message.getText());

        return stringBuilder.toString();
    }
}
