package com.mygdx.game;

public class AmbushSystem {
	private final double  basicChance = 0.1;
	private final double  maxChance = 0.3;
	private MapGenerator mapG;
	private Hero hero;
	private int distance;
	private int maxDistance;
	private double chancePerTurn;
	private double attackChance;
	Location start = new Location(0,0);
	Location end;
	
	public AmbushSystem(MapGenerator mapG, Hero hero) {
		this.hero = hero;
		this.mapG = mapG;
		this.end = new Location(mapG.getMap().getLength()-1, mapG.getMap().getWidth()-1);
		this.maxDistance = MapGenerator.getDistance(start, end);
		//this.maxDistance = MapGenerator.getDistance(mapG.getStartPos(), mapG.getEndPos());
		this.chancePerTurn = maxChance/((double) maxDistance);
	}
	
	public int monsterLevel() {
		java.util.Random rand = new java.util.Random();
		Double dist = (double) distance;
		Double maxdist = (double) maxDistance;
		if(dist < maxdist/3) {
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
		} else if(dist > (maxdist*2)/3) {
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
	
	
	public int getDistanceFromEntrance() {
		Location locS = mapG.getStartPos();
		Location locH = hero.getLoc();
		this.distance = MapGenerator.getDistance(locS, locH);
		return distance;
	}
	
	public double generateAttackChance() {
		distance = getDistanceFromEntrance();
		attackChance = basicChance + chancePerTurn*(double) distance;
		if(attackChance > basicChance+maxChance) {
			attackChance = basicChance+maxChance;
		}
		return attackChance;		
	}
	
	public double getBasicChance() {
		return basicChance;
	}

	public int getDistance() {
		return distance;
	}

	
}
