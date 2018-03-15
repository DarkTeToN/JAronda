/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.EPlayer;
import com.evilinc.jaronda.exceptions.IllegalMoveException;
import com.evilinc.jaronda.gui.BoardPanel;
import com.evilinc.jaronda.gui.RemainingMovesPanel;
import com.evilinc.jaronda.interfaces.IGameController;
import com.evilinc.jaronda.model.Board;
import com.evilinc.jaronda.model.Move;
import com.evilinc.jaronda.model.SimpleSquare;
import com.evilinc.jaronda.model.Square;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author teton
 */
public class GameController implements IGameController {

    private static GameController instance;
    private final SquareController squareController;
    private final Stack<Move> playedMoves;
    private BoardController boardController;
    private BoardPanel boardPanel;
    private EPlayer currentPlayer;
    private int numberOfMovesToPlay = 1;
    private RemainingMovesPanel remainingMovesPanel;
    private Action cancelAction;

    private GameController() {
        currentPlayer = EPlayer.BLACK;
        playedMoves = new Stack<>();
        squareController = new SquareController();
        getCancelAction().setEnabled(false);
    }

    public void setRemainingMovesPanel(RemainingMovesPanel remainingMovesPanel) {
        this.remainingMovesPanel = remainingMovesPanel;
    }

    private void updateDisplay() {
        if (boardController != null) {
            boardController.updateBoardPanel(squareController.getSquares());
        }
        if (remainingMovesPanel != null) {
            remainingMovesPanel.setRemainingMoves(numberOfMovesToPlay, currentPlayer.getColor());
        }
        getCancelAction().setEnabled(!playedMoves.empty());
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
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
                    int[] playedSquare = boardController.getMoveCoordinates(event.getPoint());
                    if (isPlayedSquareValid(playedSquare)) {
                        try {
                            playMoveAt(playedSquare[0], playedSquare[1]);
                        } catch (IllegalMoveException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    public Action getCancelAction() {
        if (cancelAction == null) {
            cancelAction = new AbstractAction("Cancel last move") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelLastMove();
                }
            };
        }
        return cancelAction;
    }

    private boolean isPlayedSquareValid(final int[] playedSquare) {
        return playedSquare[0] > -1 && playedSquare[1] > -1;
    }

    @Override
    public void startNewGame() {
        squareController.reset();
        boardController.updateBoardPanel(squareController.getSquares());
        numberOfMovesToPlay = 1;
        currentPlayer = EPlayer.BLACK;
        remainingMovesPanel.setRemainingMoves(numberOfMovesToPlay, currentPlayer.getColor());
        playedMoves.clear();
    }

    public void playMoveAt(final int row, final int squareNumber) throws IllegalMoveException {
        final Square playedSquare = squareController.getSquareAt(row, squareNumber);
        checkMoveValidity(playedSquare, currentPlayer);
        final Move playedMove = squareController.playMoveAt(row, squareNumber, currentPlayer);
        playedMoves.push(playedMove);
        final EPlayer winner = getWinner();
        if (--numberOfMovesToPlay == 0) {
            resetNumberOfMovesToPlay();
            changePlayersTurn();
        }
        updateDisplay();
        if (winner != null) {
            JOptionPane.showMessageDialog(null, winner.name() + " wins !", "End of the game", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cancelLastMove() {
        final Move lastPlayedMove = playedMoves.pop();
        squareController.cancelMove(lastPlayedMove);
        if (++numberOfMovesToPlay == 3) {
            numberOfMovesToPlay = 1;
            changePlayersTurn();
        }
        updateDisplay();

    }

    private EPlayer getNextPlayerTurn() {
        EPlayer nextPlayer = currentPlayer;
        if (numberOfMovesToPlay - 1 == 0) {
            if (nextPlayer == EPlayer.BLACK) {
                nextPlayer = EPlayer.WHITE;
            } else {
                nextPlayer = EPlayer.BLACK;
            }
        }
        return nextPlayer;
    }

    private boolean canPlayerPlay(final EPlayer player) {
        for (final Square[] squareRow : squareController.getSquares()) {
            for (final Square currentSquare : squareRow) {
                try {
                    checkMoveValidity(currentSquare, player);
                    return true;
                } catch (final IllegalMoveException e) {

                }
            }
        }
        return false;
    }

    public void checkMoveValidity(final Square square, final EPlayer playerToCheck) throws IllegalMoveException {
        if (isConquered(square)) {
            throw new IllegalMoveException("This square is already conquered. It's not possible to play here.");
        }

        if (!square.isOnTheEdge()) {
            boolean connected = false;
            final List<Square> alreadyVisitedSquare = new ArrayList<>();
            alreadyVisitedSquare.add(square);
            for (final Square currentAdjacentSquare : square.getAdjacentSquares()) {
                if (isConnectedToTheEdge(currentAdjacentSquare, alreadyVisitedSquare, playerToCheck)) {
                    connected = true;
                    break;
                }
            }
            if (!connected) {
                throw new IllegalMoveException("This square is not connected to the edge.");
            }
        }
    }

    public boolean isConnectedToTheEdge(final Square square, final List<Square> alreadyVisitedSquares, EPlayer player) {
        alreadyVisitedSquares.add(square);
        if (square.isOnTheEdge()) {
            if (square.containsPawnFromColor(player)) {
                return true;
            }
        } else {
            if (square.containsPawnFromColor(player)) {
                for (final Square neighbourSquare : square.getAdjacentSquares()) {
                    if (!alreadyVisitedSquares.contains(neighbourSquare)) {
                        if (isConnectedToTheEdge(neighbourSquare, alreadyVisitedSquares, player)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isConquered(final Square square) {
        return square.getConqueringPlayer() != null;
    }

    private void resetNumberOfMovesToPlay() {
        numberOfMovesToPlay = 2;
    }

    private void changePlayersTurn() {
        if (currentPlayer == EPlayer.BLACK) {
            currentPlayer = EPlayer.WHITE;
        } else {
            currentPlayer = EPlayer.BLACK;
        }
    }

    private EPlayer getWinner() {
        final EPlayer nextPlayer = getNextPlayerTurn();
        if (squareController.getNumberOfBlackConqueredSquares() >= 13) {
            return EPlayer.BLACK;
        } else if (squareController.getNumberOfWhiteConqueredSquares() >= 13) {
            return EPlayer.WHITE;
        } else if (!canPlayerPlay(nextPlayer)) {
            if (nextPlayer == EPlayer.BLACK) {
                return EPlayer.WHITE;
            } else {
                return EPlayer.BLACK;
            }
        }
        return null;
    }

    public Board getCurrentBoard() {
        final Board currentBoard = new Board();
        currentBoard.squares = getSimpleSquareList();
        currentBoard.currentPlayer = currentPlayer.name();
        currentBoard.winner = String.valueOf(getWinner());
        currentBoard.remainingMoves = numberOfMovesToPlay;
        currentBoard.blackScore = squareController.getNumberOfBlackConqueredSquares();
        currentBoard.whiteScore = squareController.getNumberOfWhiteConqueredSquares();
        return currentBoard;
    }

    public Board playMove(final Board boardBeforeMove) throws IllegalMoveException {
        updateSquareListFromSimpleSquareList(boardBeforeMove.squares);
        currentPlayer = EPlayer.fromString(boardBeforeMove.currentPlayer);
        numberOfMovesToPlay = boardBeforeMove.remainingMoves;
        playMoveAt(boardBeforeMove.moveRow, boardBeforeMove.moveSquareNumber);
        final Board boardAfterMove = getCurrentBoard();
        boardAfterMove.validMove = true;
        return boardAfterMove;
    }

    private void updateSquareListFromSimpleSquareList(final List<SimpleSquare> simpleSquares) {
        simpleSquares.forEach((simpleSquare) -> {
            final Square square = squareController.getSquareAt(simpleSquare.row, simpleSquare.squareNumber);
            square.conqueringPlayer = EPlayer.fromString(simpleSquare.conqueringColor);
            square.numberOfBlackPawns = simpleSquare.numberOfBlackPawns;
            square.numberOfWhitePawns = simpleSquare.numberOfWhitePawns;
        });
    }

    private List<SimpleSquare> getSimpleSquareList() {
        final List<SimpleSquare> squares = new ArrayList<>();
        squareController.getSquareList().forEach((currentSquare) -> {
            squares.add(new SimpleSquare(currentSquare));
        });
        return squares;
    }
}
