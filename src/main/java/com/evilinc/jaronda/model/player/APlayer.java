/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.player;

import com.evilinc.jaronda.controller.game.EventController;
import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.game.Move;
import com.evilinc.jaronda.model.game.Square;

/**
 *
 * @author teton
 */
public abstract class APlayer {

    protected final EventController eventController;
    public final EPlayer color;

    public APlayer(final EPlayer color) {
        eventController = EventController.getInstance();
        this.color = color;
    }

    public abstract void playMove(final Square[][] board, final Move lastPlayedMove);
    
}
