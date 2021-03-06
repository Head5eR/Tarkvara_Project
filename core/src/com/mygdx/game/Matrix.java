package com.mygdx.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Matrix implements Serializable {
	private int length;
	private int width;
	private ArrayList<ArrayList<Integer>> columns = new ArrayList<ArrayList<Integer>>();
	
	public Matrix(int length, int width, int cellValues) {
		this.setLength(length);
		this.setWidth(width);
		
		for(int i=0; i<length; i++) {
			columns.add(new ArrayList<Integer>());
			for(int j=0; j<width; j++) {
				 (columns.get(i)).add(cellValues);
				
			}
		}
		//System.out.println(columns.toString());
	}
	
	public Matrix(int length, int width, ArrayList<ArrayList<Integer>> columns) {
		this.length = length;
		this.width = width;
		this.columns = columns;
	}
	
	public Matrix(int length, int width) {
		this.setLength(length);
		this.setWidth(width);
		
		for(int i=0; i<length; i++) {
			columns.add(new ArrayList<Integer>());
			for(int j=0; j<width; j++) {
				 (columns.get(i)).add(15); // 15 means 1 + 2 + 4 + 8 => first 4 bits, each of them is value of the wall => 01111, 5th bit is for "visited" flag
				
			}
		}
		//System.out.println(columns.toString());
	}
	
	public ArrayList<ArrayList<Integer>> getMatrix(){
		return columns;
	}
	
	public int getCell(Location loc) {
		//System.out.println("returned value: " + (int) columns.get(loc.getX()).get(loc.getY()));
		
		return (int) columns.get(loc.getX()).get(loc.getY());
		//return (int) columns.get(loc.getX()).get(loc.getY());
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}