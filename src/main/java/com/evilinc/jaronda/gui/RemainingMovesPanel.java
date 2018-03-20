/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author teton
 */
public class RemainingMovesPanel extends JPanel {

    private static final int MARGIN = 15;
    private static final int CIRCLE_DIAMETER = 30;
    
    private int remainingMoves = 1;
    private Color color = Color.BLACK;
    private final Dimension size;
    private final JLabel remainingMovesLabel;
    
    public RemainingMovesPanel() {
        remainingMovesLabel = new JLabel("Remaining moves:");
        remainingMovesLabel.setFont(remainingMovesLabel.getFont().deriveFont(Font.BOLD));
        add(remainingMovesLabel);
        this.size = new Dimension(2*(CIRCLE_DIAMETER + MARGIN), 2 * (CIRCLE_DIAMETER + MARGIN));
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
            g.fillOval(MARGIN + i*(CIRCLE_DIAMETER + MARGIN), CIRCLE_DIAMETER + MARGIN, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
            g.setColor(Color.BLACK);
            g.drawOval(MARGIN + i*(CIRCLE_DIAMETER + MARGIN), CIRCLE_DIAMETER + MARGIN, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
        }
    }
    
}
