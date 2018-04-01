/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.game;

import com.evilinc.jaronda.consts.EventConst;
import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.enums.EPlayerType;
import com.evilinc.jaronda.enums.ESerializatonType;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.exceptions.InvalidAroMoveException;
import com.evilinc.jaronda.gui.BoardPanel;
import com.evilinc.jaronda.gui.JArondaMenuBar;
import com.evilinc.jaronda.gui.RemainingMovesPanel;
import com.evilinc.jaronda.interfaces.IFileParser;
import com.evilinc.jaronda.interfaces.IGameController;
import com.evilinc.jaronda.model.factory.PlayerFactory;
import com.evilinc.jaronda.model.game.Move;
import com.evilinc.jaronda.model.game.Square;
import com.evilinc.jaronda.model.player.APlayer;
import com.evilinc.jaronda.model.player.InternetPlayer;
import com.evilinc.jaronda.model.serialization.aro.AroMove;
import com.evilinc.jaronda.model.serialization.json.JsonBoard;
import com.evilinc.jaronda.model.serialization.json.JsonMove;
import com.evilinc.jaronda.model.serialization.json.JsonSquare;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author teton
 */
public class GameController implements IGameController {

    private final SquareController squareController;
    private final Stack<Move> playedMoves;
    private final TurnController turnController;
    private final EventController eventController;
    private APlayer whitePlayer;
    private APlayer blackPlayer;
    private EPlayer winner;
    private BoardController boardController;
    private BoardPanel boardPanel;
    private RemainingMovesPanel remainingMovesPanel;
    private final JArondaMenuBar menuBar;

    public GameController() {
        playedMoves = new Stack<>();
        squareController = new SquareController();
        turnController = new TurnController();
        eventController = EventController.getInstance();
        menuBar = JArondaMenuBar.getInstance();
        menuBar.getCancelLastMoveAction().setEnabled(false);
        initializePlayers();
        initializeEventListeners();
    }

    private void initializeEventListeners() {
        eventController.addPropertyChangeListener(EventConst.MOVE_PLAYED, (PropertyChangeEvent evt) -> {
            final JsonMove playedMove = (JsonMove) evt.getNewValue();
            final APlayer currentPlayer = (APlayer) evt.getOldValue();
            try {
                playMoveAt(currentPlayer, playedMove.row, playedMove.squareNumber);
            } catch (IllegalMoveException ex) {
                Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void setRemainingMovesPanel(RemainingMovesPanel remainingMovesPanel) {
        this.remainingMovesPanel = remainingMovesPanel;
    }

    private void updateDisplay() {
        updateCursor();
        if (boardController != null) {
            boardController.updateBoardPanel(squareController.getSquares(), squareController.getLastPlayedSquare());
        }
        if (remainingMovesPanel != null) {
            remainingMovesPanel.setRemainingMoves(turnController.getRemainingMoves(), turnController.getCurrentPlayer().getColor());
        }
        menuBar.getCancelLastMoveAction().setEnabled(!playedMoves.empty());
        winner = RuleController.getWinner(squareController, turnController);

        if (winner != null) {
            JOptionPane.showMessageDialog(null, winner.name() + " wins !", "End of the game", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        boardController = new BoardController(boardPanel);
        initBoardPanelListener();
        updateDisplay();
    }

    private void initBoardPanelListener() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event) && turnController.getCurrentPlayer().playerType == EPlayerType.HUMAN) {
                    playMoveAt(event.getPoint());
                }
            }
        });
    }

    @Override
    public void saveGame(final File outputFile) {
        final List<Move> movesList = new ArrayList<>(playedMoves);
        IFileParser parser = ESerializatonType.ARO.getParser();
        List<AroMove> aroMoves = new ArrayList();
        for (final Move currentMove : movesList) {
            aroMoves.add(new AroMove(currentMove));
        }
        try {
            parser.serialize(outputFile, aroMoves);
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void loadGame(final File inputFile) {
        final IFileParser parser = ESerializatonType.getParserFromFile(inputFile);
        try {
            final List<AroMove> movesToLoad = parser.parse(inputFile);
            for (final AroMove currentMove : movesToLoad) {
                final Square playedSquare = squareController.getSquareAt(currentMove.row, currentMove.squareNumber);
                RuleController.checkMoveValidity(playedSquare, turnController.getCurrentPlayer());
                final Move playedMove = squareController.playMoveAt(currentMove.row, currentMove.squareNumber, turnController.getCurrentPlayer());
                playedMoves.push(playedMove);
                turnController.playMove();
                final EPlayer winner = RuleController.getWinner(squareController, turnController);

                if (winner != null) {
                    JOptionPane.showMessageDialog(null, winner.name() + " wins !", "End of the game", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            updateDisplay();
        } catch (IOException | InvalidAroMoveException | IllegalMoveException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isPlayedSquareValid(final int[] playedSquare) {
        return playedSquare[0] > -1 && playedSquare[1] > -1;
    }

    @Override
    public void startNewGame() {
        squareController.reset();
        turnController.reset();
        playedMoves.clear();
        initializePlayers();
        updateDisplay();
        updatePlayerConnections();
        notifyPlayerTurn(null);
    }

    private void updatePlayerConnections() {
        if (EPlayer.WHITE.playerType == EPlayerType.INTERNET_CLIENT) {
            eventController.addPropertyChangeListener(EventConst.CLIENT_CONNECTION_OK, (PropertyChangeEvent evt) -> {
                boardPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            });
            waitForPlayerConnection();
        } else if (EPlayer.BLACK.playerType == EPlayerType.INTERNET_HOST) {
            new Thread(() -> {
                ((InternetPlayer) blackPlayer).startConnexion();
            }).start();
        }
    }

    private void waitForPlayerConnection() {
        boardPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        new Thread(() -> {
            ((InternetPlayer) whitePlayer).startConnexion();
        }).start();
    }

    private void initializePlayers() {
        blackPlayer = PlayerFactory.getPlayer(EPlayer.BLACK);
        whitePlayer = PlayerFactory.getPlayer(EPlayer.WHITE);
    }

    public void playMoveAt(final APlayer currentPlayer, final int row, final int squareNumber) throws IllegalMoveException {
        if (turnController.getCurrentPlayer() != currentPlayer.color) {
            throw new IllegalMoveException("It's not your turn to play.");
        }
        playMoveAt(row, squareNumber);
    }

    public void playMoveAt(final Point clickedPoint) {
        int[] playedSquare = boardController.getMoveCoordinates(clickedPoint);
        if (isPlayedSquareValid(playedSquare)) {
            try {
                playMoveAt(playedSquare[0], playedSquare[1]);
            } catch (IllegalMoveException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void playMoveAt(final int row, final int squareNumber) throws IllegalMoveException {
        final Square playedSquare = squareController.getSquareAt(row, squareNumber);
        boolean notification = !(turnController.getCurrentPlayer().playerType == EPlayerType.INTERNET_CLIENT
                || turnController.getCurrentPlayer().playerType == EPlayerType.INTERNET_HOST);
        System.out.println("notification: " + notification);
        RuleController.checkMoveValidity(playedSquare, turnController.getCurrentPlayer());
        final Move playedMove = squareController.playMoveAt(row, squareNumber, turnController.getCurrentPlayer());
        playedMoves.push(playedMove);
        turnController.playMove();
        if (notification) {
            notifyPlayerTurn(playedMove);
        }
        updateDisplay();
    }

    private void notifyPlayerTurn(final Move playedMove) {
        if (winner == null) {
            if (turnController.getCurrentPlayer() == EPlayer.BLACK) {
                blackPlayer.playMove(squareController.getSquares(), playedMove);
                whitePlayer.playMove(squareController.getSquares(), playedMove);
            } else {
                blackPlayer.playMove(squareController.getSquares(), playedMove);
                whitePlayer.playMove(squareController.getSquares(), playedMove);
            }
        }
    }

    private void updateCursor() {
        if (winner != null) {
            boardPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else if (boardPanel != null) {
            boardPanel.setCursor(new Cursor(turnController.getCurrentPlayer().playerType == EPlayerType.CPU ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void cancelLastMove() {
        final Move lastPlayedMove = playedMoves.pop();
        squareController.cancelMove(lastPlayedMove);
        turnController.cancelMove();
        updateDisplay();
    }

    public JsonBoard getCurrentBoard() {
        final JsonBoard currentBoard = new JsonBoard();
        currentBoard.squares = getSimpleSquareList();
        currentBoard.currentPlayer = turnController.getCurrentPlayer().name();
        currentBoard.winner = String.valueOf(RuleController.getWinner(squareController, turnController));
        currentBoard.remainingMoves = turnController.getRemainingMoves();
        currentBoard.blackScore = squareController.getNumberOfBlackConqueredSquares();
        currentBoard.whiteScore = squareController.getNumberOfWhiteConqueredSquares();
        return currentBoard;
    }

    private List<JsonSquare> getSimpleSquareList() {
        final List<JsonSquare> squares = new ArrayList<>();
        squareController.getSquareList().forEach((currentSquare) -> {
            squares.add(new JsonSquare(currentSquare));
        });
        return squares;
    }

    public void loadGame(final List<AroMove> movesToPlay) throws IllegalMoveException {
        for (final AroMove currentMove : movesToPlay) {
            playMoveAt(currentMove.row, currentMove.squareNumber);
        }
    }
}
