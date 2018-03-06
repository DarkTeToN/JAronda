/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.consts.MathsConstants;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.gui.BoardPanel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author teton
 */
public class GuiToModelController {

    private final BoardPanel boardPanel;
    private final GameController gameController;
    private final SquareController squareController;

    private int centerX = 0;
    private int centerY = 0;

    public GuiToModelController(final BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        this.gameController = GameController.getInstance();
        this.squareController = SquareController.getInstance();
        boardPanel.setSquaresToDraw(squareController.getSquareList());
        initListeners();
    }

    private void initListeners() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    updateCenterCoordinates();
                    playMoveAt(e.getPoint());
                }
            }
        });
    }

    private void updateCenterCoordinates() {
        centerX = boardPanel.getBoardCenterXCoordinate();
        centerY = boardPanel.getBoardCenterYCoordinate();
    }

    private void playMoveAt(final Point clickedPoint) {
        int[] playedSquare = getSquareFromBoardCoordinates(getBoardCoordinatesFromPanelCoordinates(clickedPoint));
        if (isPlayedSquareValid(playedSquare)) {
            try {
                gameController.playMoveAt(playedSquare[0], playedSquare[1]);
                boardPanel.setSquaresToDraw(squareController.getSquareList());
            } catch (IllegalMoveException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean isPlayedSquareValid(final int[] playedSquare) {
        return playedSquare[0] > -1 && playedSquare[1] > -1;
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
            theta += 2*Math.PI;
        }
        return theta;
    }

    private int getRow(final Point boardCoordinates) {
        final double distanceFromBoardCenter = Math.sqrt(Math.pow(boardCoordinates.x, 2) + Math.pow(boardCoordinates.y, 2));
        if (distanceFromBoardCenter < BoardPanel.FIRST_CIRCLE_RADIUS) {
            return 3;
        } else if (distanceFromBoardCenter < BoardPanel.SECOND_CIRCLE_RADIUS) {
            return 2;
        } else if (distanceFromBoardCenter < BoardPanel.THIRD_CIRCLE_RADIUS) {
            return 1;
        } else if (distanceFromBoardCenter < BoardPanel.FOURTH_CIRCLE_RADIUS) {
            return 0;
        } else {
            return -1;
        }
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
