/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.enums;

import java.awt.Color;

/**
 *
 * @author teton
 */
public enum EPlayer {
    BLACK(Color.BLACK, EPlayerType.HUMAN),
    WHITE(Color.WHITE, EPlayerType.HUMAN);

    private final Color playerColor;
    public EPlayerType playerType;

    private EPlayer(final Color playerColor, final EPlayerType playerType) {
        this.playerColor = playerColor;
        this.playerType = playerType;
    }

    public Color getColor() {
        return playerColor;
    }

    public EPlayerType getPlayerType() {
        return playerType;
    }
    
    public static EPlayer fromString(final String player) {
        for (final EPlayer currentPlayer : values()) {
            if (currentPlayer.name().equals(player)) {
                return currentPlayer;
            }
        }
        return null;
    }

    public static EPlayer getOpponent(final EPlayer player) {
        switch (player) {
            case BLACK:
                return WHITE;
            case WHITE:
            default:
                return BLACK;
        }
    }
}
