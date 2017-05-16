package com.mygdx.game.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;

import com.mygdx.game.Location;
import com.mygdx.game.MapGenerator;
import com.mygdx.game.Matrix;

public class testMapSize {
	
	int mapLength = 6;
	int mapWidth = 9;
	MapGenerator mapgen = new MapGenerator(mapLength,mapWidth);
	Matrix map;
	
	@Before
	public void prepareMap() {
		mapgen.generateMap();
		map = mapgen.getMap();
	}
	
	@Test
	public void testMapLength() {
		assertEquals(mapLength, map.getLength());
	}
	
	@Test
	public void testMapWidth() {
		assertEquals(mapWidth, map.getWidth());
	}
	
	@Test
	public void testBossLocationIsNearTheExit() {
		Location bossLocation = mapgen.getBossLoc();
		Location exitLocation = mapgen.getEndPos();
		boolean isNearExit = false;
		if(bossLocation.goUp(1).equals(exitLocation) || 
				bossLocation.goDown(1).equals(exitLocation) ||
				bossLocation.goLeft(1).equals(exitLocation) ||
				bossLocation.goRight(1).equals(exitLocation)) 
		{
			isNearExit = true;
		}
		assertTrue(isNearExit);
	}

}
