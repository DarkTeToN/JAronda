/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.enums;

import com.evilinc.jaronda.controller.serialization.AroParser;
import com.evilinc.jaronda.controller.serialization.SgfParser;
import com.evilinc.jaronda.interfaces.IFileParser;
import java.io.File;

/**
 *
 * @author teton
 */
public enum ESerializatonType {
    ARO(".aro", new AroParser()),
    SGF(".sgf", new SgfParser());

    
    private final String extension;
    private final IFileParser parser;

    private ESerializatonType(final String extension, final IFileParser parser) {
        this.extension = extension;
        this.parser = parser;
    }

    public String getExtension() {
        return extension;
    }

    public IFileParser getParser() {
        return parser;
    }
    
    public static IFileParser getParserFromFile(final File file) {
        for (final ESerializatonType currentType : values()) {
            if (file.getName().toLowerCase().endsWith(currentType.getExtension())) {
                return currentType.getParser();
            }
        }
        return null;
    }
    
}
