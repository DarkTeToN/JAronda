/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.consts;

/**
 *
 * @author teton
 */
public class HttpConst {
    
    public static final int WEB_SERVER_PORT = 11815;
    
    public static final String JARONDA_ROOT_PATH = "/jaronda";
    public static final String START_NEW_GAME = JARONDA_ROOT_PATH + "/startNewGame";
    public static final String GET_BOARD = JARONDA_ROOT_PATH + "/getBoard";
    public static final String PLAY_MOVE = JARONDA_ROOT_PATH + "/playMove";
    
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    
    public static final int REQUEST_OK_HTTP_CODE = 200;
    public static final int ERROR_HTTP_CODE = 520;
    public static final int WRONG_METHOD_HTTP_CODE = 405;
    public static final String WRONG_METHOD_ERROR = "Bad method in sent resquest";
    
}
