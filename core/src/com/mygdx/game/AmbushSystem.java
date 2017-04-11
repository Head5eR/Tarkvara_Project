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
		//this.maxDistance = MapGenerator.getDistance(start, end);
		this.maxDistance = MapGenerator.getDistance(mapG.getStartPos(), mapG.getEndPos());
		this.chancePerTurn = maxChance/((double) maxDistance);
	}
	
	
	public int getDistanceFromEntrance() {
		Location locS = mapG.getStartPos();
		Location locH = hero.getLoc();
		this.distance = MapGenerator.getDistance(locS, locH);
		return distance;
	}
	
	public double generateAttackChance() {
		System.out.println("chance per turn: " + chancePerTurn);
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
