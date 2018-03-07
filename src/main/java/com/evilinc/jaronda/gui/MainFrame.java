/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import com.evilinc.jaronda.controller.GameController;
import com.evilinc.jaronda.controller.BoardController;
import com.evilinc.jaronda.interfaces.IGameController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 *
 * @author teton
 */
public class MainFrame extends JFrame {

    private final BoardPanel boardPanel;
    private final JMenuBar menuBar;
    private IGameController gameController;

    public MainFrame() {
        this.boardPanel = new BoardPanel();
        this.menuBar = new JMenuBar();
        initFrame();
        initializeMenuBar();
        addComponents();
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 650));
        setTitle("JAronda v1.0");
    }

    private void addComponents() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuBar, BorderLayout.NORTH);
        getContentPane().add(boardPanel, BorderLayout.CENTER);
    }

    private void initializeMenuBar() {
        menuBar.add(getFileMenu());
        menuBar.add(getAboutMenu());
    }

    private JMenu getFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        fileMenu.add(getNewGameAction());
        fileMenu.addSeparator();
        fileMenu.add(getExitAction());
        return fileMenu;
    }

    private Action getExitAction() {
        final Action exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        return exitAction;
    }

    private JMenu getAboutMenu() {
        final JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(getAboutJArondaAction());
        return aboutMenu;
    }

    private Action getAboutJArondaAction() {
        final Action aboutJArondaAction = new AbstractAction("JAronda") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "JAronda v1.0\nDevelopped by DarkTeToN.\nGNU General Public License v3.0\nhttps://github.com/DarkTeToN/JAronda", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        return aboutJArondaAction;
    }

    private Action getNewGameAction() {
        final Action newGameAction = new AbstractAction("New game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.startNewGame();
            }
        };
        return newGameAction;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
    
    public void setGameController(final IGameController gameController) {
        this.gameController = gameController;
    }
}
