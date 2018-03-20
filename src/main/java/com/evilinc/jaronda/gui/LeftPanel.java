/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import java.awt.GridLayout;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author teton
 */
public class LeftPanel extends JPanel {

    private final RemainingMovesPanel remainingMovesPanel;

    public LeftPanel() {
        remainingMovesPanel = new RemainingMovesPanel();
        addComponents();
    }

    private void addComponents() {
        setLayout(new GridLayout(3, 1));
        add(new JLabel("Remaining moves:"));
        add(remainingMovesPanel);
    }

    public RemainingMovesPanel getRemainingMovesPanel() {
        return remainingMovesPanel;
    }
    
}
