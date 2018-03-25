/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.http;

import com.evilinc.jaronda.consts.HttpConst;
import com.evilinc.jaronda.controller.game.GameController;
import com.evilinc.jaronda.model.serialization.JsonBoard;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 *
 * @author teton
 */
public class GetBoardHttpHandler extends JArondaHttpHandler {

    public GetBoardHttpHandler(GameController gameController) {
        super(gameController);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (HttpConst.GET_METHOD.equals(exchange.getRequestMethod())) {
            final Gson gson = new Gson();
            final JsonBoard currentBoard = gameController.getCurrentBoard();
            sendResponse(exchange, HttpConst.REQUEST_OK_HTTP_CODE, gson.toJson(currentBoard));
        } else {
            sendResponse(exchange, HttpConst.WRONG_METHOD_HTTP_CODE, HttpConst.WRONG_METHOD_ERROR);
        }
    }

}
