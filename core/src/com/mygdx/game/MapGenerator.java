package com.mygdx.game;

import java.util.ArrayList;

public class MapGenerator {
	private int length;
	private int width;
	private int currentStep;
	private boolean wayExists;
	private int tokens = -1;
	Location startPos;
	Location endPos;
	Matrix map;
	ArrayList<Location> route = new ArrayList<Location>();
	MatrixPrinter printer = new MatrixPrinter();
	final int MOVELEFT = 1;
	final int MOVEUP = 2;
	final int MOVERIGHT = 4;
	final int MOVEDOWN = 8;
	final int EMPTY = 0;
	final int WALL = 1;
	
	public MapGenerator(int length, int width){
		this.length = length;
		this.width = width;
		this.startPos = new Location(0,0);
		this.map = new Matrix(length,width, WALL);
		this.endPos = new Location(length-1, width-1);
		addStep(startPos);
		
	}
	
	public void generateMap() {
		
		//printer.printMap(map, false);
		while(move());
		
//		if(map.getCell(endPos) != 0) { // there is a rare alghorithm fault, when it never reaches the end position
//			this.map = new Matrix(length,width, WALL);
//			generateMap();
//		}
		printer.printMap(map, false);
		//printer.printMapImproved(map);
	}
	
	public void addStep(Location newLoc) {
		if ((newLoc.getX() == endPos.getX()) && (newLoc.getY() == endPos.getY())) {
			System.out.println("newloc equals endpos");
			map.setCell(newLoc, 0);
			 if(route.size() / (length*width) >= 0.5) {
				 route.clear();
			 }
			 else {
				 tokens = (int) Math.floor(length*width*0.5 - route.size());
			 }
		} else {
			route.add(newLoc);
			map.setCell(newLoc, 0);
		}
	}
		
	
	public boolean move() {
		if(route.size() == 0){ return false;}
//		System.out.println(route.toString());
		Location currentLoc = route.get(currentStep);
//		System.out.println("my currentLoc is: " + currentLoc);
//		System.out.println("way left: " + (canMove(MOVELEFT, currentLoc) && checkDiagonals(MOVELEFT, currentLoc.goLeft(1))));
//		System.out.println("way up: " + (canMove(MOVEUP, currentLoc) && checkDiagonals(MOVEUP, currentLoc.goUp(1))));
//		System.out.println("way right: " + (canMove(MOVERIGHT, currentLoc) && checkDiagonals(MOVERIGHT, currentLoc.goRight(1))));
//		System.out.println("way down: " + (canMove(MOVEDOWN, currentLoc) && checkDiagonals(MOVEDOWN, currentLoc.goDown(1))));
		wayExists = ((canMove(MOVELEFT, currentLoc) && checkDiagonals(MOVELEFT, currentLoc.goLeft(1))) ||  // check it
				(canMove(MOVEUP, currentLoc) && checkDiagonals(MOVEUP, currentLoc.goUp(1))) || 
				(canMove(MOVERIGHT, currentLoc) && checkDiagonals(MOVERIGHT, currentLoc.goRight(1))) || 
				(canMove(MOVEDOWN, currentLoc) && checkDiagonals(MOVEDOWN, currentLoc.goDown(1))));
//		System.out.println("way exists? " + wayExists);
		if(wayExists) {
			int direction = (int) Math.floor(Math.random() * 4);
			
			if(map.getCell(endPos) != 0 && searchForExit(currentLoc) > 0) {
				System.out.println("i'm near the exit " + currentLoc);
				switch(searchForExit(currentLoc)) {
					case 1: { direction = 0; } break;
					case 2: { direction = 1; } break;
					case 4: { direction = 2; } break;
					case 8:{ direction = 3; } break;
				}
			} 
			
			switch(direction) {
				case 0: if (canMove(MOVELEFT, currentLoc) && checkDiagonals(MOVELEFT, currentLoc.goLeft(1))) {
					System.out.println("going left");
					Location newLoc = new Location(currentLoc.getX()-1, currentLoc.getY());
					addStep(newLoc);
				}	break;
				case 1: if (canMove(MOVEUP, currentLoc) && checkDiagonals(MOVEUP, currentLoc.goUp(1))) {
					System.out.println("going up");
					Location newLoc = new Location(currentLoc.getX(), currentLoc.getY()-1);
					addStep(newLoc);
				} break;
				case 2: if (canMove(MOVERIGHT, currentLoc) && checkDiagonals(MOVERIGHT, currentLoc.goRight(1))) {
					System.out.println("going right");
					Location newLoc = new Location(currentLoc.getX()+1, currentLoc.getY());
					addStep(newLoc);
				} break;
				case 3: if (canMove(MOVEDOWN, currentLoc) && checkDiagonals(MOVEDOWN, currentLoc.goDown(1))) {
					System.out.println("going down");
					Location newLoc = new Location(currentLoc.getX(), currentLoc.getY()+1);
					addStep(newLoc);
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
			case MOVELEFT: return((loc.getX() > 0) && (map.getCell(loc.getX()-1, loc.getY()) == WALL));
			case MOVEUP: return(loc.getY() > 0 && (map.getCell(loc.getX(), loc.getY()-1) == WALL));
			case MOVERIGHT: return(length-1 > loc.getX() && (map.getCell(loc.getX()+1, loc.getY()) == WALL));
			case MOVEDOWN: return(width-1 > loc.getY() && (map.getCell(loc.getX(), loc.getY()+1) == WALL));
		}
		return false;
	}
	
	public boolean checkDiagonals(int direction, Location loc) { // loc here is location where generator wants to go
		boolean diagonals = false;
		boolean directed = false; //checking next tile if possible (so there is no crossroads)
		//System.out.println("wanna go: " + loc);
		if (direction == MOVELEFT || direction == MOVERIGHT) {
			if (loc.getY() > 0) {
				if (loc.getY() < width-1) {
					diagonals = (canMove(MOVEUP, loc) && canMove(MOVEDOWN, loc));
				} else {
					diagonals = canMove(MOVEUP, loc); 
				}
			} else {
				diagonals = canMove(MOVEDOWN, loc);
			}
			
			if((direction == MOVELEFT && loc.getX() > 0) || (direction == MOVERIGHT && loc.getX() < length-1)) {
				directed = canMove(direction, loc);
			} else {
				directed = true;
			}
			
		} else if (direction == MOVEUP || direction == MOVEDOWN) {
			if (loc.getX() > 0) {
				if (loc.getX() < length-1) {
					diagonals = (canMove(MOVERIGHT, loc) && canMove(MOVELEFT, loc));
				} else {
					diagonals = canMove(MOVELEFT, loc);
				}
			} else {
				diagonals = canMove(MOVERIGHT, loc);
			}
			
			if((direction == MOVEUP && loc.getY() > 0) || (direction == MOVEDOWN && loc.getY() < width-1)) {
				directed = canMove(direction, loc);
			} else {
				directed = true;
			}
		}
		return (diagonals && directed);
	}
	
	private int searchForExit(Location currentLoc) { // should go to exit if it's nearby
		if(Math.abs(currentLoc.getX()-endPos.getX()) + Math.abs(currentLoc.getY()-endPos.getY()) == 1) {
			 if(Math.abs(currentLoc.getX()-endPos.getX()) == 1) {
				 if(Math.abs(currentLoc.getX()) > Math.abs(endPos.getX())) {
					 return MOVELEFT; // currently y is larger than needed
				 } else {
					 return MOVERIGHT;
				 } 
			 } else if (Math.abs(currentLoc.getY()-endPos.getY()) == 1) {
				 if(Math.abs(currentLoc.getY()) > Math.abs(endPos.getY())) {
					 return MOVEUP; // currently y is larger than needed
				 } else {
					 return MOVEDOWN;
				 }
			 }
		}
		return 0;
	}
	
	public Matrix getMap(){
		return map;
	}
	
}