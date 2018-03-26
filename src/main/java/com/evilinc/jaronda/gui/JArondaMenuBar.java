/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import com.evilinc.jaronda.interfaces.IGameController;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author teton
 */
public class JArondaMenuBar extends JMenuBar {

    private static JArondaMenuBar instance;
    private JEditorPane aboutEditorPane;
    private IGameController gameController;
    private Action cancelLastMoveAction;

    private JArondaMenuBar() {
        initializeMenuBar();
    }

    public static JArondaMenuBar getInstance() {
        if (instance == null) {
            instance = new JArondaMenuBar();
        }
        return instance;
    }

    public void setGameController(IGameController gameController) {
        this.gameController = gameController;
    }

    private void initializeMenuBar() {
        add(getFileMenu());
        add(getMoveMenu());
        add(getAboutMenu());
    }

    private JMenu getFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        fileMenu.add(getNewGameAction());
        fileMenu.addSeparator();
        fileMenu.add(getLoadGameAction());
        fileMenu.add(getSaveGameAction());
        fileMenu.addSeparator();
        fileMenu.add(getExitAction());
        return fileMenu;
    }

    private Action getSaveGameAction() {
        final Action saveGameAction = new AbstractAction("Save game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser aroFileChooser = new JFileChooser();
                aroFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                final int result = aroFileChooser.showSaveDialog(JArondaMenuBar.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    gameController.saveGame(aroFileChooser.getSelectedFile());
                }
            }
        };
        return saveGameAction;
    }

    private Action getLoadGameAction() {
        final Action loadGameAction = new AbstractAction("Load game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser aroFileChooser = new JFileChooser();
                aroFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                final int result = aroFileChooser.showOpenDialog(JArondaMenuBar.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    gameController.loadGame(aroFileChooser.getSelectedFile());
                }
            }
        };
        return loadGameAction;
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

    private JMenu getMoveMenu() {
        final JMenu moveMenu = new JMenu("Move");
        moveMenu.add(getCancelLastMoveAction());
        return moveMenu;
    }

    private JMenu getAboutMenu() {
        final JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(getAboutJArondaAction());
        return aboutMenu;
    }

    public Action getCancelLastMoveAction() {
        if (cancelLastMoveAction == null) {
            cancelLastMoveAction = new AbstractAction("Cancel last move") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gameController.cancelLastMove();
                }
            };
            cancelLastMoveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
            cancelLastMoveAction.setEnabled(false);
        }
        return cancelLastMoveAction;
    }

    private Action getAboutJArondaAction() {
        final Action aboutJArondaAction = new AbstractAction("JAronda") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, getAboutEditorPane());
            }
        };
        return aboutJArondaAction;
    }

    private JEditorPane getAboutEditorPane() {
        if (aboutEditorPane == null) {
            // for copying style
            final JLabel label = new JLabel();
            final Font font = label.getFont();

            // create some css from the label's font
            StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
            style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
            style.append("font-size:" + font.getSize() + "pt;");

            // html content
            aboutEditorPane = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
                    + "JAronda v1.0<br>Developped by DarkTeToN<br>"
                    + "GNU General Public License v3.0<br><br>"
                    + "Github:<br>"
                    + "<a href=\"https://github.com/DarkTeToN/JAronda\">https://github.com/DarkTeToN/JAronda</a><br><br>"
                    + "Rules of the game:<br>"
                    + "<a href=\"http://www.yucata.de/en/Rules/Aronda\">http://www.yucata.de/en/Rules/Aronda</a><br>"
                    + "</body></html>");

            // handle link events
            aboutEditorPane.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) && Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            aboutEditorPane.setEditable(false);
            aboutEditorPane.setBackground(label.getBackground());
        }

        return aboutEditorPane;
    }

    private Action getNewGameAction() {
        final Action newGameAction = new AbstractAction("New game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final NewGameDialog newGameDialog = new NewGameDialog(null);
                newGameDialog.setVisible(true);
                gameController.startNewGame();
            }
        };
        return newGameAction;
    }

}
