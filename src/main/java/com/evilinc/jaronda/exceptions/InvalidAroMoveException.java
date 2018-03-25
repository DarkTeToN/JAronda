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
public class InvalidAroMoveException extends JArondaException {
    
    public InvalidAroMoveException(String message) {
        super("The following move is invalid: " + message);
    }
    
}
