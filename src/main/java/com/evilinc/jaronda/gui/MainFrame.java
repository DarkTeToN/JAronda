/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.gui;

import com.evilinc.jaronda.interfaces.IGameController;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author teton
 */
public class MainFrame extends JFrame {

    private final BoardPanel boardPanel;
    private final JMenuBar menuBar;
    private final LeftPanel leftPanel;
    private IGameController gameController;
    private JEditorPane aboutEditorPane;

    public MainFrame() {
        this.boardPanel = new BoardPanel();
        this.menuBar = new JMenuBar();
        this.leftPanel = new LeftPanel();
        initFrame();
        initializeMenuBar();
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
                JOptionPane.showMessageDialog(MainFrame.this, getAboutEditorPane());
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
                gameController.startNewGame();
            }
        };
        return newGameAction;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public RemainingMovesPanel getRemainingMovesPanel() {
        return leftPanel.getRemainingMovesPanel();
    }
    
    public void setCancelAction(final Action cancelAction) {
        leftPanel.setCancelAction(cancelAction);
    }

    public void setGameController(final IGameController gameController) {
        this.gameController = gameController;
    }
}
