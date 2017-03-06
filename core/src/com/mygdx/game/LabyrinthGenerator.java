package com.mygdx.game;

import java.util.ArrayList;

public class LabyrinthGenerator {
	private int length;
	private int width;
	private int currentStep;
	private boolean wayExists;
	Location startPos;
	Location endPos;
	Matrix map;
	ArrayList<Location> route = new ArrayList<Location>();
	final int MOVELEFT = 1;
	final int MOVEUP = 2;
	final int MOVERIGHT = 4;
	final int MOVEDOWN = 8;
	final int VISITED = 128;
	
	public LabyrinthGenerator(int length, int width){
		this.length = length;
		this.width = width;
		this.startPos = new Location(0,0);
		route.add(startPos);
		
		this.map = new Matrix(length,width);
		markAsVisited(startPos);
		
		int randX = (int) Math.floor(Math.random()*(length-1));
		int randY = (int) Math.floor(Math.random()*(width-1));
		//this.endPos = new Location(randX,randY);
		this.endPos = new Location(length-1, width-1);
		//System.out.println(endPos);
	}
	
	public void generateMap() {
		//MatrixPrinter printer = new MatrixPrinter();
		while(move());
		
		//printer.printMap(map, true);
		//printer.printMapImproved(map);
	}
	
	private void addStep(Location newLoc, int direction) {
		if (newLoc.equals(endPos)) { return; }
		route.add(newLoc);
		//markAsVisited(route.get(currentStep));
		markAsVisited(newLoc);
	}
	
	private void markAsVisited(Location loc) {
		map.setCell(loc, map.getCell(loc)+VISITED);
	}
	
	
	
	private boolean move() {
		if(route.size() == 0){ return false;}
		Location currentLoc = route.get(currentStep);
		wayExists = (canMove(MOVELEFT, currentLoc) || canMove(MOVEUP, currentLoc) || canMove(MOVERIGHT, currentLoc) || canMove(MOVEDOWN, currentLoc));
		if(wayExists) {
			int direction = (int) Math.floor(Math.random() * 4);
			switch(direction) {
			case 0: if (canMove(MOVELEFT, currentLoc)) {
				Location newLoc = new Location(currentLoc.getX()-1, currentLoc.getY());
				addStep(newLoc, direction);
				map.minusInt(currentLoc, MOVELEFT);
				map.minusInt(newLoc, MOVERIGHT);
			}	break;
			
			case 1: if (canMove(MOVEUP, currentLoc)) {
				Location newLoc = new Location(currentLoc.getX(), currentLoc.getY()-1);
				addStep(newLoc, direction);
				map.minusInt(currentLoc, MOVEUP);
				map.minusInt(newLoc, MOVEDOWN);	
			} break;
			case 2: if (canMove(MOVERIGHT, currentLoc)) {
				Location newLoc = new Location(currentLoc.getX()+1, currentLoc.getY());
				addStep(newLoc, direction);
				map.minusInt(currentLoc, MOVERIGHT);
				map.minusInt(newLoc, MOVELEFT);
			} break;
			case 3: if (canMove(MOVEDOWN, currentLoc)) {
				Location newLoc = new Location(currentLoc.getX(), currentLoc.getY()+1);
				addStep(newLoc, direction);
				map.minusInt(currentLoc, MOVEDOWN);
				map.minusInt(newLoc, MOVEUP);	
			} break;
		}
			
			currentStep = route.size() - 1;
			
		} else {
			route.remove(currentStep); // removing route point from which it's impossible to continue moving 
			currentStep = 0; // taking next cell
			return (route.size() > 0); // will return false when array of possible route points will be empty
		}
		return true;
		
	}
	
	private boolean canMove(int direction, Location loc) {
		switch(direction){
			case MOVELEFT: return((loc.getX() > 0) && (map.getCell(loc.getX()-1, loc.getY()) < VISITED));
			case MOVEUP: return(loc.getY() > 0 && (map.getCell(loc.getX(), loc.getY()-1) < VISITED));
			case MOVERIGHT: return(length-1 > loc.getX() && (map.getCell(loc.getX()+1, loc.getY()) < VISITED));
			case MOVEDOWN: return(width-1 > loc.getY() && (map.getCell(loc.getX(), loc.getY()+1) < VISITED));
		}
		return false;
	}
	
	public Matrix getMap(){
		return map;
	}
}
