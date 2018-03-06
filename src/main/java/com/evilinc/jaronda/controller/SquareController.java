/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.EPlayer;
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
    private static final int FIRST_ROW_INDEX = 0;
    private static final int SECOND_ROW_INDEX = 1;
    private static final int THIRD_ROW_INDEX = 2;
    private static final int FOURTH_ROW_INDEX = 3;

    private final Square[][] squares;
    private static SquareController instance;
    private int numberOfBlackConqueredSquares = 0;
    private int numberOfWhiteConqueredSquares = 0;

    private SquareController() {
        squares = new Square[4][8];
        initializeSquares();
        initializeSquaresNeighbourood();
    }

    public static SquareController getInstance() {
        if (instance == null) {
            instance = new SquareController();
        }
        return instance;
    }

    private void initializeSquares() {
        for (int row = 0; row < 3; row++) {
            for (int squareNumber = 0; squareNumber < 8; squareNumber++) {
                switch (row) {
                    case 0:
                        initializeFirstRowSquares(squareNumber);
                        break;
                    case 1:
                        initializeSecondRowSquares(squareNumber);
                        break;
                    case 2:
                        initializeThirdRowSquares(squareNumber);
                        break;
                    default:
                        break;
                }
            }
        }
        final Square lastRowSquare = new Square(3, 0, 4, 0, 2 * Math.PI);
        for (int i = 0; i < 8; i++) {
            squares[FOURTH_ROW_INDEX][i] = lastRowSquare;
        }
    }

    private void initializeFirstRowSquares(final int squareNumber) {
        int numberOfPawnsToConquer = DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER;
        if (squareNumber % 2 == 0) {
            numberOfPawnsToConquer = 2;
        }
        switch (squareNumber) {
            case 0:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, -Math.PI / 12, Math.PI / 12);
                break;
            case 1:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, Math.PI / 12, 5 * Math.PI / 12);
                break;
            case 2:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 5 * Math.PI / 12, 7 * Math.PI / 12);
                break;
            case 3:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 7 * Math.PI / 12, 11 * Math.PI / 12);
                break;
            case 4:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 11 * Math.PI / 12, 13 * Math.PI / 12);
                break;
            case 5:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 13 * Math.PI / 12, 17 * Math.PI / 12);
                break;
            case 6:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 17 * Math.PI / 12, 19 * Math.PI / 12);
                break;
            case 7:
                squares[FIRST_ROW_INDEX][squareNumber] = new Square(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 19 * Math.PI / 12, 23 * Math.PI / 12);
                break;
            default:
                break;
        }
    }

    private void initializeSecondRowSquares(final int squareNumber) {
        int numberOfPawnsToConquer = DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER;
        if (squareNumber % 2 == 0) {
            numberOfPawnsToConquer = 4;
        }
        switch (squareNumber) {
            case 0:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, -2 * Math.PI / 12, 2 * Math.PI / 12);
                break;
            case 1:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 2 * Math.PI / 12, 4 * Math.PI / 12);
                break;
            case 2:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 4 * Math.PI / 12, 8 * Math.PI / 12);
                break;
            case 3:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 8 * Math.PI / 12, 10 * Math.PI / 12);
                break;
            case 4:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 10 * Math.PI / 12, 14 * Math.PI / 12);
                break;
            case 5:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 14 * Math.PI / 12, 16 * Math.PI / 12);
                break;
            case 6:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 16 * Math.PI / 12, 20 * Math.PI / 12);
                break;
            case 7:
                squares[SECOND_ROW_INDEX][squareNumber] = new Square(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 20 * Math.PI / 12, 22 * Math.PI / 12);
                break;
            default:
                break;
        }
    }

    private void initializeThirdRowSquares(final int squareNumber) {
        switch (squareNumber) {
            case 0:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 0, Math.PI / 4);
                break;
            case 1:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, Math.PI / 4, Math.PI / 2);
                break;
            case 2:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, Math.PI / 2, 3 * Math.PI / 4);
                break;
            case 3:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 3 * Math.PI / 4, Math.PI);
                break;
            case 4:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, Math.PI, 5 * Math.PI / 4);
                break;
            case 5:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 5 * Math.PI / 4, 3 * Math.PI / 2);
                break;
            case 6:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 3 * Math.PI / 2, 7 * Math.PI / 4);
                break;
            case 7:
                squares[THIRD_ROW_INDEX][squareNumber] = new Square(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 7 * Math.PI / 4, 2 * Math.PI);
                break;
            default:
                break;
        }
    }

    private void initializeSquaresNeighbourood() {
        for (int row = 0; row < 4; row++) {
            for (int squareNumber = 0; squareNumber < 8; squareNumber++) {
                final Square currentSquare = squares[row][squareNumber];
                final Set<Square> neighbours = new HashSet<>();
                neighbours.add(squares[row][((squareNumber + 8) - 1) % 8]);
                neighbours.add(squares[row][(squareNumber + 1) % 8]);
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
        for (int row = 0; row < 4; row++) {
            for (int squareNumber = 0; squareNumber < 8; squareNumber++) {
                squares[row][squareNumber].reset();
            }
        }
    }

    public Square getSquareAt(final int row, final int squareNumber) {
        return squares[row][squareNumber];
    }

    public List<Square> getSquareList() {
        List<Square> squaresList = new ArrayList<>();
        for (Square[] squareArray : squares) {
            squaresList.addAll(Arrays.asList(squareArray));
        }
        return squaresList;
    }

    public void playMoveAt(final int row, final int squareNumber, final EPlayer currentPlayer) {
        final Square playedSquare = squares[row][squareNumber];
        playedSquare.addPawn(currentPlayer);
        updateConqueredStatus();
        updateConqueredSquaresCounts();
    }

    public int getNumberOfBlackConqueredSquares() {
        return numberOfBlackConqueredSquares;
    }

    public int getNumberOfWhiteConqueredSquares() {
        return numberOfWhiteConqueredSquares;
    }

    private void updateConqueredStatus() {
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
                            currentSquare.setConqueringPlayer(EPlayer.BLACK);
                            newConqueredSquares = true;
                        } else if (whiteConqueredNeighbourSquares >= currentSquare.getNecessaryPawnsToConquer()) {
                            currentSquare.setConqueringPlayer(EPlayer.WHITE);
                            newConqueredSquares = true;
                        }
                    }
                }
            }
        } while (newConqueredSquares == true);
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
