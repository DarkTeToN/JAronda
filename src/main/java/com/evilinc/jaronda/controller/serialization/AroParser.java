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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author teton
 */
public class AroParser {

    private final File fileToParse;
    
    public AroParser(final File fileToParse) {
        this.fileToParse =fileToParse;
    }
    
    
    public List<TextMove> parse() throws IOException, InvalidTextMoveException {
        final List<TextMove> movesList = new ArrayList<>();
        final String fileContent = FileUtils.getTextFileAsString(fileToParse);
        final String[] moves = fileContent.split(";");
        for (final String currentMove : moves) {
            movesList.add(new TextMove(currentMove.trim()));
        }
        return movesList;
    }
    
}
