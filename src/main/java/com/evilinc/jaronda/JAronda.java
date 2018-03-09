/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda;

import com.evilinc.jaronda.controller.GameController;
import com.evilinc.jaronda.gui.MainFrame;

/**
 *
 * @author teton
 */
public class JAronda {

    private static MainFrame mainFrame;
    private static GameController gameController;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initializeGui();
        initializeControllers();
        launchJAronda();
    }

    private static void initializeGui() {
        mainFrame = new MainFrame();
    }
    
    private static void initializeControllers() {
        gameController = GameController.getInstance(mainFrame.getBoardPanel());
        gameController.setRemainingMovesPanel(mainFrame.getRemainingMovesPanel());
        mainFrame.setGameController(gameController);
    }
    
    private static void launchJAronda() {
        mainFrame.setVisible(true);
    }

}
