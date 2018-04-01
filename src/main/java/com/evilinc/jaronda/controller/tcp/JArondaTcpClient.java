/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.tcp;

import com.evilinc.jaronda.consts.SystemConst;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author teton
 */
public class JArondaTcpClient extends AJArondaTcp {

    private final String serverAddress;

    public JArondaTcpClient(final String serverAddress) {
        super();
        this.serverAddress = serverAddress;
    }

    @Override
    public void start() {
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;

        try {
            /*
         * les informations du serveur ( port et adresse IP ou nom d'hote
         * 127.0.0.1 est l'adresse local de la machine
             */
            clientSocket = new Socket(serverAddress, SystemConst.TCP_PORT);

            System.out.println("Connexion to server OK");

            //flux pour envoyer
            out = new PrintWriter(clientSocket.getOutputStream());
            //flux pour recevoir
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread sendThread = new Thread(() -> {
                while (true) {
                    if (!messageQueue.isEmpty()) {
                        final String messageToSend = messageQueue.poll();
                        System.out.println("Client - Sending: " + messageToSend);
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
                        System.out.println("Client - Receiving: " + receivedMessage);
                        handleReceivedMessage(receivedMessage);
                        receivedMessage = in.readLine();
                    }
                    //sortir de la boucle si le client a déconecté
                    System.out.println("Client disconnected");
                    //fermer le flux et la session socket
                    out.close();
                    clientSocket.close();
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
