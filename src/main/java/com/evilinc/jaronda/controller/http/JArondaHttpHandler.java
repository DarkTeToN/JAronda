/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.http;

import com.evilinc.jaronda.controller.game.GameController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author teton
 */
public abstract class JArondaHttpHandler implements HttpHandler {

    protected final GameController gameController;

    public JArondaHttpHandler(GameController gameController) {
        this.gameController = gameController;
    }
    
    protected void sendResponse(final HttpExchange exchange, final int responseCode, final String responseContent) throws IOException {
        exchange.sendResponseHeaders(responseCode, responseContent.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseContent.getBytes());
        }
    }
}
