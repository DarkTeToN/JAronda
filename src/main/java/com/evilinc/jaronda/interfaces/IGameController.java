/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.interfaces;

import java.io.File;

/**
 *
 * @author teton
 */
public interface IGameController {
    
    public void startNewGame();
    
    public void cancelLastMove();
    
    public void saveGame(final File outputFile);
    
    public void loadGame();
    
}
