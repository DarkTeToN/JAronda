/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda;

import com.evilinc.jaronda.consts.HttpConst;
import com.evilinc.jaronda.consts.SystemConst;
import com.evilinc.jaronda.controller.game.GameController;
import com.evilinc.jaronda.controller.http.GetBoardHttpHandler;
import com.evilinc.jaronda.controller.http.PlayMoveHttpHandler;
import com.evilinc.jaronda.controller.http.StartNewGameHandler;
import com.evilinc.jaronda.enums.EHttpHandler;
import com.evilinc.jaronda.gui.JArondaMenuBar;
import com.evilinc.jaronda.gui.MainFrame;
import com.jtattoo.plaf.mint.MintLookAndFeel;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 *
 * @author teton
 */
public class JAronda {

    private static MainFrame mainFrame;
    private static GameController gameController;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws IOException {
        if (isAjpMode()) {
            launchJArondaInConsoleMode();
        } else {
            launchJArondaInGuiMode();
        }
    }

    private static boolean isAjpMode() {
        return Boolean.parseBoolean(System.getProperty(SystemConst.AJP_MODE));
    }

    private static void launchJArondaInConsoleMode() throws IOException {
        initializeGameControllers();
        launchArondaHttpServer();
    }

    private static void launchJArondaInGuiMode() {
        initializeGui();
        initializeControllers();
        launchJAronda();
    }

    private static void initializeGui() {
        try {
            Properties props = new Properties();
            props.put("logoString", "JAronda");
            MintLookAndFeel.setCurrentTheme(props);
            // select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
        } catch (Exception ex) {
            Logger.getLogger(JAronda.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainFrame = new MainFrame();
    }

    private static void initializeControllers() {
        initializeGameControllers();
        initializeGraphicalControllers();

    }

    private static void initializeGameControllers() {
        gameController = new GameController();
    }

    private static void initializeGraphicalControllers() {
        gameController.setBoardPanel(mainFrame.getBoardPanel());
        gameController.setRemainingMovesPanel(mainFrame.getRemainingMovesPanel());
        JArondaMenuBar.getInstance().setGameController(gameController);
    }

    private static void launchJAronda() {
        mainFrame.setVisible(true);
    }

    private static void launchArondaHttpServer() throws IOException {
        System.out.println("Starting JAronda in Aronda JSON Protocol communication mode...");
        System.out.println(String.format("Listening on port: %d", HttpConst.WEB_SERVER_PORT));
        HttpServer server = HttpServer.create(new InetSocketAddress(HttpConst.WEB_SERVER_PORT), 0);
        server.createContext(EHttpHandler.START_NEW_GAME.getContext(), new StartNewGameHandler(gameController));
        server.createContext(EHttpHandler.GET_BOARD.getContext(), new GetBoardHttpHandler(gameController));
        server.createContext(EHttpHandler.PLAY_MOVE.getContext(), new PlayMoveHttpHandler(gameController));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

}
