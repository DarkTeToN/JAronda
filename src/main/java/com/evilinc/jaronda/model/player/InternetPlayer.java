/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.player;

import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.game.Square;

/**
 *
 * @author teton
 */
public class InternetPlayer extends APlayer {

    public InternetPlayer(EPlayer color) {
        super(color);
    }

    @Override
    public void playMove(Square[][] board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
