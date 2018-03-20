/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.game;

import com.evilinc.jaronda.enums.EPlayer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author teton
 */
public class TurnControllerTest {

    private final TurnController turnController;

    public TurnControllerTest() {
        turnController = new TurnController();
    }

    @Test
    public void testTurns() {
        assertTrue(turnController.getCurrentPlayer() == EPlayer.BLACK);
        assertTrue(turnController.getNextPlayer() == EPlayer.WHITE);
        assertEquals(1, turnController.getRemainingMoves());

        turnController.playMove();
        assertTrue(turnController.getCurrentPlayer() == EPlayer.WHITE);
        assertTrue(turnController.getNextPlayer() == EPlayer.WHITE);
        assertEquals(2, turnController.getRemainingMoves());

        turnController.playMove();
        assertTrue(turnController.getCurrentPlayer() == EPlayer.WHITE);
        assertTrue(turnController.getNextPlayer() == EPlayer.BLACK);
        assertEquals(1, turnController.getRemainingMoves());

        turnController.playMove();
        assertTrue(turnController.getCurrentPlayer() == EPlayer.BLACK);
        assertTrue(turnController.getNextPlayer() == EPlayer.BLACK);
        assertEquals(2, turnController.getRemainingMoves());

        turnController.cancelMove();
        assertTrue(turnController.getCurrentPlayer() == EPlayer.WHITE);
        assertTrue(turnController.getNextPlayer() == EPlayer.BLACK);
        assertEquals(1, turnController.getRemainingMoves());

        turnController.cancelMove();
        turnController.cancelMove();
        turnController.cancelMove();
        turnController.cancelMove();
        turnController.cancelMove();
        assertTrue(turnController.getCurrentPlayer() == EPlayer.BLACK);
        assertTrue(turnController.getNextPlayer() == EPlayer.WHITE);
        assertEquals(1, turnController.getRemainingMoves());
    }
}
