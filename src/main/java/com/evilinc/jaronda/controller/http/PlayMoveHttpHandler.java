/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.http;

import com.evilinc.jaronda.consts.HttpConst;
import com.evilinc.jaronda.controller.game.GameController;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.model.serialization.JsonBoard;
import com.evilinc.jaronda.model.serialization.JsonMove;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author teton
 */
public class PlayMoveHttpHandler extends JArondaHttpHandler {

    public PlayMoveHttpHandler(GameController gameController) {
        super(gameController);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (HttpConst.POST_METHOD.equals(exchange.getRequestMethod())) {
            final Gson gson = new Gson();
            final JsonMove moveToPlay = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), JsonMove.class);
            try {
                gameController.playMoveAt(moveToPlay.row, moveToPlay.squareNumber);
                final JsonBoard boardAfterMove = gameController.getCurrentBoard();
                sendResponse(exchange, HttpConst.REQUEST_OK_HTTP_CODE, gson.toJson(boardAfterMove));
            } catch (IllegalMoveException ex) {
                sendResponse(exchange, HttpConst.ERROR_HTTP_CODE, ex.getMessage());
            }
        } else {
            sendResponse(exchange, HttpConst.WRONG_METHOD_HTTP_CODE, HttpConst.WRONG_METHOD_ERROR);
        }
    }

}
