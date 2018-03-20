/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import com.evilinc.jaronda.interfaces.IGameController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

/**
 *
 * @author teton
 */
public class MainFrame extends JFrame {

    private final BoardPanel boardPanel;
    private final JMenuBar menuBar;
    private final RemainingMovesPanel leftPanel;
    private final RightPanel rightPanel;
    private IGameController gameController;

    public MainFrame() {
        this.boardPanel = new BoardPanel();
        this.menuBar = JArondaMenuBar.getInstance();
        this.leftPanel = new RemainingMovesPanel();
        this.rightPanel = new RightPanel();
        initFrame();
        addComponents();
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 300));
        setTitle("JAronda v1.0");
    }

    private void addComponents() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuBar, BorderLayout.NORTH);
        getContentPane().add(boardPanel, BorderLayout.CENTER);
        getContentPane().add(leftPanel, BorderLayout.WEST);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public RemainingMovesPanel getRemainingMovesPanel() {
        return leftPanel;
    }
}
