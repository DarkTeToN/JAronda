/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.game;

import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.model.game.Square;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author teton
 */
public class RuleController {

    public static boolean isMoveLegal(final Square square, final EPlayer playerToCheck) {
        try {
            checkMoveValidity(square, playerToCheck);
        } catch (IllegalMoveException ex) {
            return false;
        }
        return true;
    }

    public static void checkMoveValidity(final Square square, final EPlayer playerToCheck) throws IllegalMoveException {
        if (isConquered(square)) {
            throw new IllegalMoveException(String.format("Illegal move: The square %d,%d is already conquered.", square.getRow(), square.getSquareNumber()));
        }

        if (!square.isOnTheEdge()) {
            boolean connected = false;
            final List<Square> alreadyVisitedSquare = new ArrayList<>();
            alreadyVisitedSquare.add(square);
            for (final Square currentAdjacentSquare : square.getAdjacentSquares()) {
                if (isConnectedToTheEdge(currentAdjacentSquare, alreadyVisitedSquare, playerToCheck)) {
                    connected = true;
                    break;
                }
            }
            if (!connected) {
                throw new IllegalMoveException(String.format("Illegal move: The square %d,%d has no connexion to the edge.", square.getRow(), square.getSquareNumber()));
            }
        }
    }

    public static boolean isConnectedToTheEdge(final Square square, final List<Square> alreadyVisitedSquares, EPlayer player) {
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

    public static boolean isConquered(final Square square) {
        return square.getConqueringPlayer() != null;
    }

    public static boolean isGameFinished(final SquareController squareController, final EPlayer nextPlayer) {
        return (squareController.getNumberOfBlackConqueredSquares() >= 13)
                || (squareController.getNumberOfWhiteConqueredSquares() >= 13)
                || (!playerCanPlay(squareController, nextPlayer));
    }

    private static boolean playerCanPlay(final SquareController squareController, final EPlayer player) {
        boolean canPlay = false;
        for (final Square[] squareRow : squareController.getSquares()) {
            for (final Square currentSquare : squareRow) {
                try {
                    RuleController.checkMoveValidity(currentSquare, player);
                    canPlay = true;
                    break;
                } catch (final IllegalMoveException e) {
                }
            }
        }
        return canPlay;
    }

    public static EPlayer getWinner(final SquareController squareController, final TurnController turnController) {
        final EPlayer nextPlayer = turnController.getNextPlayer();
        if (squareController.getNumberOfBlackConqueredSquares() >= 13) {
            return EPlayer.BLACK;
        } else if (squareController.getNumberOfWhiteConqueredSquares() >= 13) {
            return EPlayer.WHITE;
        } else if (!playerCanPlay(squareController, nextPlayer)) {
            if (nextPlayer == EPlayer.BLACK) {
                return EPlayer.WHITE;
            } else {
                return EPlayer.BLACK;
            }
        }
        return null;
    }

}
