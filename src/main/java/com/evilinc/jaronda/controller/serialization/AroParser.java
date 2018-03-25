/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.serialization;

import com.evilinc.jaronda.exceptions.InvalidAroMoveException;
import com.evilinc.jaronda.model.serialization.aro.AroMove;
import com.evilinc.jaronda.utils.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author teton
 */
public class AroParser {

    private final File file;

    public AroParser(final File fileToParse) {
        this.file = fileToParse;
    }

    public List<AroMove> parse() throws IOException, InvalidAroMoveException {
        final List<AroMove> movesList = new ArrayList<>();
        final String fileContent = FileUtils.getTextFileAsString(file);
        final String[] moves = fileContent.split(";");
        for (final String currentMove : moves) {
            movesList.add(new AroMove(currentMove.trim()));
        }
        return movesList;
    }

    public void writeToFile(final List<AroMove> gameMoves) throws IOException {
        final StringBuilder outputContentBuilder = new StringBuilder();
        final FileWriter outputFileWriter = new FileWriter(file, false);
        for (final AroMove currentMove : gameMoves) {
            outputContentBuilder.append(currentMove).append(";");
        }
        outputContentBuilder.setLength(outputContentBuilder.length() - 1);
        outputFileWriter.write(outputContentBuilder.toString());
        outputFileWriter.flush();
    }

}
