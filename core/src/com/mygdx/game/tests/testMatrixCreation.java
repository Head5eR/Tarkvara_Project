package com.mygdx.game.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Location;
import com.mygdx.game.Matrix;

public class testMatrixCreation {
	Matrix matrix;
	
	@Test
	public void testFillWithNumber() {
		int numberToFillMatrixWith = 5;
		boolean isFilledWithNumber = true;
		matrix = new Matrix(8, 3, numberToFillMatrixWith);
		for(int x=0; x<matrix.getLength(); x++) {
			for(int y=0; y<matrix.getWidth(); y++) {
				if(matrix.getCell(x, y) != numberToFillMatrixWith) {
					isFilledWithNumber = false;
					
				}
			}
		}
		assertTrue(isFilledWithNumber);
	}
	
	@Test
	public void testSetLoc() {
		int numberToLookFor = 5;
		matrix = new Matrix(8, 3, 1);
		int randX = MathUtils.random(matrix.getLength()-1);
		int randY = MathUtils.random(matrix.getWidth()-1);
		matrix.setCell(new Location(randX,randY), numberToLookFor);
		int foundNumber = matrix.getCell(randX, randY);
		assertEquals(numberToLookFor, foundNumber);
	}

}
