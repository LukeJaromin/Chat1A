package com.jaromin.message;

import com.jaromin.file_transfer.FileUploader;
import com.jaromin.file_transfer.TransferredFile;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageBuilder {

    private static final Pattern PRIVATE_MESSAGE_PATTERN = Pattern.compile("@[a-zA-Z0-9]+");
    private static final Pattern CHANNEL_MESSAGE_PATTERN = Pattern.compile("#[a-zA-Z0-9]+");
    private static final Pattern ADD_USER_TO_CHANNEL_PATTERN = Pattern.compile("#ADD_[a-zA-Z0-9]+_[a-zA-Z0-9]+");
    private static final Pattern CREATE_CHANNEL_PATTERN = Pattern.compile("#CREATE_[a-zA-Z0-9]+");
    private static final Pattern LEAVE_CHANNEL_PATTERN = Pattern.compile("#LEAVE_[a-zA-Z0-9]+");
    private static final Pattern SEND_FILE_PATTERN = Pattern.compile("%SEND_[a-zA-Z0-9]+_.*");
    private static final Pattern DOWNLOAD_FILE_PATTERN = Pattern.compile("%DOWNLOAD_.*");
    private static final Pattern HISTORY_PATTERN = Pattern.compile("HISTORY");
    private static final Pattern HISTORY_PRIVATE_PATTERN = Pattern.compile("@HISTORY_[a-zA-Z0-9]+");
    private static final Pattern HISTORY_CHANNEL_PATTERN = Pattern.compile("#HISTORY_[a-zA-Z0-9]+");

    public static Message buildMessage(String text, String name) {
        String firstWord = text.split(" ")[0];
        if (PRIVATE_MESSAGE_PATTERN.matcher(firstWord).matches()) {
            return buildPrivateMessage(firstWord, text, name);
        } else if (CHANNEL_MESSAGE_PATTERN.matcher(firstWord).matches()) {
            return buildChannelMessage(firstWord, text, name);
        } else if (ADD_USER_TO_CHANNEL_PATTERN.matcher(firstWord).matches()) {
            return buildAddUserMessage(firstWord, name);
        } else if (CREATE_CHANNEL_PATTERN.matcher(firstWord).matches()) {
            return buildCreateChannelMessage(firstWord, name);
        } else if (LEAVE_CHANNEL_PATTERN.matcher(firstWord).matches()) {
            return buildLeaveChannelMessage(firstWord, name);
        } else if (SEND_FILE_PATTERN.matcher(firstWord).matches()) {
            return buildSendFileMessage(firstWord, name);
        } else if (DOWNLOAD_FILE_PATTERN.matcher(firstWord).matches() && firstWord.replaceFirst("_", "")
            .contains("_")) {
            return buildDownloadMessage(firstWord, name);
        } else if(HISTORY_PATTERN.matcher(firstWord).matches()) {
            return buildHistoryMessage(name);
        } else if(HISTORY_PRIVATE_PATTERN.matcher(firstWord).matches()) {
            return buildPrivateHistoryMessage(firstWord, name);
        } else if(HISTORY_CHANNEL_PATTERN.matcher(firstWord).matches()) {
            return buildChannelHistoryMessage(firstWord, name);
        } else {
            return buildPublicMessage(text, name);
        }
    }

    private static Message buildPrivateMessage(String firstWord, String text, String sender) {
        return Message.builder()
            .action(Action.PRIVATE_MESSAGE)
            .sender(sender)
            .receiver(firstWord.replace("@", ""))
            .text(String.format("Private message from: %s : %s", sender, removeFirstWord(text)))
            .build();
    }

    private static Message buildChannelMessage(String firstWord, String text, String sender) {
        String channel = firstWord.replace("#", "");
        return Message.builder()
            .action(Action.CHANNEL_MESSAGE)
            .sender(sender)
            .channel(channel)
            .text(channel + ": " + sender + ": " + removeFirstWord(text))
            .build();
    }

    private static Message buildAddUserMessage(String firstWord, String sender) {
        String[] receiverAndChannel = firstWord.replace("#ADD_", "").split("_");
        String receiver = receiverAndChannel[0];
        String channel = receiverAndChannel[1];
        return Message.builder()
            .action(Action.ADD_USER_TO_CHANNEL)
            .sender(sender)
            .receiver(receiver)
            .channel(channel)
            .text(String.format("%s added %s to %s channel", sender, receiver, channel))
            .build();
    }

    private static Message buildCreateChannelMessage(String firstWord, String sender) {
        String channel = firstWord.replace("#CREATE_", "");
        return Message.builder()
            .action(Action.CREATE_CHANNEL)
            .sender(sender)
            .channel(channel)
            .text(String.format("You successfully created %s channel", channel))
            .build();
    }

    private static Message buildLeaveChannelMessage(String firstWord, String sender) {
        String channel = firstWord.replace("#LEAVE_", "");
        return Message.builder()
            .action(Action.LEAVE_CHANNEL)
            .sender(sender)
            .channel(channel)
            .text(String.format("%s left %s channel", sender, channel))
            .build();
    }

    private static Message buildPublicMessage(String text, String sender) {
        return Message.builder()
            .action(Action.PUBLIC_MESSAGE)
            .sender(sender)
            .text(sender + ": " + text)
            .build();
    }

    private static Message buildSendFileMessage(String firstWord, String sender) {
        String[] recipientAndPath = firstWord.replace("%SEND_", "").split("_");

        return Message.builder()
            .action(Action.SEND_FILE)
            .sender(sender)
            .receiver(recipientAndPath[0])
            .transferredFile(FileUploader.getFile(recipientAndPath[1]))
            .build();
    }

    private static Message buildDownloadMessage(String firstWord, String sender) {
        String[] nameAndPath = firstWord.replace("%DOWNLOAD_", "").split("_");
        String fileName = nameAndPath[0];
        String path = nameAndPath[1];

        TransferredFile fileInfo = TransferredFile.builder()
            .fileName(fileName)
            .build();

        return Message.builder()
            .action(Action.DOWNLOAD_FILE)
            .text(path + "/" + fileName)
            .transferredFile(fileInfo)
            .sender(sender)
            .receiver(sender)
            .build();
    }

    private static Message buildHistoryMessage(String name) {
        return Message.builder()
                .action(Action.HISTORY)
                .sender(name)
                .build();
    }

    private static Message buildPrivateHistoryMessage(String firstWord, String name) {
        String receiver = firstWord.replace("@HISTORY_", "");
        return Message.builder()
                .action(Action.HISTORY_PRIVATE)
                .sender(name)
                .receiver(receiver)
                .build();
    }

    private static Message buildChannelHistoryMessage(String firstWord, String name) {
        String channel = firstWord.replace("#HISTORY_", "");
        return Message.builder()
                .action(Action.HISTORY_CHANNEL)
                .channel(channel)
                .sender(name)
                .build();
    }

    private static String removeFirstWord(String text) {
        return text.split(" ", 2)[1];
    }
}
