/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.serialization;

import com.evilinc.jaronda.enums.ESerializatonType;
import com.evilinc.jaronda.exceptions.InvalidAroMoveException;
import com.evilinc.jaronda.interfaces.IFileParser;
import com.evilinc.jaronda.model.serialization.aro.AroMove;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author teton
 */
public class AroParserTest {

    File validSquaresFile;
    File invalidSquaresFile;
    File completeGameFile;

    public AroParserTest() {
    }

    @Before
    public void setUp() throws URISyntaxException {
        validSquaresFile = new File(getClass().getResource("/com/evilinc/jaronda/controller/validSquares.aro").toURI());
        invalidSquaresFile = new File(getClass().getResource("/com/evilinc/jaronda/controller/invalidSquares.aro").toURI());
        completeGameFile = new File(getClass().getResource("/com/evilinc/jaronda/controller/completeGame.aro").toURI());
    }

    @Test
    public void testValidCharacters() throws URISyntaxException, IOException, InvalidAroMoveException {
        final IFileParser parser = ESerializatonType.ARO.getParser();
        final List<AroMove> validMoves = parser.parse(validSquaresFile);
        assertTrue(validMoves.size() == 25);
    }

    @Test
    public void testInvalidCharacters() {
        final IFileParser parser = ESerializatonType.ARO.getParser();
        List<AroMove> invalidMoves = null;
        boolean invlaidMoveExceptionThrown = false;
        try {
            invalidMoves = parser.parse(invalidSquaresFile);
        } catch (IOException ex) {
            Logger.getLogger(AroParserTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAroMoveException ex) {
            invlaidMoveExceptionThrown = true;
        }
        assertTrue(invlaidMoveExceptionThrown);
        assertNull(invalidMoves);
    }

    @Test
    public void testWritingCompleteGame() throws IOException, InvalidAroMoveException {
        final IFileParser parser = ESerializatonType.ARO.getParser();
        final List<AroMove> gameMoves = parser.parse(completeGameFile);
        final File tempFile = File.createTempFile("writingAroFileTest", "aro");
        parser.serialize(tempFile, gameMoves);
        assertEquals(177, tempFile.length(), 1);
    }

}
