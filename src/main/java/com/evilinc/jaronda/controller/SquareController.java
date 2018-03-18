/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.model.Move;
import com.evilinc.jaronda.model.JsonSquare;
import com.evilinc.jaronda.model.Square;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author teton
 */
public class SquareController {

    private static final int DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER = 3;

    public Square[][] squares;
    private int numberOfBlackConqueredSquares = 0;
    private int numberOfWhiteConqueredSquares = 0;

    public SquareController() {
        squares = new Square[4][];
        for (int row = 0; row < 4; row++) {
            switch (row) {
                case 3:
                    squares[row] = new Square[1];
                    break;
                default:
                    squares[row] = new Square[8];
                    break;
            }
        }
        initializeSquares();
        initializeSquaresNeighbourood();
    }

    private void initializeSquares() {
        for (int row = 0; row < squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squares[row].length; squareNumber++) {
                int numberOfPawnsToConquer = DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER;
                switch (row) {
                    case 0:
                        if (squareNumber % 2 == 0) {
                            numberOfPawnsToConquer = 2;
                        }
                        break;
                    case 1:
                        if (squareNumber % 2 == 0) {
                            numberOfPawnsToConquer = 4;
                        }
                    default:
                        break;
                }
                squares[row][squareNumber] = new Square(row, squareNumber, numberOfPawnsToConquer);
            }
        }
        squares[3][0] = new Square(3, 0, 4);
    }

    private void initializeSquaresNeighbourood() {
        for (int row = 0; row < squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squares[row].length; squareNumber++) {
                final Square currentSquare = squares[row][squareNumber];
                final Set<Square> neighbours = new HashSet<>();
                if (row != 3) {
                    neighbours.add(squares[row][((squareNumber + 8) - 1) % 8]);
                    neighbours.add(squares[row][(squareNumber + 1) % 8]);
                }
                switch (row) {
                    case 0:
                        neighbours.add(squares[row + 1][squareNumber]);
                        if (squareNumber % 2 != 0) {
                            neighbours.add(squares[row + 1][((squareNumber + 8) - 1) % 8]);
                            neighbours.add(squares[row + 1][(squareNumber + 1) % 8]);
                        }
                        break;
                    case 1:
                        neighbours.add(squares[row - 1][squareNumber]);
                        neighbours.add(squares[row + 1][((squareNumber + 8) - 1) % 8]);
                        neighbours.add(squares[row + 1][squareNumber]);
                        if (squareNumber % 2 == 0) {
                            neighbours.add(squares[row - 1][((squareNumber + 8) - 1) % 8]);
                            neighbours.add(squares[row - 1][(squareNumber + 1) % 8]);
                        }
                        break;
                    case 2:
                        neighbours.add(squares[3][0]);
                        neighbours.add(squares[row - 1][squareNumber]);
                        neighbours.add(squares[row - 1][(squareNumber + 1) % 8]);
                        break;
                    case 3:
                        neighbours.clear();
                        for (int i = 0; i < 8; i++) {
                            neighbours.add(squares[2][i]);
                        }
                        break;
                    default:
                        break;
                }
                currentSquare.setAdjacentSquares(neighbours);
            }
        }
    }

    public void reset() {
        for (int row = 0; row < squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squares[row].length; squareNumber++) {
                squares[row][squareNumber].reset();
            }
        }
    }

    public Square getSquareAt(final int row, final int squareNumber) {
        return squares[row][squareNumber];
    }

    public Square[][] getSquares() {
        return squares;
    }

    public List<Square> getSquareList() {
        List<Square> squaresList = new ArrayList<>();
        for (Square[] squareArray : squares) {
            squaresList.addAll(Arrays.asList(squareArray));
        }
        return squaresList;
    }

    public void setSquareList(final List<Square> squareList) {
        for (final Square currentSquare : squareList) {
            squares[currentSquare.getRow()][currentSquare.getSquareNumber()] = currentSquare;
        }
        updateConqueredSquaresCounts();
    }

    public void setSquares(final Square[][] squares) {
        this.squares = squares;
        updateConqueredSquaresCounts();
    }

    public Move playMoveAt(final int row, final int squareNumber, final EPlayer currentPlayer) {
        final Square playedSquare = squares[row][squareNumber];
        final JsonSquare currentSquareBeforeMove = new JsonSquare(playedSquare);
        boolean isConquered = playedSquare.addPawn(currentPlayer);
        final List<JsonSquare> newlyConqueredSquares = updateConqueredStatus();
        if (isConquered) {
            newlyConqueredSquares.add(currentSquareBeforeMove);
        }
        updateConqueredSquaresCounts();
        return new Move(currentPlayer, row, squareNumber, newlyConqueredSquares);
    }

    public void cancelMove(final Move moveToCancel) {
        final List<JsonSquare> conqueredSquares = moveToCancel.getConqueredSquares();
        if (conqueredSquares.isEmpty()) {
            final Square squareToReinit = getSquareAt(moveToCancel.getRow(), moveToCancel.getSquareNumber());
            if (moveToCancel.getPlayer() == EPlayer.BLACK) {
                squareToReinit.numberOfBlackPawns--;
            } else {
                squareToReinit.numberOfWhitePawns--;
            }
        } else {
            for (final JsonSquare currentSquare : conqueredSquares) {
                final Square squareToReinit = getSquareAt(currentSquare.row, currentSquare.squareNumber);
                squareToReinit.conqueringPlayer = null;
                squareToReinit.numberOfBlackPawns = currentSquare.numberOfBlackPawns;
                squareToReinit.numberOfWhitePawns = currentSquare.numberOfWhitePawns;
            }
        }
    }

    public int getNumberOfBlackConqueredSquares() {
        return numberOfBlackConqueredSquares;
    }

    public int getNumberOfWhiteConqueredSquares() {
        return numberOfWhiteConqueredSquares;
    }

    private List<JsonSquare> updateConqueredStatus() {
        final List<JsonSquare> newlyConqueredSquares = new ArrayList<>();
        boolean newConqueredSquares = false;
        do {
            newConqueredSquares = false;
            for (int row = 0; row < squares.length; row++) {
                for (int squareNumber = 0; squareNumber < squares[row].length; squareNumber++) {
                    final Square currentSquare = squares[row][squareNumber];
                    if (currentSquare.getConqueringPlayer() == null) {
                        int blackConqueredNeighbourSquares = 0;
                        int whiteConqueredNeighbourSquares = 0;
                        for (final Square neighbourSquare : currentSquare.getAdjacentSquares()) {
                            if (neighbourSquare.getConqueringPlayer() == EPlayer.BLACK) {
                                blackConqueredNeighbourSquares++;
                            } else if (neighbourSquare.getConqueringPlayer() == EPlayer.WHITE) {
                                whiteConqueredNeighbourSquares++;
                            }
                        }
                        if (blackConqueredNeighbourSquares >= currentSquare.getNecessaryPawnsToConquer()) {
                            newlyConqueredSquares.add(new JsonSquare(currentSquare));
                            currentSquare.setConqueringPlayer(EPlayer.BLACK);
                            newConqueredSquares = true;
                        } else if (whiteConqueredNeighbourSquares >= currentSquare.getNecessaryPawnsToConquer()) {
                            newlyConqueredSquares.add(new JsonSquare(currentSquare));
                            currentSquare.setConqueringPlayer(EPlayer.WHITE);
                            newConqueredSquares = true;
                        }
                    }
                }
            }
        } while (newConqueredSquares == true);
        return newlyConqueredSquares;
    }

    private void resetConqueredSquaresCounts() {
        numberOfBlackConqueredSquares = 0;
        numberOfWhiteConqueredSquares = 0;
    }

    private void updateConqueredSquaresCounts() {
        resetConqueredSquaresCounts();
        for (int row = 0; row < squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squares[row].length; squareNumber++) {
                if (row < 3) {
                    final EPlayer conqueringPlayer = squares[row][squareNumber].getConqueringPlayer();
                    if (conqueringPlayer == EPlayer.BLACK) {
                        numberOfBlackConqueredSquares++;
                    } else if (conqueringPlayer == EPlayer.WHITE) {
                        numberOfWhiteConqueredSquares++;
                    }
                } else if (squareNumber == 0) {
                    final EPlayer conqueringPlayer = squares[row][squareNumber].getConqueringPlayer();
                    if (conqueringPlayer == EPlayer.BLACK) {
                        numberOfBlackConqueredSquares++;
                    } else if (conqueringPlayer == EPlayer.WHITE) {
                        numberOfWhiteConqueredSquares++;
                    }
                }
            }
        }
    }
}
