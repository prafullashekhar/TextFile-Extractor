package org.example.data;

import org.example.domain.MetaDataModel;

import java.util.concurrent.LinkedBlockingQueue;

public class WaitingPool extends Thread {
    
    private LinkedBlockingQueue<MetaDataModel> waitingQueue;
    
    public WaitingPool() {
        waitingQueue = new LinkedBlockingQueue<>();
    }
    
    public void addInQueue(MetaDataModel metaDataModel) {
        waitingQueue.offer(metaDataModel);
    }
    
    private MetaDataModel getFromQueue() {
        return waitingQueue.poll();
    }
    
    @Override
    public void run() {
        while (true) {
            if (!waitingQueue.isEmpty()) {
                // assign task to
            }
        }
    }
}
