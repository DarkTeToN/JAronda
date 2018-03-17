/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model;

/**
 *
 * @author teton
 */
public class JsonSquare {

    public int row;
    public int squareNumber;
    public int numberOfBlackPawns;
    public int numberOfWhitePawns;
    public int necessaryPawsToConquer;
    public String conqueringColor;

    public JsonSquare() {
    }
    
    public JsonSquare(final Square square) {
        row = square.getRow();
        squareNumber = square.getSquareNumber();
        numberOfBlackPawns = square.getNumberOfBlackPawns();
        numberOfWhitePawns = square.getNumberOfWhitePawns();
        conqueringColor = String.valueOf(square.getConqueringPlayer());
        necessaryPawsToConquer = square.getNecessaryPawnsToConquer();
    }
    
}
