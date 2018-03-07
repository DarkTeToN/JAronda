/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import com.evilinc.jaronda.model.Square;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author DarkTeToN
 */
public class BoardPanel extends JPanel {

    // Circles radiuses
    public static final int FIRST_CIRCLE_RADIUS = 75;
    public static final int FIRST_CIRCLE_DIAMETER = 2 * FIRST_CIRCLE_RADIUS;
    public static final int SECOND_CIRCLE_DIAMETER = 2 * FIRST_CIRCLE_DIAMETER;
    public static final int SECOND_CIRCLE_RADIUS = 2 * FIRST_CIRCLE_RADIUS;
    public static final int THIRD_CIRCLE_DIAMETER = 3 * FIRST_CIRCLE_DIAMETER;
    public static final int THIRD_CIRCLE_RADIUS = 3 * FIRST_CIRCLE_RADIUS;
    public static final int FOURTH_CIRCLE_DIAMETER = 4 * FIRST_CIRCLE_DIAMETER;
    public static final int FOURTH_CIRCLE_RADIUS = 4 * FIRST_CIRCLE_RADIUS;

    private final int DOT_RADIUS = 4;
    private final int DOT_DIAMETER = 2 * DOT_RADIUS;
    private final int NEGATIVE_MARGIN = 3 * DOT_RADIUS;
    private final int POSITIVE_MARGIN = DOT_RADIUS;

    private static final int SMALL_PAWN_RADIUS = 8;
    private static final int SMALL_PAWN_DIAMETER = 2 * SMALL_PAWN_RADIUS;

    private static final int CONQUERED_DOT_RADIUS = 25;
    private static final int CONQUERED_DOT_DIAMETER = 2 * CONQUERED_DOT_RADIUS;

    private static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 255);

    // Board center coordinates
    private int BOARD_CENTER_X;
    private int BOARD_CENTER_Y;

    private int FOURTH_CIRCLE_ORIGIN_X;
    private int FOURTH_CIRCLE_ORIGIN_Y;
    private int THIRD_CIRCLE_ORIGIN_X;
    private int THIRD_CIRCLE_ORIGIN_Y;
    private int SECOND_CIRCLE_ORIGIN_X;
    private int SECOND_CIRCLE_ORIGIN_Y;
    private int FIRST_CIRCLE_ORIGIN_X;
    private int FIRST_CIRCLE_ORIGIN_Y;

    private Square[][] squaresToDraw;
    private final BufferedImage texture;

    public BoardPanel() {
        // TODO: Reuse the coordinates stored in the Square object to calculate dynamically all the lines coordinates
        recalculateCoordinates();
        URL imageUrl = getClass().getResource("/com/evilinc/jaronda/background/wood.jpg");
        texture = toBufferedImage(new ImageIcon(imageUrl).getImage());
    }

    private void recalculateCoordinates() {
        recaculatePanelCenter();
        recalculateCirclesOrigins();
    }

    private BufferedImage toBufferedImage(Image image) {
        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        // copy the original image
        Graphics g = bi.createGraphics();

        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bi;
    }

    private void recaculatePanelCenter() {
        BOARD_CENTER_X = (int) getWidth() / 2;
        BOARD_CENTER_Y = (int) getHeight() / 2;
    }

    private void recalculateCirclesOrigins() {
        FOURTH_CIRCLE_ORIGIN_X = BOARD_CENTER_X - FOURTH_CIRCLE_RADIUS;
        FOURTH_CIRCLE_ORIGIN_Y = BOARD_CENTER_Y - FOURTH_CIRCLE_RADIUS;
        THIRD_CIRCLE_ORIGIN_X = BOARD_CENTER_X - THIRD_CIRCLE_RADIUS;
        THIRD_CIRCLE_ORIGIN_Y = BOARD_CENTER_Y - THIRD_CIRCLE_RADIUS;
        SECOND_CIRCLE_ORIGIN_X = BOARD_CENTER_X - SECOND_CIRCLE_RADIUS;
        SECOND_CIRCLE_ORIGIN_Y = BOARD_CENTER_Y - SECOND_CIRCLE_RADIUS;
        FIRST_CIRCLE_ORIGIN_X = BOARD_CENTER_X - FIRST_CIRCLE_RADIUS;
        FIRST_CIRCLE_ORIGIN_Y = BOARD_CENTER_Y - FIRST_CIRCLE_RADIUS;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Draw the board
        recalculateCoordinates();
        drawCircles(g);
        g.setColor(Color.BLACK);
        for (final Square[] squareRow : squaresToDraw) {
            for (final Square currentSquare : squareRow) {
                final int row = currentSquare.getRow();
                if (row < 3) {
                    double x1 = BOARD_CENTER_X + (4 - row) * FIRST_CIRCLE_RADIUS * Math.cos(currentSquare.getLeftLimitAngle());
                    double y1 = BOARD_CENTER_Y + (4 - row) * FIRST_CIRCLE_RADIUS * Math.sin(currentSquare.getLeftLimitAngle());
                    double x2 = BOARD_CENTER_X + (4 - row - 1) * FIRST_CIRCLE_RADIUS * Math.cos(currentSquare.getLeftLimitAngle());
                    double y2 = BOARD_CENTER_Y + (4 - row - 1) * FIRST_CIRCLE_RADIUS * Math.sin(currentSquare.getLeftLimitAngle());
                    g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                }
            }
        }
        // Draw the dots in the squares
        drawDots(g);

        // Draw pawns
        drawPawns(g);
    }

    private void drawCircles(final Graphics g) {
        // Draw the circles
        g.setColor(Color.YELLOW);

        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D tr = new Rectangle2D.Double(FOURTH_CIRCLE_ORIGIN_X, FOURTH_CIRCLE_ORIGIN_Y, texture.getWidth(), texture.getHeight());
        TexturePaint tp = new TexturePaint(texture, tr);

        // draw texture filled ellipse, but with antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D e = new Ellipse2D.Float(FOURTH_CIRCLE_ORIGIN_X, FOURTH_CIRCLE_ORIGIN_Y, FOURTH_CIRCLE_DIAMETER, FOURTH_CIRCLE_DIAMETER);
        g2d.setPaint(tp);
        g2d.fill(e);

        g.fillOval(FOURTH_CIRCLE_ORIGIN_X, FOURTH_CIRCLE_ORIGIN_Y, FOURTH_CIRCLE_DIAMETER, FOURTH_CIRCLE_DIAMETER);
        g.setColor(Color.BLACK);
        g.drawOval(FOURTH_CIRCLE_ORIGIN_X, FOURTH_CIRCLE_ORIGIN_Y, FOURTH_CIRCLE_DIAMETER, FOURTH_CIRCLE_DIAMETER);
        g.drawOval(THIRD_CIRCLE_ORIGIN_X, THIRD_CIRCLE_ORIGIN_Y, THIRD_CIRCLE_DIAMETER, THIRD_CIRCLE_DIAMETER);
        g.drawOval(SECOND_CIRCLE_ORIGIN_X, SECOND_CIRCLE_ORIGIN_Y, SECOND_CIRCLE_DIAMETER, SECOND_CIRCLE_DIAMETER);
        g.drawOval(FIRST_CIRCLE_ORIGIN_X, FIRST_CIRCLE_ORIGIN_Y, FIRST_CIRCLE_DIAMETER, FIRST_CIRCLE_DIAMETER);
    }

    private void drawDots(final Graphics g) {
        drawFirstRowDots(g);
        drawSecondRowDots(g);
        drawCenterSquareDots(g);
    }

    private void drawFirstRowDots(final Graphics g) {
        drawDotsInNorthSquareFirstRow(g);
        drawDotsInSouthSquareFirstRow(g);
        drawDotsInEastSquareFirstRow(g);
        drawDotsInWestSquareFirstRow(g);

    }

    private void drawSecondRowDots(final Graphics g) {
        drawDotsInNorthSquareSecondRow(g);
        drawDotsInSouthSquareSecondRow(g);
        drawDotsInEastSquareSecondRow(g);
        drawDotsInWestSquareSecondRow(g);
    }

    private void drawCenterSquareDots(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // Center square
        g.fillOval(BOARD_CENTER_X - DOT_RADIUS, BOARD_CENTER_Y - NEGATIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - DOT_RADIUS, BOARD_CENTER_Y + POSITIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - NEGATIVE_MARGIN, BOARD_CENTER_Y - DOT_RADIUS, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + POSITIVE_MARGIN, BOARD_CENTER_Y - DOT_RADIUS, DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawDotsInNorthSquareFirstRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // North square
        g.fillOval(BOARD_CENTER_X - NEGATIVE_MARGIN,
                BOARD_CENTER_Y - THIRD_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + POSITIVE_MARGIN,
                BOARD_CENTER_Y - THIRD_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawDotsInSouthSquareFirstRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // South square
        g.fillOval(BOARD_CENTER_X - NEGATIVE_MARGIN,
                BOARD_CENTER_Y + THIRD_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + POSITIVE_MARGIN,
                BOARD_CENTER_Y + THIRD_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawDotsInEastSquareFirstRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // East square
        g.fillOval(BOARD_CENTER_X + THIRD_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y - NEGATIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + THIRD_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y + POSITIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);

    }

    private void drawDotsInWestSquareFirstRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // West square
        g.fillOval(BOARD_CENTER_X - THIRD_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y - NEGATIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - THIRD_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y + POSITIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawDotsInNorthSquareSecondRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // Middle North square
        g.fillOval(BOARD_CENTER_X - NEGATIVE_MARGIN,
                BOARD_CENTER_Y - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + POSITIVE_MARGIN,
                BOARD_CENTER_Y - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - DOT_RADIUS,
                BOARD_CENTER_Y - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - NEGATIVE_MARGIN,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - DOT_RADIUS,
                BOARD_CENTER_Y - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) + POSITIVE_MARGIN,
                DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawDotsInSouthSquareSecondRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // Middle South square
        g.fillOval(BOARD_CENTER_X - NEGATIVE_MARGIN,
                BOARD_CENTER_Y + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + POSITIVE_MARGIN,
                BOARD_CENTER_Y + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - DOT_RADIUS,
                BOARD_CENTER_Y + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - NEGATIVE_MARGIN,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - DOT_RADIUS,
                BOARD_CENTER_Y + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) + POSITIVE_MARGIN,
                DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawDotsInEastSquareSecondRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // Middle East square
        g.fillOval(BOARD_CENTER_X + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y - NEGATIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y + POSITIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) - NEGATIVE_MARGIN,
                BOARD_CENTER_Y - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X + SECOND_CIRCLE_RADIUS + (FIRST_CIRCLE_RADIUS / 2) + POSITIVE_MARGIN,
                BOARD_CENTER_Y - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawDotsInWestSquareSecondRow(final Graphics g) {
        g.setColor(TRANSPARENT_BLACK);
        // Middle West square
        g.fillOval(BOARD_CENTER_X - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y - NEGATIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - DOT_RADIUS,
                BOARD_CENTER_Y + POSITIVE_MARGIN, DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) - NEGATIVE_MARGIN,
                BOARD_CENTER_Y - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
        g.fillOval(BOARD_CENTER_X - SECOND_CIRCLE_RADIUS - (FIRST_CIRCLE_RADIUS / 2) + POSITIVE_MARGIN,
                BOARD_CENTER_Y - DOT_RADIUS,
                DOT_DIAMETER, DOT_DIAMETER);
    }

    private void drawPawns(final Graphics g) {
        if (squaresToDraw != null) {
            for (final Square[] squareRow : squaresToDraw) {
                for (final Square currentSquare : squareRow) {
                    drawSquare(g, currentSquare);
                }
            }
        }
    }

    private void drawSquare(final Graphics g, final Square squareToDraw) {
        if (squareToDraw.getConqueringPlayer() != null) {
            drawConqueredSquare(g, squareToDraw);
        } else if (squareToDraw.getNumberOfBlackPawns() > 0 || squareToDraw.getNumberOfWhitePawns() > 0) {
            drawSmallPawn(g, squareToDraw);
        }
    }

    private void drawSmallPawn(final Graphics g, final Square squareToDraw) {
        Color currentColor = Color.BLACK;
        final double meanAngle = (squareToDraw.getRightLimitAngle() - squareToDraw.getLeftLimitAngle()) / 2;
        final double pawnSpacingAngle = meanAngle / squareToDraw.getNecessaryPawnsToConquer();
        final double distanceFromCenter = (3.5 - squareToDraw.getRow()) * FIRST_CIRCLE_RADIUS;
        for (int blackPanwsIndex = 0; blackPanwsIndex < squareToDraw.getNumberOfBlackPawns(); blackPanwsIndex++) {
            final double pawnAngle = (blackPanwsIndex + 1) * pawnSpacingAngle;
            double yCoordinate = BOARD_CENTER_Y + Math.sin(squareToDraw.getLeftLimitAngle() + pawnAngle) * distanceFromCenter - SMALL_PAWN_RADIUS;
            double xCoordinate = BOARD_CENTER_X + Math.cos(squareToDraw.getLeftLimitAngle() + pawnAngle) * distanceFromCenter - SMALL_PAWN_RADIUS;
            g.setColor(currentColor);
            g.fillOval((int) xCoordinate,
                    (int) yCoordinate,
                    SMALL_PAWN_DIAMETER, SMALL_PAWN_DIAMETER);
            g.setColor(Color.BLACK);
            g.drawOval((int) xCoordinate,
                    (int) yCoordinate,
                    SMALL_PAWN_DIAMETER, SMALL_PAWN_DIAMETER);
        }
        currentColor = Color.WHITE;
        for (int whitePawnsIndex = 0; whitePawnsIndex < squareToDraw.getNumberOfWhitePawns(); whitePawnsIndex++) {
            final double pawnAngle = meanAngle + (whitePawnsIndex + 1) * pawnSpacingAngle;
            double yCoordinate = BOARD_CENTER_Y + Math.sin(squareToDraw.getLeftLimitAngle() + pawnAngle) * distanceFromCenter - SMALL_PAWN_RADIUS;
            double xCoordinate = BOARD_CENTER_X + Math.cos(squareToDraw.getLeftLimitAngle() + pawnAngle) * distanceFromCenter - SMALL_PAWN_RADIUS;
            g.setColor(currentColor);
            g.fillOval((int) xCoordinate,
                    (int) yCoordinate,
                    SMALL_PAWN_DIAMETER, SMALL_PAWN_DIAMETER);
            g.setColor(Color.BLACK);
            g.drawOval((int) xCoordinate,
                    (int) yCoordinate,
                    SMALL_PAWN_DIAMETER, SMALL_PAWN_DIAMETER);
        }
    }

    private void drawConqueredSquare(final Graphics g, final Square squareToDraw) {
        final int row = squareToDraw.getRow();
        double distanceFromCenter = (3.5 - row) * FIRST_CIRCLE_RADIUS;
        if (row == 3) {
            distanceFromCenter = 0;
        }
        final double angleToDrawThePawn = squareToDraw.getLeftLimitAngle() + ((squareToDraw.getRightLimitAngle() - squareToDraw.getLeftLimitAngle()) / 2);
        double yCoordinate = BOARD_CENTER_Y + Math.sin(angleToDrawThePawn) * distanceFromCenter - CONQUERED_DOT_RADIUS;
        double xCoordinate = BOARD_CENTER_X + Math.cos(angleToDrawThePawn) * distanceFromCenter - CONQUERED_DOT_RADIUS;
        g.setColor(squareToDraw.getConqueringPlayer().getColor());
        g.fillOval((int) xCoordinate,
                (int) yCoordinate,
                CONQUERED_DOT_DIAMETER, CONQUERED_DOT_DIAMETER);
        g.setColor(Color.BLACK);
        g.drawOval((int) xCoordinate,
                (int) yCoordinate,
                CONQUERED_DOT_DIAMETER, CONQUERED_DOT_DIAMETER);
    }

    public int getBoardCenterXCoordinate() {
        return BOARD_CENTER_X;
    }

    public int getBoardCenterYCoordinate() {
        return BOARD_CENTER_Y;
    }

    public void setSquaresToDraw(final Square[][] squaresToDraw) {
        this.squaresToDraw = squaresToDraw;
        repaint();
    }
}
