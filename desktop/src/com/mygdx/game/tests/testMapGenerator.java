package com.mygdx.game.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;

import com.mygdx.game.Location;
import com.mygdx.game.MapGenerator;
import com.mygdx.game.Matrix;

public class testMapGenerator {
	
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
	public void testExitIsReachable() {
		Location exitLocation = mapgen.getEndPos();
		boolean isReachable = false;
		Matrix map = mapgen.getMap();
		if(map.getCell(exitLocation.goUp(1)) ==0 || 
				map.getCell(exitLocation.goDown(1)) ==0 ||
				map.getCell(exitLocation.goLeft(1)) ==0 ||
				map.getCell(exitLocation.goRight(1)) ==0) 
		{
			isReachable = true;
		}
		assertTrue(isReachable);
	}
	
	@Test
	public void distanceCalculation() {
		int distance = mapgen.getDistance(new Location(2,0), new Location(11,8));
		// (2-11)^2 + (0-8)^2 = 145 => 12
		assertEquals(12, distance);
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
