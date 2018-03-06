/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import com.evilinc.jaronda.controller.GuiToModelController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author teton
 */
public class MainFrame extends JFrame {
    
    private final BoardPanel boardPanel;
    private final GuiToModelController controller;

    public MainFrame() {
        this.boardPanel = new BoardPanel();
        this.controller = new GuiToModelController(boardPanel);
        initFrame();
        addComponents();
    }
    
    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800,650));
        setTitle("JAronda");
    }

    private void addComponents() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(boardPanel, BorderLayout.CENTER);
    }

}
