/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.serialization;

import com.evilinc.jaronda.exceptions.InvalidTextMoveException;

/**
 *
 * @author teton
 */
public class TextMove {

    public final int row;
    public final int column;

    public TextMove(final String text) throws InvalidTextMoveException {
        if (text == null || text.length() < 2) {
            throw new InvalidTextMoveException(text);
        }
        row = ((int) text.toLowerCase().codePointAt(0)) - 97;
        column = Integer.parseInt(String.valueOf(text.charAt(1)));
        if (row < 0 || row > 3 || column < 0 || column > 7) {
            throw new InvalidTextMoveException(text);
        }
    }
}
