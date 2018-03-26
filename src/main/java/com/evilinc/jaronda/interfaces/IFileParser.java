/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.interfaces;

import com.evilinc.jaronda.exceptions.InvalidAroMoveException;
import com.evilinc.jaronda.model.serialization.aro.AroMove;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author teton
 */
public interface IFileParser {

    public abstract List<AroMove> parse(final File fileToParse) throws IOException, InvalidAroMoveException;

    public abstract void serialize(final File outputFile, final List<AroMove> gameMoves) throws IOException;
}
