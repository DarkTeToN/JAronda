/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.game;

import com.evilinc.jaronda.ai.ComputerPlayer;
import com.evilinc.jaronda.enums.EPlayer;
import com.evilinc.jaronda.enums.EPlayerType;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.gui.BoardPanel;
import com.evilinc.jaronda.gui.JArondaMenuBar;
import com.evilinc.jaronda.gui.RemainingMovesPanel;
import com.evilinc.jaronda.interfaces.IGameController;
import com.evilinc.jaronda.model.game.Board;
import com.evilinc.jaronda.model.game.Move;
import com.evilinc.jaronda.model.serialization.JsonSquare;
import com.evilinc.jaronda.model.game.Square;
import com.evilinc.jaronda.model.serialization.TextMove;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author teton
 */
public class GameController implements IGameController {

    private final SquareController squareController;
    private final Stack<Move> playedMoves;
    private final TurnController turnController;
    private ComputerPlayer cpuPlayer;
    private BoardController boardController;
    private BoardPanel boardPanel;
    private RemainingMovesPanel remainingMovesPanel;
    private final JArondaMenuBar menuBar;

    public GameController() {
        playedMoves = new Stack<>();
        squareController = new SquareController();
        turnController = new TurnController();
        cpuPlayer = new ComputerPlayer(EPlayer.WHITE);
        menuBar = JArondaMenuBar.getInstance();
        menuBar.getCancelLastMoveAction().setEnabled(false);
    }

    public void setRemainingMovesPanel(RemainingMovesPanel remainingMovesPanel) {
        this.remainingMovesPanel = remainingMovesPanel;
    }

    private void updateDisplay() {
        if (boardController != null) {
            boardController.updateBoardPanel(squareController.getSquares());
        }
        if (remainingMovesPanel != null) {
            remainingMovesPanel.setRemainingMoves(turnController.getRemainingMoves(), turnController.getCurrentPlayer().getColor());
        }
        menuBar.getCancelLastMoveAction().setEnabled(!playedMoves.empty());
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        boardController = new BoardController(boardPanel);
        initListeners();
        updateDisplay();
    }

    private void initListeners() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    if (turnController.getCurrentPlayer().playerType == EPlayerType.CPU) {
                        return;
                    }
                    int[] playedSquare = boardController.getMoveCoordinates(event.getPoint());
                    if (isPlayedSquareValid(playedSquare)) {
                        try {
                            playMoveAt(playedSquare[0], playedSquare[1]);
                            if (turnController.getCurrentPlayer().playerType == EPlayerType.CPU) {
                                playCpuMove();
                            }
                        } catch (IllegalMoveException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void cpuPlays(final int[] cpuMoveCoordinates) {
        try {
            playMoveAt(cpuMoveCoordinates[0], cpuMoveCoordinates[1]);
            if (RuleController.getWinner(squareController, turnController) == null && turnController.getCurrentPlayer().playerType == EPlayerType.CPU) {
                playCpuMove();
            } else {
                if (boardPanel != null) {
                    boardPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                menuBar.getCancelLastMoveAction().setEnabled(true);
            }
        } catch (IllegalMoveException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void playCpuMove() throws IllegalMoveException {
        menuBar.getCancelLastMoveAction().setEnabled(false);
        if (!RuleController.isGameFinished(squareController, turnController.getCurrentPlayer())) {
            final CpuMoveWorker worker = new CpuMoveWorker();
            worker.execute();
            if (boardPanel != null) {
                boardPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            }
        }
    }

    private class CpuMoveWorker extends SwingWorker<int[], Void> {

        @Override
        protected int[] doInBackground() throws Exception {
            return cpuPlayer.getMove(squareController.getSquares());
        }

        @Override
        protected void done() {
            try {
                final int[] cpuMoveCoordinates = get();
                cpuPlays(cpuMoveCoordinates);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean isPlayedSquareValid(final int[] playedSquare) {
        return playedSquare[0] > -1 && playedSquare[1] > -1;
    }

    @Override
    public void startNewGame() {
        squareController.reset();
        boardController.updateBoardPanel(squareController.getSquares());
        turnController.reset();
        playedMoves.clear();
        if (turnController.getCurrentPlayer().playerType == EPlayerType.CPU) {
            cpuPlayer = new ComputerPlayer(turnController.getCurrentPlayer());
            try {
                playCpuMove();
            } catch (IllegalMoveException ex) {
                Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        updateDisplay();
    }

    public void playMoveAt(final int row, final int squareNumber) throws IllegalMoveException {
//        System.out.println("Playing: " + row + "-" + squareNumber);
        final Square playedSquare = squareController.getSquareAt(row, squareNumber);
        RuleController.checkMoveValidity(playedSquare, turnController.getCurrentPlayer());
        final Move playedMove = squareController.playMoveAt(row, squareNumber, turnController.getCurrentPlayer());
        playedMoves.push(playedMove);
        turnController.playMove();
        updateDisplay();
        final EPlayer winner = RuleController.getWinner(squareController, turnController);

        if (winner != null) {
            JOptionPane.showMessageDialog(null, winner.name() + " wins !", "End of the game", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void cancelLastMove() {
        final Move lastPlayedMove = playedMoves.pop();
        squareController.cancelMove(lastPlayedMove);
        turnController.cancelMove();
        updateDisplay();

    }

    public Board getCurrentBoard() {
        final Board currentBoard = new Board();
        currentBoard.squares = getSimpleSquareList();
        currentBoard.currentPlayer = turnController.getCurrentPlayer().name();
        currentBoard.winner = String.valueOf(RuleController.getWinner(squareController, turnController));
        currentBoard.remainingMoves = turnController.getRemainingMoves();
        currentBoard.blackScore = squareController.getNumberOfBlackConqueredSquares();
        currentBoard.whiteScore = squareController.getNumberOfWhiteConqueredSquares();
        return currentBoard;
    }

    public Board playMove(final Board boardBeforeMove) throws IllegalMoveException {
        // TODO: Change the JSON interface to put the number of move played from the beginning instead
        updateSquareListFromSimpleSquareList(boardBeforeMove.squares);
//        currentPlayer = EPlayer.fromString(boardBeforeMove.currentPlayer);
//        numberOfMovesToPlay = boardBeforeMove.remainingMoves;
        playMoveAt(boardBeforeMove.moveRow, boardBeforeMove.moveSquareNumber);
        final Board boardAfterMove = getCurrentBoard();
        boardAfterMove.validMove = true;
        return boardAfterMove;
    }

    private void updateSquareListFromSimpleSquareList(final List<JsonSquare> simpleSquares) {
        simpleSquares.forEach((simpleSquare) -> {
            final Square square = squareController.getSquareAt(simpleSquare.row, simpleSquare.squareNumber);
            square.conqueringPlayer = EPlayer.fromString(simpleSquare.conqueringColor);
            square.numberOfBlackPawns = simpleSquare.numberOfBlackPawns;
            square.numberOfWhitePawns = simpleSquare.numberOfWhitePawns;
        });
    }

    private List<JsonSquare> getSimpleSquareList() {
        final List<JsonSquare> squares = new ArrayList<>();
        squareController.getSquareList().forEach((currentSquare) -> {
            squares.add(new JsonSquare(currentSquare));
        });
        return squares;
    }

    public void loadGame(final List<TextMove> movesToPlay) throws IllegalMoveException {
        for (final TextMove currentMove : movesToPlay) {
            playMoveAt(currentMove.row, currentMove.column);
        }
    }
}
