package com.mygdx.game;

import java.io.Serializable;

public class Bodypart implements Serializable {
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
