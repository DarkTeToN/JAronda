/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.serialization;

import com.evilinc.jaronda.exceptions.InvalidTextMoveException;
import com.evilinc.jaronda.model.serialization.TextMove;
import com.evilinc.jaronda.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class AroParserTest {

    File validSquaresFile;
    File invalidSquaresFile;

    public AroParserTest() {
    }

    @Before
    public void setUp() throws URISyntaxException {
        validSquaresFile = new File(getClass().getResource("/com/evilinc/jaronda/controller/validSquares.aro").toURI());
        invalidSquaresFile = new File(getClass().getResource("/com/evilinc/jaronda/controller/invalidSquares.aro").toURI());
    }

    @Test
    public void testValidCharacters() throws URISyntaxException, IOException, InvalidTextMoveException {
        final AroParser parser = new AroParser(validSquaresFile);
        final List<TextMove> validMoves = parser.parse();
        assertTrue(validMoves.size() == 25);
    }

    @Test
    public void testInvalidCharacters() {
        final AroParser parser = new AroParser(invalidSquaresFile);
        List<TextMove> invalidMoves = null;
        boolean invlaidMoveExceptionThrown = false;
        try {
            invalidMoves = parser.parse();
        } catch (IOException ex) {
            Logger.getLogger(AroParserTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidTextMoveException ex) {
            invlaidMoveExceptionThrown = true;
        }
        assertTrue(invlaidMoveExceptionThrown);
        assertNull(invalidMoves);
    }
}
