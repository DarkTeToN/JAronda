/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.tcp;

import com.evilinc.jaronda.consts.EventConst;
import com.evilinc.jaronda.controller.game.EventController;
import com.evilinc.jaronda.model.serialization.json.JsonMove;
import com.google.gson.Gson;
import java.beans.PropertyChangeEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author teton
 */
public class NetworkController {

    private static NetworkController instance;
    private final EventController eventController;
    private final Gson gson;

    private NetworkController() {
        eventController = EventController.getInstance();
        gson = new Gson();

        eventController.addPropertyChangeListener(EventConst.NETWORK_MESSAGE_RECEIVED, (PropertyChangeEvent evt) -> {
            final String receivedMessage = (String) evt.getNewValue();
            final JsonMove playedMove = getPlayedMoveFromMessage(receivedMessage);
            if (playedMove.row != -1) {
                SwingUtilities.invokeLater(() -> {
                    eventController.firePropertyChange(EventConst.NETWORK_MOVE_PLAYED, null, playedMove);
                });
            }
        });
    }

    private JsonMove getPlayedMoveFromMessage(final String message) {
        return gson.fromJson(message, JsonMove.class);
    }

    public static NetworkController getInstance() {
        if (instance == null) {
            instance = new NetworkController();
        }
        return instance;
    }

}
