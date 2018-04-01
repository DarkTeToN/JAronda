/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model.player;

import com.evilinc.jaronda.consts.EventConst;
import com.evilinc.jaronda.controller.game.RuleController;
import com.evilinc.jaronda.controller.game.SquareController;
import com.evilinc.jaronda.controller.game.TurnController;
import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.game.Move;
import com.evilinc.jaronda.model.game.Square;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;

/**
 *
 * @author teton
 */
public class AIPlayer extends APlayer {

    private final SquareController squareController;
    private final TurnController turnController;
    private final int maxDepth = 5;

    public AIPlayer(EPlayer color) {
        super(color);
        squareController = new SquareController();
        turnController = new TurnController();
    }

    @Override
    public void playMove(final Square[][] board, final Move lastPlayedMove) {
        Thread aiCalculusThread = new Thread(() -> {
            final int[] playedMove = getMove(board);
            SwingUtilities.invokeLater(() -> {
                eventController.firePropertyChange(EventConst.MOVE_PLAYED, AIPlayer.this, playedMove);
            });
        });
        aiCalculusThread.start();
    }

    public int[] getMove(final Square[][] gameBoard) {
        final long startTime = System.currentTimeMillis();
        int temp;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int borne = 0;
        Map<Integer, List<int[]>> movesMap = new HashMap<>();
        int maxRow = -1;
        int maxSquareNumber = -1;
        int[] chosenMove = new int[2];
        squareController.setSquares(gameBoard);

        for (int row = 0; row < gameBoard.length; row++) {
            for (int squareNumber = 0; squareNumber < gameBoard[row].length; squareNumber++) {
                if (RuleController.isMoveLegal(squareController.getSquareAt(row, squareNumber), color)) {
                    final Move playedMove = squareController.playMoveAt(row, squareNumber, color);
                    turnController.playMove();
                    if (turnController.getCurrentPlayer() == color) {
                        temp = calculateMax(max, Integer.MAX_VALUE, maxDepth - 1);
                        if (temp >= max) {
                            max = temp;
                            List<int[]> movesList = movesMap.get(temp);
                            if (movesList == null) {
                                movesList = new ArrayList();
                                movesMap.put(temp, movesList);
                            }
                            movesList.add(new int[]{row, squareNumber});
                            maxRow = row;
                            maxSquareNumber = squareNumber;
                            borne = max;
                        }
                    } else {
                        temp = calculateMin(min, Integer.MAX_VALUE, maxDepth - 1);
                        if (temp >= min) {
                            min = temp;
                            List<int[]> movesList = movesMap.get(temp);
                            if (movesList == null) {
                                movesList = new ArrayList();
                                movesMap.put(temp, movesList);
                            }
                            movesList.add(new int[]{row, squareNumber});
                            maxRow = row;
                            maxSquareNumber = squareNumber;
                            borne = min;
                        }
                    }
                    turnController.cancelMove();
                    squareController.cancelMove(playedMove);
                }
            }
        }
        final List<Integer> valuesList = new ArrayList<>(movesMap.keySet());
        final Integer maxVal = Collections.max(valuesList);
        final List<int[]> potentialMoves = movesMap.get(maxVal);
        chosenMove = potentialMoves.get((int) (potentialMoves.size() * Math.random()));
        final long endTime = System.currentTimeMillis();
        final long currentCalculationTime = endTime - startTime;
//        if (currentCalculationTime < MIN_REFERENCE_CALCULATION_TIME) {
//            maxProof++;
//            System.out.println("Too short: increasing maximum proof to " + maxProof);
//        } else if (currentCalculationTime > MAX_REFERENCE_CALCULATION_TIME) {
//            maxProof--;
//            System.out.println("Too long: decreasing maximum proof to " + maxProof);
//        }
//        System.out.println(numberOfCalculatedPositions + " moves calculated in: " + currentCalculationTime + "ms");
//        chosenMove[0] = maxRow;
//        chosenMove[1] = maxSquareNumber;
        return chosenMove;
    }

    public int calculateMin(int alpha, int beta, final int proof) {
        int temp;
        //Si on est à la profondeur voulue, on retourne l'évaluation
        if (proof == 0 || RuleController.isGameFinished(squareController, turnController.getNextPlayer())) {
            return evaluatePosition();
        }

        for (int row = 0; row < squareController.squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squareController.squares[row].length; squareNumber++) {
                if (RuleController.isMoveLegal(squareController.getSquareAt(row, squareNumber), color)) {
                    final Move playedMove = squareController.playMoveAt(row, squareNumber, color);
                    turnController.playMove();
                    if (turnController.getCurrentPlayer() == color) {
                        temp = calculateMax(alpha, beta, proof - 1);
                    } else {
                        temp = calculateMin(alpha, beta, proof - 1);
                    }
                    squareController.cancelMove(playedMove);
                    turnController.cancelMove();
                    beta = Math.min(beta, temp);
                    if (alpha > beta) {
                        return alpha;
                    }
                }
            }
        }
        return beta;
    }

    public int calculateMax(int alpha, int beta, final int proof) {
        int temp;

        //Si on est à la profondeur voulue, on retourne l'évaluation
        if (proof == 0 || RuleController.isGameFinished(squareController, turnController.getNextPlayer())) {
            return evaluatePosition();
        }

        for (int row = 0; row < squareController.squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squareController.squares[row].length; squareNumber++) {
                if (RuleController.isMoveLegal(squareController.getSquareAt(row, squareNumber), color)) {
                    final Move playedMove = squareController.playMoveAt(row, squareNumber, color);
                    turnController.playMove();
                    if (turnController.getCurrentPlayer() == color) {
                        temp = calculateMax(alpha, beta, proof - 1);
                    } else {
                        temp = calculateMin(alpha, beta, proof - 1);
                    }
                    squareController.cancelMove(playedMove);
                    turnController.cancelMove();
                    alpha = Math.max(alpha, temp);
                    if (beta < alpha) {
                        return beta;
                    }
                }
            }
        }
        return alpha;
    }

    private int evaluatePosition() {
        final EPlayer winner = RuleController.getWinner(squareController, turnController);
        final int numberOfConqueredBlackSquares = 10 * squareController.getNumberOfBlackConqueredSquares();
        final int numberOfConqueredWhiteSquares = 10 * squareController.getNumberOfWhiteConqueredSquares();
        if (winner == color) {
            return 10000 + (EPlayer.BLACK == color ? numberOfConqueredBlackSquares : numberOfConqueredWhiteSquares);
        } else if (winner != null) {
            return -10000 + (EPlayer.BLACK == color ? numberOfConqueredWhiteSquares : numberOfConqueredBlackSquares);
        }

        int cpuScore = 0;
        int opponentScore = 0;
        for (int row = 0; row < squareController.squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squareController.squares[row].length; squareNumber++) {
                final Square currentSquare = squareController.getSquareAt(row, squareNumber);
                final EPlayer conqueringPlayer = currentSquare.getConqueringPlayer();
                final int numberOfAdjacentSquaresScore = -10 * currentSquare.getAdjacentSquares().size();
                if (conqueringPlayer == null) {
                    cpuScore += numberOfAdjacentSquaresScore;
                    opponentScore += numberOfAdjacentSquaresScore;
                    if (color == EPlayer.BLACK) {
                        cpuScore += (10 + row + currentSquare.numberOfBlackPawns - currentSquare.numberOfWhitePawns) * currentSquare.numberOfBlackPawns;
                        opponentScore += (10 + row + currentSquare.numberOfWhitePawns - currentSquare.numberOfBlackPawns) * currentSquare.numberOfWhitePawns;

                    } else {
                        opponentScore += (10 + row + currentSquare.numberOfBlackPawns - currentSquare.numberOfWhitePawns) * currentSquare.numberOfBlackPawns;
                        cpuScore += (10 + row + currentSquare.numberOfWhitePawns - currentSquare.numberOfBlackPawns) * currentSquare.numberOfWhitePawns;
                    }
                } else {
                    if (conqueringPlayer == color) {
                        cpuScore += ((20 + row - 3) * currentSquare.getNecessaryPawnsToConquer());
                    } else {
                        opponentScore += ((20 + row - 3) * currentSquare.getNecessaryPawnsToConquer());
                    }
                }
            }
        }
        return cpuScore - opponentScore;
    }

}
