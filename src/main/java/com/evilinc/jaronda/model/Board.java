/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.model;

/**
 *
 * @author teton
 */
public class Board {

    private final Square[] firstRowSquares = new Square[8];
    private final Square[] secondRowSquares = new Square[8];
    private final Square[] thirdRowSquares = new Square[8];
    private final Square[] fourthRowSquares = new Square[1];

    public Board() {
        initSquares();
    }

    private void initSquares() {
        initFirstRowSquares();
        initSecondRowSquares();
        initThirdRowSquares();
        initFourthRowSquares();
    }

    private void initFirstRowSquares() {
        for (int i = 0; i < firstRowSquares.length; i++) {
//            firstRowSquares[i] = new Square();
        }
    }

    private void initSecondRowSquares() {

    }

    private void initThirdRowSquares() {

    }

    private void initFourthRowSquares() {

    }

}
