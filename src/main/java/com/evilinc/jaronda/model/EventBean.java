/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model;

import com.evilinc.jaronda.model.serialization.json.JsonMove;

/**
 *
 * @author teton
 */
public class EventBean {
    
    public String playerTurn;
    public int[] movePlayed;
    public JsonMove lastMoveNotification;
}
