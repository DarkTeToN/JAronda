/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda;

import java.awt.Color;

/**
 *
 * @author teton
 */
public enum EPlayer {
    BLACK(Color.BLACK),
    WHITE(Color.WHITE);

    private final Color playerColor;

    private EPlayer(final Color playerColor) {
        this.playerColor = playerColor;
    }

    public Color getColor() {
        return playerColor;
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
