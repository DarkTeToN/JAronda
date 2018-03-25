/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.serialization.aro;

import com.evilinc.jaronda.exceptions.InvalidAroMoveException;
import com.evilinc.jaronda.model.game.Move;

/**
 *
 * @author teton
 */
public class AroMove {

    public final int row;
    public final int squareNumber;

    public AroMove(final Move move) {
        row = move.getRow();
        squareNumber = move.getSquareNumber();
    }

    public AroMove(final String text) throws InvalidAroMoveException {
        if (text == null || text.length() < 2) {
            throw new InvalidAroMoveException(text);
        }
        row = ((int) text.toLowerCase().codePointAt(0)) - 97;
        squareNumber = Integer.parseInt(String.valueOf(text.charAt(1)));
        if (row < 0 || row > 3 || squareNumber < 0 || squareNumber > 7) {
            throw new InvalidAroMoveException(text);
        }
    }

    @Override
    public String toString() {
        return ((char) (row + 97) + "" + squareNumber);
    }
}
