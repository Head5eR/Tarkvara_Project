package com.mygdx.game;

public class Bodypart {
	public String name;
	public boolean isCritical;
	
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
}
