package com.mygdx.game;


public class Location {
	private int x;
	private int y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString(){
		return x + "," + y;
	}
	
	public Location goLeft(int tiles) {
		return new Location(this.x-tiles, this.y);
	}
	
	public Location goRight(int tiles) {
		return new Location(this.x+tiles, this.y);
	}
	
	public Location goUp(int tiles) {
		return new Location(this.x, this.y-tiles);
	}
	
	public Location goDown(int tiles) {
		return new Location(this.x, this.y+tiles);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof Location)) {
			return false;
		}
	
		Location l = (Location) o;
		
		return ((x == l.getX()) && (y == l.getY()));
	}
}
