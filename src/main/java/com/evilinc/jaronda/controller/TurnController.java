/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.enums.EPlayer;

/**
 *
 * @author teton
 */
public class TurnController {

    private final EPlayer[] turnArray;
    private int moveNumber = 0;

    public TurnController() {
        turnArray = new EPlayer[4];
        turnArray[0] = EPlayer.BLACK;
        turnArray[1] = EPlayer.WHITE;
        turnArray[2] = EPlayer.WHITE;
        turnArray[3] = EPlayer.BLACK;
    }

    public void playMove() {
        moveNumber++;
    }

    public void cancelMove() {
        moveNumber--;
    }

    public EPlayer getCurrentPlayer() {
        return turnArray[(moveNumber) % 4];
    }

    public EPlayer getNextPlayer() {
        return turnArray[(moveNumber + 1) % 4];
    }

    public int getRemainingMoves() {
        switch (moveNumber % 4) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
            default:
                return 2;
        }
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }
    
    public void reset() {
        moveNumber = 0;
    }

}
