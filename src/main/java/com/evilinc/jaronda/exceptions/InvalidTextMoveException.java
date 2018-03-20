/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.exceptions;

/**
 *
 * @author teton
 */
public class InvalidTextMoveException extends JArondaException {
    
    public InvalidTextMoveException(String message) {
        super("The following move is invalid: " + message);
    }
    
}
