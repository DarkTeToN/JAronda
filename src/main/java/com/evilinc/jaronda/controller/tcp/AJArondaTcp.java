/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.tcp;

import com.evilinc.jaronda.consts.EventConst;
import com.evilinc.jaronda.controller.game.EventController;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author teton
 */
public abstract class AJArondaTcp {

    protected final ConcurrentLinkedQueue<String> messageQueue;
    protected final EventController eventController;

    public AJArondaTcp() {
        eventController = EventController.getInstance();
        messageQueue = new ConcurrentLinkedQueue();
    }

    public abstract void start();
    
    public void sendMessage(final String messageToSend) {
        messageQueue.add(messageToSend);
    }

    protected void handleReceivedMessage(final String receivedMessage) {
        Thread messageHandlingThread = new Thread(() -> {
            eventController.firePropertyChange(EventConst.NETWORK_MESSAGE_RECEIVED, null, receivedMessage);
        });
        messageHandlingThread.start();
    }
}
