/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author teton
 */
public class RemainingMovesPanel extends JPanel {

    private static final int MARGIN = 5;
    private static final int CIRCLE_DIAMETER = 30;
    
    private int remainingMoves = 1;
    private Color color = Color.BLACK;
    private final Dimension size;
    
    public RemainingMovesPanel() {
        this.size = new Dimension(CIRCLE_DIAMETER + MARGIN, 2 * (CIRCLE_DIAMETER + MARGIN));
    }

    public void setRemainingMoves(final int remainingMoves, final Color color) {
        this.remainingMoves = remainingMoves;
        this.color = color;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < remainingMoves; i++) {
            g.setColor(color);
            g.fillOval(i*(CIRCLE_DIAMETER + MARGIN), MARGIN, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
            g.setColor(Color.BLACK);
            g.drawOval(i*(CIRCLE_DIAMETER + MARGIN), MARGIN, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
        }
    }

    @Override
    public Dimension getSize() {
        return size;
    }
    
}
