/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author teton
 */
public class FileUtils {

    public static String getTextFileAsString(final File fileToRead) throws FileNotFoundException, IOException {
        final StringBuilder builder = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            builder.append(currentLine).append("\n");
        }
        return builder.toString();
    }

}
