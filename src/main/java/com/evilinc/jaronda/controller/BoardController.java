/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.consts.MathsConstants;
import com.evilinc.jaronda.gui.BoardPanel;
import com.evilinc.jaronda.model.Square;
import java.awt.Point;

/**
 *
 * @author teton
 */
public class BoardController {

    private final BoardPanel boardPanel;

    private int centerX = 0;
    private int centerY = 0;

    public BoardController(final BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    private void updateCenterCoordinates() {
        centerX = boardPanel.getBoardCenterXCoordinate();
        centerY = boardPanel.getBoardCenterYCoordinate();
    }

    public int[] getMoveCoordinates(final Point clickedPoint) {
        updateCenterCoordinates();
        return getSquareFromBoardCoordinates(getBoardCoordinatesFromPanelCoordinates(clickedPoint));
    }

    public void updateBoardPanel(Square[][] squares) {
        boardPanel.setSquaresToDraw(squares);
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

}
