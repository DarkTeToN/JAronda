/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.serialization;

import com.evilinc.jaronda.exceptions.InvalidAroMoveException;
import com.evilinc.jaronda.interfaces.IFileParser;
import com.evilinc.jaronda.model.serialization.aro.AroMove;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author teton
 */
public class SgfParser implements IFileParser {

    @Override
    public List<AroMove> parse(File fileToParse) throws IOException, InvalidAroMoveException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void serialize(File outputFile, List<AroMove> gameMoves) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
