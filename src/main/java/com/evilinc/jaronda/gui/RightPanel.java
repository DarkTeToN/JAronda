/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author teton
 */
public class RightPanel extends JPanel {

    private JButton menuButton;

    public RightPanel() {
        initPanel();
        initComponents();
        addComponents();
    }

    private void initPanel() {
        setLayout(new GridBagLayout());
    }

    private void initComponents() {
        initMenuButton();
    }

    private void initMenuButton() {
        final URL cogWheelUrl = getClass().getResource("/com/evilinc/jaronda/icons/wheel.png");
        Image im = new ImageIcon(cogWheelUrl, "Menu").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        menuButton = new JButton(new ImageIcon(im));
    }

    private void addComponents() {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        add(menuButton, constraints);
    }

}
