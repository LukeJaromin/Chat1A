package com.jaromin.chat;

import com.jaromin.message.Message;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchronizedChatWorkersProxy implements ChatWorkers {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ChatWorkers chatWorkers;

    public SynchronizedChatWorkersProxy(ChatWorkers chatWorkers) {
        this.chatWorkers = chatWorkers;
    }

    @Override
    public void add(String username, ChatWorker chatWorker) {
        lock.writeLock().lock();
        chatWorkers.add(username, chatWorker);
        lock.writeLock().unlock();
    }

    @Override
    public void remove(ChatWorker chatWorker) {
        lock.writeLock().lock();
        chatWorkers.remove(chatWorker);
        lock.writeLock().unlock();
    }

    @Override
    public void broadcast(Message message) {
        lock.readLock().lock();
        chatWorkers.broadcast(message);
        lock.readLock().unlock();
    }

    @Override
    public void broadcastToUser(Message message) {
        lock.readLock().lock();
        chatWorkers.broadcastToUser(message);
        lock.readLock().unlock();
    }

    @Override
    public void broadcastToChannel(Message message) {
        lock.readLock().lock();
        chatWorkers.broadcastToChannel(message);
        lock.readLock().unlock();
    }

    @Override
    public void addUserToChannel(Message message) {
        lock.writeLock().lock();
        chatWorkers.addUserToChannel(message);
        lock.writeLock().unlock();
    }

    @Override
    public void leaveChannel(Message message) {
        lock.writeLock().lock();
        chatWorkers.leaveChannel(message);
        lock.writeLock().unlock();
    }

    @Override
    public void createChannel(Message message) {
        lock.writeLock().lock();
        chatWorkers.createChannel(message);
        lock.writeLock().unlock();
    }

    @Override
    public void sendFile(Message message) {
        lock.readLock().lock();
        chatWorkers.sendFile(message);
        lock.readLock().unlock();
    }

    @Override
    public void downloadFile(Message message) {
        lock.readLock().lock();
        chatWorkers.downloadFile(message);
        lock.readLock().unlock();
    }

    @Override
    public void getHistory(Message message) {
        lock.readLock().lock();
        chatWorkers.getHistory(message);
        lock.readLock().unlock();
    }

    @Override
    public void getPrivateHistory(Message message) {
        lock.readLock().lock();
        chatWorkers.getPrivateHistory(message);
        lock.readLock().unlock();
    }

    @Override
    public void getHistoryFromChannel(Message message) {
        lock.readLock().lock();
        chatWorkers.getHistoryFromChannel(message);
        lock.readLock().unlock();
    }

}
