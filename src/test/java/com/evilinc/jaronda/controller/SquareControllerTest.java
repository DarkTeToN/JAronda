/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evilinc.jaronda.controller;

import com.evilinc.jaronda.model.Square;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author teton
 */
public class SquareControllerTest {

    private final SquareController squareController;

    public SquareControllerTest() {
        squareController = new SquareController();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testNeighbours() {
        for (int row = 0; row < squareController.squares.length; row++) {
            for (int squareNumber = 0; squareNumber < squareController.squares[row].length; squareNumber++) {
                final List<Square> adjacentSquares = squareController.getSquareAt(row, squareNumber).getAdjacentSquares();
                for (final Square currentSquare : adjacentSquares) {
                    System.out.println("Square[" + row + "," + squareNumber + "]: " + currentSquare.getRow() + "," + currentSquare.getSquareNumber());
                }
                switch (row) {
                    case 0:
                        testFirstRowSquare(squareNumber);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void testFirstRowSquare(final int squareNumber) {
        switch (squareNumber) {
            case 0:
                final List<Square> adjacentSquares = squareController.getSquareAt(0, squareNumber).getAdjacentSquares();
                for (final Square currentSquare : adjacentSquares) {

                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }
}
