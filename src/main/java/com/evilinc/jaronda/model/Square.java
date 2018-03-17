/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model;

import com.evilinc.jaronda.enums.EPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author teton
 */
public class Square {

    private final int[] coordinates = new int[2];
    private final List<Square> adjacentSquares;
    public int numberOfBlackPawns = 0;
    public int numberOfWhitePawns = 0;
    private final int necessaryPawnsToConquer;
    public EPlayer conqueringPlayer;
    private final double leftLimitAngle;
    private final double rightLimitAngle;

    public Square(final int row, final int squareNumber, final int necessaryPawnsToConquer, final double leftLimitAngle, final double rightLimitAngle) {
        this.adjacentSquares = new ArrayList<>();
        this.coordinates[0] = row;
        this.coordinates[1] = squareNumber;
        this.necessaryPawnsToConquer = necessaryPawnsToConquer;
        this.leftLimitAngle = leftLimitAngle;
        this.rightLimitAngle = rightLimitAngle;
    }

    public void setAdjacentSquares(final Collection<Square> adjacentSquares) {
        this.adjacentSquares.clear();
        this.adjacentSquares.addAll(adjacentSquares);
    }

    public List<Square> getAdjacentSquares() {
        return adjacentSquares;
    }

    public double getLeftLimitAngle() {
        return leftLimitAngle;
    }

    public double getRightLimitAngle() {
        return rightLimitAngle;
    }

    public int getNecessaryPawnsToConquer() {
        return necessaryPawnsToConquer;
    }

    public boolean containsPawnFromColor(final EPlayer colorToCheck) {
        return (colorToCheck == conqueringPlayer) || (colorToCheck == EPlayer.BLACK ? numberOfBlackPawns > 0 : numberOfWhitePawns > 0);
    }

    public boolean isOnTheEdge() {
        return coordinates[0] == 0;
    }

    public boolean addPawn(final EPlayer player) {
        switch (player) {
            case BLACK:
                numberOfBlackPawns++;
                break;
            case WHITE:
                numberOfWhitePawns++;
            default:
                break;
        }
        return updateConqueredStatus();
    }

    private boolean updateConqueredStatus() {
        if (numberOfBlackPawns == necessaryPawnsToConquer) {
            conqueringPlayer = EPlayer.BLACK;
        } else if (numberOfWhitePawns == necessaryPawnsToConquer) {
            conqueringPlayer = EPlayer.WHITE;
        }

        if (conqueringPlayer != null) {
            resetPawns();
            return true;
        }
        return false;
    }

    public int getNumberOfBlackPawns() {
        return numberOfBlackPawns;
    }

    public int getNumberOfWhitePawns() {
        return numberOfWhitePawns;
    }

    public int getRow() {
        return coordinates[0];
    }

    public int getSquareNumber() {
        return coordinates[1];
    }

    public EPlayer getConqueringPlayer() {
        return conqueringPlayer;
    }
    
    public void setConqueringPlayer(final EPlayer conqueringPlayer) {
        this.conqueringPlayer = conqueringPlayer;
        resetPawns();
    }

    public void resetPawns() {
        numberOfBlackPawns = 0;
        numberOfWhitePawns = 0;
    }

    public void reset() {
        resetPawns();
        conqueringPlayer = null;
    }

    @Override
    public String toString() {
        return "[ " + coordinates[0] + ", " + coordinates[1] + " ]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Square) {
            final Square otherSquare = (Square) obj;
            return coordinates[0] == otherSquare.coordinates[0] && coordinates[1] == otherSquare.coordinates[1];
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Arrays.hashCode(this.coordinates);
        return hash;
    }
}
