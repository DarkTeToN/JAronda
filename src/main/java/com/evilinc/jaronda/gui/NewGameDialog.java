/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.enums.EPlayerType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 *
 * @author teton
 */
public class NewGameDialog extends JDialog {

    private final JLabel messagelabel = new JLabel("New game:");
    private final JRadioButton hostNewGameRadioButton = new JRadioButton("Host a new game");
    private final JRadioButton connectToNewGameRadioButton = new JRadioButton("Connect to a new game");
    private final JRadioButton playAsBlackRadioButton = new JRadioButton("Play as Black");
    private final JRadioButton playAsWhiteRadioButton = new JRadioButton("Play as White");
    private final JRadioButton analysisModeRadioButton = new JRadioButton("Human VS Human (Analysis mode)");
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton okButton = new JButton("OK");

    public NewGameDialog(final JFrame parent) {
        super(parent, "New game", true);
        initDialog();
        initComponents();
        addComponents();
    }

    private void initDialog() {
        getContentPane().setLayout(new GridBagLayout());
    }

    private void initComponents() {
        buttonGroup.add(playAsBlackRadioButton);
        buttonGroup.add(analysisModeRadioButton);
        buttonGroup.add(playAsWhiteRadioButton);
        buttonGroup.add(hostNewGameRadioButton);
        buttonGroup.add(connectToNewGameRadioButton);
        analysisModeRadioButton.setSelected(true);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserChoice();
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void addComponents() {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.weightx = 1;
        constraints.weighty = 0;
        getContentPane().add(messagelabel, constraints);
        constraints.gridy++;
        getContentPane().add(hostNewGameRadioButton, constraints);
        constraints.gridy++;
        getContentPane().add(connectToNewGameRadioButton, constraints);
        constraints.gridy++;
        getContentPane().add(playAsBlackRadioButton, constraints);
        constraints.gridy++;
        getContentPane().add(playAsWhiteRadioButton, constraints);
        constraints.gridy++;
        getContentPane().add(analysisModeRadioButton, constraints);
        constraints.gridx++;
        constraints.gridy++;
        constraints.gridwidth = 1;
        getContentPane().add(cancelButton, constraints);
        constraints.gridx++;
        getContentPane().add(okButton, constraints);
        pack();
    }

    private void updateUserChoice() {
        if (hostNewGameRadioButton.isSelected()) {
            EPlayer.WHITE.playerType = EPlayerType.INTERNET_CLIENT;
            EPlayer.BLACK.playerType = EPlayerType.HUMAN;
        } else if (connectToNewGameRadioButton.isSelected()) {
            EPlayer.BLACK.playerType = EPlayerType.INTERNET_HOST;
            EPlayer.WHITE.playerType = EPlayerType.HUMAN;
        } else if (playAsWhiteRadioButton.isSelected()) {
            EPlayer.BLACK.playerType = EPlayerType.CPU;
            EPlayer.WHITE.playerType = EPlayerType.HUMAN;
        } else if (playAsWhiteRadioButton.isSelected()) {
            EPlayer.BLACK.playerType = EPlayerType.CPU;
            EPlayer.WHITE.playerType = EPlayerType.HUMAN;
        } else {
            EPlayer.WHITE.playerType = EPlayerType.HUMAN;
            EPlayer.BLACK.playerType = EPlayerType.HUMAN;
        }
    }

}
