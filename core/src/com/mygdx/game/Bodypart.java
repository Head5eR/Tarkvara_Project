package com.mygdx.game;

public class Bodypart {
	private String name;
	private boolean isCritical;
	
	public Bodypart(String name, boolean critical) {
		this.name = name;
		isCritical = critical;
	}
	
	@Override
	public String toString() {
		return name + " isCritical: " + isCritical;
	}

	public String getName() {
		return name;
	}

	public boolean isCritical() {
		return isCritical;
	}
}
