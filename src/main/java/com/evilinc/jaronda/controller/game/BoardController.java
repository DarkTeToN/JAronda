/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.game;

import com.evilinc.jaronda.consts.MathsConstants;
import com.evilinc.jaronda.gui.BoardPanel;
import com.evilinc.jaronda.model.game.GuiSquare;
import com.evilinc.jaronda.model.game.Square;
import java.awt.Point;

/**
 *
 * @author teton
 */
public class BoardController {

    private static final int DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER = 3;
    private static final int FIRST_ROW_INDEX = 0;
    private static final int SECOND_ROW_INDEX = 1;
    private static final int THIRD_ROW_INDEX = 2;
    private static final int FOURTH_ROW_INDEX = 3;

    private final BoardPanel boardPanel;

    private int centerX = 0;
    private int centerY = 0;
    private final GuiSquare[][] guiSquares;

    public BoardController(final BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        guiSquares = new GuiSquare[4][];
        for (int row = 0; row < 4; row++) {
            switch (row) {
                case 3:
                    guiSquares[row] = new GuiSquare[1];
                    break;
                default:
                    guiSquares[row] = new GuiSquare[8];
                    break;
            }
        }
        initializeSquares();
    }

    private void updateCenterCoordinates() {
        centerX = boardPanel.getBoardCenterXCoordinate();
        centerY = boardPanel.getBoardCenterYCoordinate();
    }

    public int[] getMoveCoordinates(final Point clickedPoint) {
        updateCenterCoordinates();
        return getSquareFromBoardCoordinates(getBoardCoordinatesFromPanelCoordinates(clickedPoint));
    }

    public void updateBoardPanel(Square[][] squares, final Square lastPlayedSquare) {
        for (int row = 0; row < squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squares[row].length; squareNumber++) {
                final Square currentSquare = squares[row][squareNumber];
                final GuiSquare currentGuiSquare = guiSquares[row][squareNumber];
                currentGuiSquare.conqueringPlayer = currentSquare.conqueringPlayer;
                currentGuiSquare.numberOfBlackPawns = currentSquare.numberOfBlackPawns;
                currentGuiSquare.numberOfWhitePawns = currentSquare.numberOfWhitePawns;
                currentGuiSquare.isLastPlayedMove = (lastPlayedSquare != null && 
                        lastPlayedSquare.getRow() == row && 
                        lastPlayedSquare.getSquareNumber() == squareNumber);
            }
        }
        boardPanel.setSquaresToDraw(guiSquares);
    }

    private Point getBoardCoordinatesFromPanelCoordinates(final Point clickedPoint) {
        final Point boardCoordinates = new Point();
        boardCoordinates.x = clickedPoint.x - centerX;
        boardCoordinates.y = clickedPoint.y - centerY;
        return boardCoordinates;
    }

    private int[] getSquareFromBoardCoordinates(final Point boardCoordinates) {
        int[] square = new int[2];
        final int row = getRow(boardCoordinates);
        final double theta = getAngle(boardCoordinates);
        final double cosTheta = Math.cos(theta);
        int squareNumber = -1;
        switch (row) {
            case 0:
                squareNumber = getSquareIndexFromFirstRow(cosTheta, boardCoordinates.y);
                break;
            case 1:
                squareNumber = getSquareIndexFromSecondRow(cosTheta, boardCoordinates.y);
                break;
            case 2:
                squareNumber = getSquareIndexFromThirdRow(cosTheta, boardCoordinates.y);
                break;
            case 3:
                squareNumber = getSquareIndexFromFourthRow();
                break;
            default:
                break;
        }
        square[0] = row;
        square[1] = squareNumber;
        return square;
    }

    private double getAngle(final Point boardCoordinates) {
        double theta = Math.atan2(boardCoordinates.y, boardCoordinates.x);
        if (boardCoordinates.y < 0) {
            theta += 2 * Math.PI;
        }
        return theta;
    }

    private int getRow(final Point boardCoordinates) {
        final double distanceFromBoardCenter = Math.sqrt(Math.pow(boardCoordinates.x, 2) + Math.pow(boardCoordinates.y, 2));
        return new Double(4 - Math.ceil(distanceFromBoardCenter / boardPanel.getFirstCircleRadius())).intValue();
    }

    private int getSquareIndexFromFirstRow(final double cosTheta, final int y) {
        int squareIndex;
        if (cosTheta > MathsConstants.COS_PI_12) {
            squareIndex = 0;
        } else if (cosTheta > MathsConstants.COS_5PI_12) {
            squareIndex = 1;
        } else if (cosTheta > MathsConstants.COS_7PI_12) {
            squareIndex = 2;
        } else if (cosTheta > MathsConstants.COS_11PI_12) {
            squareIndex = 3;
        } else {
            squareIndex = 4;
        }
        if (y < 0) {
            squareIndex = (8 - squareIndex) % 8;
        }
        return squareIndex;
    }

    private int getSquareIndexFromSecondRow(final double cosTheta, final int y) {
        int squareIndex;
        if (cosTheta > MathsConstants.COS_2PI_12) {
            squareIndex = 0;
        } else if (cosTheta > MathsConstants.COS_4PI_12) {
            squareIndex = 1;
        } else if (cosTheta > MathsConstants.COS_8PI_12) {
            squareIndex = 2;
        } else if (cosTheta > MathsConstants.COS_10PI_12) {
            squareIndex = 3;
        } else {
            squareIndex = 4;
        }

        if (y < 0) {
            squareIndex = (8 - squareIndex) % 8;
        }
        return squareIndex;
    }

    private int getSquareIndexFromThirdRow(final double cosTheta, final int y) {
        int squareIndex;
        if (cosTheta > MathsConstants.COS_PI_4) {
            squareIndex = 0;
        } else if (cosTheta > MathsConstants.COS_PI_2) {
            squareIndex = 1;
        } else if (cosTheta > MathsConstants.COS_3PI_4) {
            squareIndex = 2;
        } else {
            squareIndex = 3;
        }

        if (y < 0) {
            squareIndex = 7 - squareIndex;
        }
        return squareIndex;
    }

    private int getSquareIndexFromFourthRow() {
        return 0;
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
        guiSquares[FOURTH_ROW_INDEX][0] = new GuiSquare(3, 0, 4, 0, 2 * Math.PI);
    }

    private void initializeFirstRowSquares(final int squareNumber) {
        int numberOfPawnsToConquer = DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER;
        if (squareNumber % 2 == 0) {
            numberOfPawnsToConquer = 2;
        }
        switch (squareNumber) {
            case 0:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, -Math.PI / 12, Math.PI / 12);
                break;
            case 1:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, Math.PI / 12, 5 * Math.PI / 12);
                break;
            case 2:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 5 * Math.PI / 12, 7 * Math.PI / 12);
                break;
            case 3:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 7 * Math.PI / 12, 11 * Math.PI / 12);
                break;
            case 4:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 11 * Math.PI / 12, 13 * Math.PI / 12);
                break;
            case 5:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 13 * Math.PI / 12, 17 * Math.PI / 12);
                break;
            case 6:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 17 * Math.PI / 12, 19 * Math.PI / 12);
                break;
            case 7:
                guiSquares[FIRST_ROW_INDEX][squareNumber] = new GuiSquare(FIRST_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 19 * Math.PI / 12, 23 * Math.PI / 12);
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
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, -2 * Math.PI / 12, 2 * Math.PI / 12);
                break;
            case 1:
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 2 * Math.PI / 12, 4 * Math.PI / 12);
                break;
            case 2:
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 4 * Math.PI / 12, 8 * Math.PI / 12);
                break;
            case 3:
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 8 * Math.PI / 12, 10 * Math.PI / 12);
                break;
            case 4:
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 10 * Math.PI / 12, 14 * Math.PI / 12);
                break;
            case 5:
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 14 * Math.PI / 12, 16 * Math.PI / 12);
                break;
            case 6:
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 16 * Math.PI / 12, 20 * Math.PI / 12);
                break;
            case 7:
                guiSquares[SECOND_ROW_INDEX][squareNumber] = new GuiSquare(SECOND_ROW_INDEX, squareNumber, numberOfPawnsToConquer, 20 * Math.PI / 12, 22 * Math.PI / 12);
                break;
            default:
                break;
        }
    }

    private void initializeThirdRowSquares(final int squareNumber) {
        switch (squareNumber) {
            case 0:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 0, Math.PI / 4);
                break;
            case 1:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, Math.PI / 4, Math.PI / 2);
                break;
            case 2:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, Math.PI / 2, 3 * Math.PI / 4);
                break;
            case 3:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 3 * Math.PI / 4, Math.PI);
                break;
            case 4:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, Math.PI, 5 * Math.PI / 4);
                break;
            case 5:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 5 * Math.PI / 4, 3 * Math.PI / 2);
                break;
            case 6:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 3 * Math.PI / 2, 7 * Math.PI / 4);
                break;
            case 7:
                guiSquares[THIRD_ROW_INDEX][squareNumber] = new GuiSquare(THIRD_ROW_INDEX, squareNumber, DEFAULT_NUMBER_OF_PAWNS_TO_CONQUER, 7 * Math.PI / 4, 2 * Math.PI);
                break;
            default:
                break;
        }
    }

}
