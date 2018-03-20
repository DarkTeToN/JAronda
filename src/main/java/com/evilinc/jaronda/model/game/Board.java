/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.game;

import com.evilinc.jaronda.model.serialization.JsonSquare;
import java.util.List;

/**
 *
 * @author teton
 */
public class Board {

    public List<JsonSquare> squares;
    public String currentPlayer;
    public int remainingMoves;
    public String winner;
    public int blackScore;
    public int whiteScore;
    public int moveRow = -1;
    public int moveSquareNumber = -1;
    public boolean validMove = false;

    public Board() {
    }
}
