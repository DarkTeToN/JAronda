/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.serialization.json;

import java.util.List;

/**
 *
 * @author teton
 */
public class JsonBoard {

    public List<JsonSquare> squares;
    public String currentPlayer;
    public int remainingMoves;
    public String winner;
    public int blackScore;
    public int whiteScore;

    public JsonBoard() {
    }
}
