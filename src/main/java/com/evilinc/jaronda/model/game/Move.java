/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.game;

import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.serialization.JsonSquare;
import java.util.List;

/**
 *
 * @author teton
 */
public class Move {

    private final EPlayer player;
    private final int row;
    private final int squareNumber;
    private final List<JsonSquare> conqueredSquares;

    public Move(final EPlayer player, final int row, final int squareNumber, final List<JsonSquare> conqueredSquares) {
        this.player = player;
        this.row = row;
        this.squareNumber = squareNumber;
        this.conqueredSquares = conqueredSquares;
    }

    public EPlayer getPlayer() {
        return player;
    }

    public int getRow() {
        return row;
    }

    public int getSquareNumber() {
        return squareNumber;
    }

    public List<JsonSquare> getConqueredSquares() {
        return conqueredSquares;
    }

}
