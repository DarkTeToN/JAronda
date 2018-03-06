/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.EPlayer;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.model.Square;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author teton
 */
public class GameController {

    private static GameController instance;
    private final SquareController squareController;
    private EPlayer playerTurn;
    private int numberOfMovesToPlay = 1;

    private GameController() {
        playerTurn = EPlayer.BLACK;
        squareController = SquareController.getInstance();
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void resetGame() {
        squareController.reset();
    }

    public void playMoveAt(final int row, final int squareNumber) throws IllegalMoveException {
        final Square playedSquare = squareController.getSquareAt(row, squareNumber);
        checkMoveValidity(playedSquare);
        squareController.playMoveAt(row, squareNumber, playerTurn);
        checkForEndOfGame();
        if (--numberOfMovesToPlay == 0) {
            resetNumberOfMovesToPlay();
            changePlayersTurn();
        }
    }

    public void checkMoveValidity(final Square square) throws IllegalMoveException {
        if (isConquered(square)) {
            throw new IllegalMoveException("This square is already conquered. It's not possible to play here.");
        }

        if (!square.isOnTheEdge()) {
            boolean connected = false;
            final List<Square> alreadyVisitedSquare = new ArrayList<>();
            alreadyVisitedSquare.add(square);
            for (final Square currentAdjacentSquare : square.getAdjacentSquares()) {
                if (isConnectedToTheEdge(currentAdjacentSquare, alreadyVisitedSquare, playerTurn)) {
                    connected = true;
                    break;
                }
            }
            if (!connected) {
                throw new IllegalMoveException("This square is not connected to the edge.");
            }
        }
    }

    
    public boolean isConnectedToTheEdge(final Square square, final List<Square> alreadyVisitedSquares, EPlayer player) {
        alreadyVisitedSquares.add(square);
        if (square.isOnTheEdge()) {
            if (square.containsPawnFromColor(player)) {
                return true;
            }
        } else {
            if (square.containsPawnFromColor(player)) {
                for (final Square neighbourSquare : square.getAdjacentSquares()) {
                    if (!alreadyVisitedSquares.contains(neighbourSquare)) {
                        if (isConnectedToTheEdge(neighbourSquare, alreadyVisitedSquares, player)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isConquered(final Square square) {
        return square.getConqueringPlayer() != null;
    }

    private void resetNumberOfMovesToPlay() {
        numberOfMovesToPlay = 2;
    }

    private void changePlayersTurn() {
        if (playerTurn == EPlayer.BLACK) {
            playerTurn = EPlayer.WHITE;
        } else {
            playerTurn = EPlayer.BLACK;
        }
    }

    private void checkForEndOfGame() {
        if (squareController.getNumberOfBlackConqueredSquares() >= 13) {
            JOptionPane.showMessageDialog(null, "Black wins !", "End of the game", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if (squareController.getNumberOfWhiteConqueredSquares()>= 13) {
            JOptionPane.showMessageDialog(null, "White wins !", "End of the game", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
}
