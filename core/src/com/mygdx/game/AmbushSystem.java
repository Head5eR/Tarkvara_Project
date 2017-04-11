package com.mygdx.game;

public class AmbushSystem {
	private double basicChance = 0.1;
	private double maxChance = 0.3;
	private MapGenerator mapG;
	private Hero hero;
	private int distance;
	private int maxDistance;
	private double chancePerTurn;
	private double attackChance;
	Location start = new Location(0,0);
	Location end = new Location(mapG.getLength()-1, mapG.getWidth()-1);
	
	public AmbushSystem(MapGenerator mapG, Hero hero) {
		this.hero = hero;
		this.mapG = mapG;
	}
	
	
	public int getDestanceFromEntrance() {
		Location locS = mapG.getStartPos();
		Location locH = hero.getLoc();
		this.distance = MapGenerator.getDistance(locS, locH);
		System.out.println(distance);
		return distance;
	}
	
	public void getMapMaxDistance() {
		this.maxDistance = MapGenerator.getDistance(start, end);
		System.out.println(maxDistance);		
	}
	
	public double getChancePerTurn() {
		chancePerTurn = maxChance/maxDistance;
		System.out.println(chancePerTurn);
		return chancePerTurn;
	}
	
	public double generateAttackChance(Location herolocation, Location mapstart) {
		distance = getDestanceFromEntrance();
		chancePerTurn = getChancePerTurn();
		attackChance = basicChance + chancePerTurn*distance;
		System.out.println(attackChance);
		return attackChance;		
	}
	
	public double getBasicChance() {
		return basicChance;
	}

	public int getDistance() {
		return distance;
	}

	
}
