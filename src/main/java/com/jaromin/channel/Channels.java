package com.jaromin.channel;

import java.util.Set;

public interface Channels {

    void createChannel(String channelName, String funder);

    void addUserToChannel(String channelName, String userName);

    Set<String> getChannelUsers(String channelName);

    void leaveChannel(String channelName, String userName);

    boolean channelExists(String channelName);
}
