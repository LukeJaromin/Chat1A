package com.jaromin.channel;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchronizedChannelsProxy implements Channels {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ChannelsMap channelsMap;

    public SynchronizedChannelsProxy(ChannelsMap channelsMap) {
        this.channelsMap = channelsMap;
    }

    @Override
    public void createChannel(String channelName, String funder) {
        lock.writeLock().lock();
        channelsMap.createChannel(channelName, funder);
        lock.writeLock().unlock();
    }

    @Override
    public void addUserToChannel(String channelName, String userName) {
        lock.writeLock().lock();
        channelsMap.addUserToChannel(channelName, userName);
        lock.writeLock().unlock();
    }

    @Override
    public Set<String> getChannelUsers(String channelName) {
        lock.readLock().lock();
        Set<String> users = channelsMap.getChannelUsers(channelName);
        lock.readLock().unlock();
        return users;
    }

    @Override
    public void leaveChannel(String channelName, String userName) {
        lock.writeLock().lock();
        channelsMap.leaveChannel(channelName, userName);
        lock.writeLock().unlock();
    }

    @Override
    public boolean channelExists(String channelName) {
        lock.readLock().lock();
        boolean exists = channelsMap.channelExists(channelName);
        lock.readLock().unlock();
        return exists;
    }
}
