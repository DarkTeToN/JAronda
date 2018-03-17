/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda;

import com.evilinc.jaronda.consts.SystemConst;
import com.evilinc.jaronda.controller.GameController;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.gui.MainFrame;
import com.evilinc.jaronda.model.Board;
import com.google.gson.Gson;
import java.io.FileNotFoundException;

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
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length > 0) {
            launchJArondaInConsoleMode(args);
        } else {
            launchJArondaInGuiMode();
        }
    }

    private static void launchJArondaInConsoleMode(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.exit(SystemConst.BAD_NUMBER_OF_ARGUMENTS_EXIT_STATUS);
        }
        initializeGameControllers();
        final Gson gson = new Gson();
        final String argValue = args[0];
        if ("print".equals(argValue)) {
            final Board currentBoard = gameController.getCurrentBoard();
            System.out.println(gson.toJson(currentBoard));
        } else {
            Board board = gson.fromJson(argValue, Board.class);
            try {
                board = gameController.playMove(board);
            } catch (IllegalMoveException ex) {
                board.validMove = false;
            }
            System.out.println(gson.toJson(board));
        }
    }

    private static void launchJArondaInGuiMode() {
        initializeGui();
        initializeControllers();
        launchJAronda();
    }

    private static void initializeGui() {
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
        mainFrame.setCancelAction(gameController.getCancelAction());
        mainFrame.setGameController(gameController);
    }

    private static void launchJAronda() {
        mainFrame.setVisible(true);
    }

}
