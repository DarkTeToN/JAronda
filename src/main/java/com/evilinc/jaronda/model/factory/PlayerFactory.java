/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.factory;

import com.evilinc.jaronda.controller.tcp.JArondaTcpClient;
import com.evilinc.jaronda.controller.tcp.JArondaTcpServer;
import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.player.AIPlayer;
import com.evilinc.jaronda.model.player.APlayer;
import com.evilinc.jaronda.model.player.HumanPlayer;
import com.evilinc.jaronda.model.player.InternetPlayer;

/**
 *
 * @author teton
 */
public class PlayerFactory {

    public static final APlayer getPlayer(final EPlayer currentPlayer) {
        switch (currentPlayer.playerType) {
            case INTERNET_HOST:
                return new InternetPlayer(currentPlayer, new JArondaTcpClient("192.168.1.29"));
            case INTERNET_CLIENT:
                return new InternetPlayer(currentPlayer, new JArondaTcpServer());
            case CPU:
                return new AIPlayer(currentPlayer);
            case HUMAN:
            default:
                return new HumanPlayer(currentPlayer);
        }
    }
}
