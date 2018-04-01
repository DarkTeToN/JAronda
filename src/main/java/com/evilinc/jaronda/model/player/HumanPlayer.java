/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.player;

import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.game.Move;
import com.evilinc.jaronda.model.game.Square;

/**
 *
 * @author teton
 */
public class HumanPlayer extends APlayer {

    public HumanPlayer(EPlayer color) {
        super(color);
    }

    @Override
    public void playMove(final Square[][] board, final Move playedMove) {
        // Nothing to do here since we're waiting for the player to click on the board.
        // This part is directly handled by the GameController.
    }
    
}
