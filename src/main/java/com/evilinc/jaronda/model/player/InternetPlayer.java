/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.player;

import com.evilinc.jaronda.consts.EventConst;
import com.evilinc.jaronda.controller.tcp.AJArondaTcp;
import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.game.Move;
import com.evilinc.jaronda.model.game.Square;
import com.evilinc.jaronda.model.serialization.json.JsonMove;
import com.google.gson.Gson;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author teton
 */
public class InternetPlayer extends APlayer {

    private final AJArondaTcp connexion;
    
    public InternetPlayer(EPlayer color, final AJArondaTcp connexion) {
        super(color);
        this.connexion = connexion;
        eventController.addPropertyChangeListener(EventConst.NETWORK_MOVE_PLAYED, (PropertyChangeEvent evt) -> {
            eventController.firePropertyChange(EventConst.MOVE_PLAYED, InternetPlayer.this, evt.getNewValue());
        });
    }
    
    public void startConnexion() {
        connexion.start();
    }

    @Override
    public void playMove(Square[][] board, final Move lastPlayedMove) {
        JsonMove playedMove = new JsonMove();
        if (lastPlayedMove == null) {
            playedMove.row = -1;
            playedMove.squareNumber = -1;
        } else {
            playedMove.row = lastPlayedMove.getRow();
            playedMove.squareNumber = lastPlayedMove.getSquareNumber();
        }
        Gson gson = new Gson();
        connexion.sendMessage(gson.toJson(playedMove));
//        eventController.firePropertyChange(EventConst.NOTIFY_LAST_PLAYED_MOVE, InternetPlayer.this, playedMove);
        
    }

}
