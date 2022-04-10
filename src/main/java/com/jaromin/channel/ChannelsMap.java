package com.jaromin.channel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChannelsMap implements Channels{

    private static final Map<String, Set<String>> CHANNELS_MAP = new HashMap<>();

    @Override
    public void createChannel(String channelName, String funder) {
        ChannelsMap.CHANNELS_MAP.put(channelName, Set.of(funder));
    }

    @Override
    public void addUserToChannel(String channelName, String userName) {
        var users = new HashSet<>(ChannelsMap.CHANNELS_MAP.get(channelName));
        users.add(userName);
        CHANNELS_MAP.put(channelName, users);
    }

    @Override
    public Set<String> getChannelUsers(String channelName) {
        return ChannelsMap.CHANNELS_MAP.get(channelName);
    }

    @Override
    public void leaveChannel(String channelName, String userName) {
        var users = CHANNELS_MAP.get(channelName);
        users.remove(userName);
        if (users.isEmpty()){
            CHANNELS_MAP.remove(channelName);
        }
    }

    @Override
    public boolean channelExists(String channelName) {
        return CHANNELS_MAP.containsKey(channelName);
    }
}
