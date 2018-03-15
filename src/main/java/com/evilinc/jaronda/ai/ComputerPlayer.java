/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.ai;

import com.evilinc.jaronda.EPlayer;
import com.evilinc.jaronda.controller.SquareController;

/**
 *
 * @author teton
 */
public class ComputerPlayer {

    private final EPlayer player;
    private final EPlayer opponent;
    private final SquareController squareController;
    
    public ComputerPlayer(final EPlayer player) {
        this.player = player;
        this.opponent = EPlayer.getOpponent(player);
        this.squareController = new SquareController();
        
    }
    
    public int[] getMove() {
        final int[] chosenMove = new int[2];
        
        return chosenMove;
    }
    
}
