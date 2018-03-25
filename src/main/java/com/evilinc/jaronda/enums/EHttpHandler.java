/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.enums;

import com.evilinc.jaronda.consts.HttpConst;

/**
 *
 * @author teton
 */
public enum EHttpHandler {
    START_NEW_GAME(HttpConst.START_NEW_GAME),
    GET_BOARD(HttpConst.GET_BOARD),
    PLAY_MOVE(HttpConst.PLAY_MOVE);
    
    private final String context;
    
    private EHttpHandler(final String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }
    
}
