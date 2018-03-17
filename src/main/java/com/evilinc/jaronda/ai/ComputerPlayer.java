/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.ai;

import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.controller.RuleController;
import com.evilinc.jaronda.controller.SquareController;
import com.evilinc.jaronda.controller.TurnController;
import com.evilinc.jaronda.model.Move;
import com.evilinc.jaronda.model.Square;

/**
 *
 * @author teton
 */
public class ComputerPlayer {

    private final EPlayer player;
    private final SquareController squareController;
    private final TurnController turnController;
    private final int maxProof = 4;

    public ComputerPlayer(final EPlayer player) {
        this.player = player;
        squareController = new SquareController();
        turnController = new TurnController();
    }

    public int[] getMove(final Square[][] gameBoard) {
        final long startTime = System.currentTimeMillis();
        int temp;
        int max = Integer.MIN_VALUE;
        int maxRow = -1;
        int maxSquareNumber = -1;
        final int[] chosenMove = new int[2];
        squareController.setSquares(gameBoard);

        for (int row = 0; row < gameBoard.length; row++) {
            for (int squareNumber = 0; squareNumber < gameBoard[row].length; squareNumber++) {
                if (RuleController.isMoveLegal(squareController.getSquareAt(row, squareNumber), player)) {
                    final Move playedMove = squareController.playMoveAt(row, squareNumber, player);
                    turnController.playMove();
                    if (turnController.getCurrentPlayer() == player) {
                        temp = calculateMax(maxProof - 1);
                    } else {
                        temp = calculateMin(maxProof - 1);
                    }
                    if (temp > max) {
                        max = temp;
                        maxRow = row;
                        maxSquareNumber = squareNumber;
                    }
                    max = Math.max(max, temp);
                    turnController.cancelMove();
                    squareController.cancelMove(playedMove);
                }
            }
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Move calculated in: " + (endTime - startTime) + "ms");
        chosenMove[0] = maxRow;
        chosenMove[1] = maxSquareNumber;
        return chosenMove;
    }

    public int calculateMin(final int proof) {
        int temp;
        int min = Integer.MAX_VALUE;

        //Si on est à la profondeur voulue, on retourne l'évaluation
        if (proof == 0 || RuleController.isGameFinished(squareController, turnController.getNextPlayer())) {
            return evaluatePosition();
        }

        for (int row = 0; row < squareController.squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squareController.squares[row].length; squareNumber++) {
                if (RuleController.isMoveLegal(squareController.getSquareAt(row, squareNumber), player)) {
                    final Move playedMove = squareController.playMoveAt(row, squareNumber, player);
                    turnController.playMove();
                    if (turnController.getCurrentPlayer() == player) {
                        temp = calculateMax(proof - 1);
                    } else {
                        temp = calculateMin(proof - 1);
                    }
                    min = Math.min(min, temp);
                    squareController.cancelMove(playedMove);
                    turnController.cancelMove();
                }
            }
        }
        return min;
    }

    public int calculateMax(final int proof) {
        int temp;
        int max = Integer.MIN_VALUE;

        //Si on est à la profondeur voulue, on retourne l'évaluation
        if (proof == 0 || RuleController.isGameFinished(squareController, turnController.getNextPlayer())) {
            return evaluatePosition();
        }

        for (int row = 0; row < squareController.squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squareController.squares[row].length; squareNumber++) {
                if (RuleController.isMoveLegal(squareController.getSquareAt(row, squareNumber), player)) {
                    final Move playedMove = squareController.playMoveAt(row, squareNumber, player);
                    turnController.playMove();
                    if (turnController.getCurrentPlayer() == player) {
                        temp = calculateMax(proof - 1);
                    } else {
                        temp = calculateMin(proof - 1);
                    }
                    max = Math.max(max, temp);
                    squareController.cancelMove(playedMove);
                }
            }
        }
        return max;
    }

    private int evaluatePosition() {
        final EPlayer winner = RuleController.getWinner(squareController, turnController);
        final int numberOfConqueredBlackSquares = 10 * squareController.getNumberOfBlackConqueredSquares();
        final int numberOfConqueredWhiteSquares = 10 * squareController.getNumberOfWhiteConqueredSquares();
        if (winner == player) {
            return 1000 + (EPlayer.BLACK == player ? numberOfConqueredBlackSquares : numberOfConqueredWhiteSquares);
        } else if (winner != null) {
            return -1000 + (EPlayer.BLACK == player ? numberOfConqueredWhiteSquares : numberOfConqueredBlackSquares);
        }

        int cpuScore = 0;
        int opponentScore = 0;
        for (int row = 0; row < squareController.squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squareController.squares[row].length; squareNumber++) {
                final Square currentSquare = squareController.getSquareAt(row, squareNumber);
                final EPlayer conqueringPlayer = currentSquare.getConqueringPlayer();
                final int numberOfAdjacentSquaresScore = 10 * currentSquare.getAdjacentSquares().size();
                if (conqueringPlayer == null) {
                    cpuScore += numberOfAdjacentSquaresScore;
                    opponentScore += numberOfAdjacentSquaresScore;
                    if (player == EPlayer.BLACK) {
                        cpuScore += 10 * currentSquare.numberOfBlackPawns;
                        opponentScore += 10 * currentSquare.numberOfWhitePawns;
                    } else {
                        cpuScore += 10 * currentSquare.numberOfWhitePawns;
                        opponentScore += 10 * currentSquare.numberOfBlackPawns;
                    }
                } else {
                    if (conqueringPlayer == player) {
                        cpuScore += (numberOfAdjacentSquaresScore + 10 * currentSquare.getNecessaryPawnsToConquer());
                    } else {
                        opponentScore += (numberOfAdjacentSquaresScore + 10 * currentSquare.getNecessaryPawnsToConquer());
                    }
                }
            }
        }

        return cpuScore - opponentScore;
    }

}
