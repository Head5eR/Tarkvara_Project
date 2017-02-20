package com.mygdx.game;

import java.util.ArrayList;

public class Matrix {
	private int width;
	private int height;
	private ArrayList<ArrayList> columns = new ArrayList<>();
	
	public Matrix(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
		
		for(int i=0; i<width; i++) {
			columns.add(new ArrayList<>());
			for(int j=0; j<height; j++) {
				 (columns.get(i)).add(15); // 15 means 1 + 2 + 4 + 8 => first 4 bits, each of them is value of the wall => 01111, 5th bit is for "visited" flag
				
			}
		}
		//System.out.println(columns.toString());
	}
	
	public ArrayList<ArrayList> getMatrix(){
		return columns;
	}
	
	public int getCell(Location loc) {
		//System.out.println("returned value: " + (int) columns.get(loc.getX()).get(loc.getY()));
		return (int) columns.get(loc.getX()).get(loc.getY());
	}
	
	public int getCell(int x, int y) {
		return (int) columns.get(x).get(y);
	}
	
	public void setCell(Location loc, int value) {
		columns.get(loc.getX()).set(loc.getY(), value);
	}
	
	public void minusInt(Location loc, int value) {
		//System.out.println("Old value: " + getCell(loc));
		setCell(loc, getCell(loc) - value);
		//System.out.println("New value: " + (getCell(loc) - value));
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}