package com.mygdx.game;


import java.util.ArrayList;

public class MatrixPrinter {
	
	public void printMap(Matrix m, boolean labyrinthVersion) {
		//System.out.println("received matrix");
		ArrayList<ArrayList<Integer>> matrix = m.getMatrix();
		for(int i=0; i<matrix.get(0).size(); i++) {
			for(int j=0; j<matrix.size(); j++) {
				if((int) matrix.get(j).get(i)>128) {
					//System.out.print((int) matrix.get(j).get(i)-128 + "|");
				} else { 
					//System.out.print((int) matrix.get(j).get(i) + "|");
				}
				
			}
			System.out.println();
		}
	}
	
	public void printMapImproved(Matrix m) {
		ArrayList<ArrayList<Integer>> matrix = m.getMatrix();
		for(int i=0; i<matrix.size(); i++) { // row 
			for(int j=0; j<matrix.get(0).size(); j++) { // column
				if(i==0 && j==0) {
					String str = new String(new char[matrix.size()]).replace("\0", " _");
					System.out.print(str);
					System.out.println();
				}
				int value = (int) matrix.get(j).get(i) - 128;
				String binaryValue = Integer.toBinaryString(value);
				
				if(binaryValue.length()<4) {
					String str2 = new String(new char[4-binaryValue.length()]).replace("\0", "0");
					binaryValue = str2 + binaryValue;
				}
				
				if(binaryValue.endsWith("1")){
					System.out.print("|");
				}
				
				if(binaryValue.startsWith("1")){
					System.out.print("_ ");
				} else {
					System.out.print("  ");
				}
				
				if(j==matrix.size()-1) {
					System.out.print("|");
				}
			}
			System.out.println();
		}
	}
}
