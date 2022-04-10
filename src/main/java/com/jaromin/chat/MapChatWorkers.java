package com.jaromin.chat;

import com.jaromin.channel.Channels;
import com.jaromin.channel.ChannelsMap;
import com.jaromin.channel.SynchronizedChannelsProxy;
import com.jaromin.file_transfer.FileUploader;
import com.jaromin.file_transfer.TransferredFile;
import com.jaromin.history.HistoryRepository;
import com.jaromin.message.Message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class MapChatWorkers implements ChatWorkers {

    private final Map<String, ChatWorker> chatWorkers = new HashMap<>();
    private final Channels channels = new SynchronizedChannelsProxy(new ChannelsMap());
    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(getClass().getName());
    HistoryRepository historyRepository = new HistoryRepository();


    @Override
    public void add(String username, ChatWorker chatWorker) {
        chatWorkers.put(username, chatWorker);
    }

    @Override
    public void remove(ChatWorker chatWorker) {
        chatWorkers.values().remove(chatWorker);
    }

    @Override
    public void broadcast(Message message) {
        chatWorkers.values().forEach(chatWorker -> chatWorker.send(message));
    }

    @Override
    public void broadcastToUser(Message message) {
        ChatWorker chatWorker = chatWorkers.get(message.getReceiver());
        if (chatWorker == null) {
            ChatWorker senderChatWorker = chatWorkers.get(message.getSender());
            senderChatWorker.send(message);
        } else {
            chatWorker.send(message);
        }
    }

    @Override
    public void broadcastToChannel(Message message) {
        if (!channels.channelExists(message.getChannel())) {
            message.setText(format("Channel with name: %s does not exist", message.getChannel()));
            chatWorkers.get(message.getSender())
                    .send(message);
        } else if (!channels.getChannelUsers(message.getChannel()).contains(message.getSender())) {
            message.setText(format("You are not a member of the channel: %s", message.getChannel()));
            chatWorkers.get(message.getSender())
                    .send(message);
        } else {
            performBroadcastToChannel(message);
        }
    }

    @Override
    public void addUserToChannel(Message message) {
        Set<String> channelUsers = channels.getChannelUsers(message.getChannel());
        if (channelUsers == null) {
            message.setText(format("Channel with name: %s does not exist", message.getChannel()));
            chatWorkers.get(message.getSender())
                    .send(message);
        } else if (!channelUsers.contains(message.getSender())) {
            message.setText(format("You are not a member of channel: %s and you cannot add any user to the channel",
                    message.getChannel()));
            chatWorkers.get(message.getSender()).send(message);
        } else if (channelUsers.contains(message.getReceiver())) {
            message.setText(format("user.User: %s already belongs to channel: %s",
                    message.getReceiver(), message.getChannel()));
            chatWorkers.get(message.getSender()).send(message);
        } else if (chatWorkers.get(message.getReceiver()) == null) {
            message.setText(format("user.User with name: %s does not exist", message.getSender()));
            chatWorkers.get(message.getSender()).send(message);
        } else {
            channels.addUserToChannel(message.getChannel(), message.getReceiver());
            performBroadcastToChannel(message);
        }
    }

    @Override
    public void leaveChannel(Message message) {
        Set<String> channelUsers = channels.getChannelUsers(message.getChannel());
        if (channelUsers == null || !channelUsers.contains(message.getSender())) {
            message.setText(format("You are not a member of a %s channel", message.getChannel()));
        } else {
            channels.leaveChannel(message.getChannel(), message.getSender());
            performBroadcastToChannel(message);
            message.setText("You left channel: " + message.getChannel());
        }
        chatWorkers.get(message.getSender()).send(message);
    }

    @Override
    public void createChannel(Message message) {
        ChatWorker chatWorker = chatWorkers.get(message.getSender());
        if (channels.channelExists(message.getChannel())) {
            message.setText(format("Channel with name: %s already exists", message.getChannel()));
        } else {
            logger.log(Level.INFO, "Creating new channel: {0} for user: {1}",
                    new String[]{message.getChannel(), message.getSender()});
            channels.createChannel(message.getChannel(), message.getSender());
        }
        chatWorker.send(message);
    }

    @Override
    public void sendFile(Message message) {
        System.out.println("sending file");
        String filename = message.getTransferredFile().getFileName();
        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/files/uploaded/" + filename)) {
            fileOutputStream.write(message.getTransferredFile().getFileBytesArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.setText(message.getSender() + " sent you a file: " + filename
                + " You can download it by providing directory and filename in the following format: %DOWNLOAD_"
                + filename + "_src/main/resources/files/downloaded");
        System.out.println("Saving file");
        message.setTransferredFile(null);
        chatWorkers.get(message.getReceiver()).send(message);
    }

    @Override
    public void downloadFile(Message message) {
        String filename = message.getTransferredFile().getFileName();
        TransferredFile transferredFile = FileUploader.getFile("src/main/resources/files/uploaded/" + filename);
        message.setTransferredFile(transferredFile);
        chatWorkers.get(message.getReceiver()).send(message);
    }

    @Override
    public void getHistory(Message message) {
        message.setText(historyRepository.getPublicMessages());
        chatWorkers.get(message.getSender()).send(message);
    }

    @Override
    public void getPrivateHistory(Message message) {
        message.setText(historyRepository.getPrivateMessages(message.getSender(), message.getReceiver()));
        chatWorkers.get(message.getSender()).send(message);
    }

    @Override
    public void getHistoryFromChannel(Message message) {
        Set<String> channelUsers = channels.getChannelUsers(message.getChannel());
        if (channelUsers == null) {
            message.setText("Channel doesn't exist!!!");
        } else if (channelUsers.contains(message.getSender())) {
            message.setText(historyRepository.getChannelMessages(message.getChannel()));
        } else {
            message.setText("You are not a member of this channel!!!");
        }
        chatWorkers.get(message.getSender()).send(message);
    }

    private void performBroadcastToChannel(Message message) {
        Set<String> users = channels.getChannelUsers(message.getChannel());
        if (users == null) {
            message.setText(format("Channel: %s does not exist", message.getChannel()));
            chatWorkers.get(message.getSender()).send(message);
        } else {
            Set<ChatWorker> chatWorkersForChannel =
                    chatWorkers.entrySet().stream().filter(entry -> users.contains(entry.getKey())).map(Map.Entry::getValue)
                            .collect(Collectors.toSet());
            chatWorkersForChannel.forEach(chatWorker -> chatWorker.send(message));
        }
    }


}
