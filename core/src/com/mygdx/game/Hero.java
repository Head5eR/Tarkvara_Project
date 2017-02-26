package com.mygdx.game;

public class Hero {
	private int strength;
	private int agility;
	private int intelligence;
	private Location loc;
	final int MOVELEFT = 1;
	final int MOVEUP = 2;
	final int MOVERIGHT = 4;
	final int MOVEDOWN = 8;
	final int EMPTY = 0;
	
	public Hero(int strength, int agility, int intelligence, Location loc) {
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
		this.loc = loc;
	}
	
	public int getHealth() {
		return strength * 18;
	}
	
	public int getMana() {
		return intelligence * 13;
	}
	
	public float getEvasion() {
		return (float) (agility*intelligence*0.1);
	}
	
	public int getArmor() {
		return (int) (agility*0.5);
	}
	
	public int getAttackDamage() {
		return (int) (strength*agility*0.2);
	}
	
	public int getWrath() {
		return (int) (strength*intelligence*0.02);
	}

	@Override
	public String toString() {
		return "Hero [strength=" + strength + ", agility=" + agility + ", intelligence=" + intelligence
				+ ", getHealth()=" + getHealth() + ", getMana()=" + getMana() + ", getEvasion()=" + getEvasion()
				+ ", getArmor()=" + getArmor() + ", getAttackDamage()=" + getAttackDamage() + ", getWrath()="
				+ getWrath() + "]";
	}
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	public void move(Matrix map, int direction) {
		switch(direction){
			case MOVELEFT: { if((loc.getX() > 0) && (map.getCell(loc.getX()-1, loc.getY()) == EMPTY)) {loc = loc.goLeft(1); } break;} 
			case MOVEUP: { if(loc.getY() > 0 && (map.getCell(loc.getX(), loc.getY()-1) == EMPTY)) {loc = loc.goUp(1); } break;} 
			case MOVERIGHT: { if(map.getLength()-1 > loc.getX() && (map.getCell(loc.getX()+1, loc.getY()) == EMPTY)) {loc = loc.goRight(1); } break;}
			case MOVEDOWN: { if(map.getWidth()-1 > loc.getY() && (map.getCell(loc.getX(), loc.getY()+1) == EMPTY)) {loc = loc.goDown(1); } break;} 
		}
			
	}
}
