package com.mygdx.game;

public class AmbushSystem {
	private final double  basicChance = 0.1;
	private final double  maxChance = 0.3;
	private MapGenerator mapG;
	private int maxDistance;
	private double chancePerTurn;
	private double attackChance;
	private Hero hero;
	Location start;
	Location end;
	
	public AmbushSystem(MapGenerator mapG, Hero hero) {
		this.mapG = mapG;
		this.hero = hero;
		//this.end = new Location(mapG.getMap().getLength()-1, mapG.getMap().getWidth()-1);
		this.start = mapG.getStartPos();
		this.end = mapG.getEndPos();
		this.maxDistance = MapGenerator.getDistance(start, end);
		//this.maxDistance = MapGenerator.getDistance(mapG.getStartPos(), mapG.getEndPos());
		this.chancePerTurn = maxChance/((double) maxDistance);
	}
	
	public int monsterLevel(Location givenLoc) {
		java.util.Random rand = new java.util.Random();
		Double dist = (double) MapGenerator.getDistance(givenLoc, mapG.getStartPos());
		Double maxdist = (double) maxDistance;
		
		if(dist <= maxdist/3) {
			if(rand.nextFloat() <= 0.6) {
				return 0;
			} else {
				return 1;
			}
		} else if(dist > maxdist/3 & dist < (maxdist*2)/3) {
			float r = rand.nextFloat();
			if(r <= 0.3) {
				return 1;
			} else if(r > 0.3 & r < 0.5) {
				return 2;
			} else {
				return 3;
			}
		} else if(dist >= (maxdist*2)/3) {
			float r = rand.nextFloat();
			if(r <= 0.3) {
				return 2;
			} else if(r > 0.3 & r < 0.5) {
				return 3;
			} else {
				return 4;
			}
		}
		return -1;
	}
	
	
	public int getHeroDistanceFromEntrance() {
		Location locS = mapG.getStartPos();
		Location locH = hero.getLoc();
		return MapGenerator.getDistance(locS,locH);
	}
	
	public double generateAttackChance() {
		double distance = getHeroDistanceFromEntrance();
		attackChance = basicChance + chancePerTurn*(double) distance;
		if(attackChance > basicChance+maxChance) {
			attackChance = basicChance+maxChance;
		}
		return attackChance;		
	}
	
	public double getBasicChance() {
		return basicChance;
	}	
}
