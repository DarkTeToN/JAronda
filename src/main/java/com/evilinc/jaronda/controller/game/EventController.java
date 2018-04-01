/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller.game;

import com.evilinc.jaronda.model.EventBean;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author teton
 */
public class EventController extends PropertyChangeSupport {
    
    private static EventController instance;
    
    private EventController() {
        super(new EventBean());
    }
    
    public static EventController getInstance() {
        if (instance == null) {
            instance = new EventController();
        }
        return instance;
    }
    
}
