/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.tcp;

import com.evilinc.jaronda.consts.EventConst;
import com.evilinc.jaronda.consts.SystemConst;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author teton
 */
public class JArondaTcpServer extends AJArondaTcp {

    @Override
    public void start() {
        final ServerSocket serveurSocket;
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;

        try {
            serveurSocket = new ServerSocket(SystemConst.TCP_PORT);
            eventController.firePropertyChange(EventConst.WAITING_CLIENT_CONNECTION, null, null);
            clientSocket = serveurSocket.accept();
            System.out.println("Client connected");
            eventController.firePropertyChange(EventConst.CLIENT_CONNECTION_OK, null, null);
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread sendThread = new Thread(() -> {
                while (true) {
                    if (!messageQueue.isEmpty()) {
                        final String messageToSend = messageQueue.poll();
                        System.out.println("Server - Sending: " + messageToSend);
                        out.println(messageToSend);
                        out.flush();
                    }
                }
            });
            sendThread.start();

            Thread receptionThread = new Thread(() -> {
                try {
                    // Blocks the thread until a message is received
                    String receivedMessage = in.readLine();
                    // So this loop will be active as long as the clien is connected
                    while (receivedMessage != null) {
                        System.out.println("Server - Receiving: " + receivedMessage);
                        handleReceivedMessage(receivedMessage);
                        receivedMessage = in.readLine();
                    }
                    //sortir de la boucle si le client a déconecté
                    System.out.println("Client disconnected");
                    //fermer le flux et la session socket
                    out.close();
                    clientSocket.close();
                    serveurSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receptionThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
