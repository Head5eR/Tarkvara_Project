package com.mygdx.game;

public class MeleeWeapon extends Weapon {
	String type;
	boolean twohanded;
	public MeleeWeapon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MeleeWeapon(String name, boolean equipable) {
		super(name, equipable);
		// TODO Auto-generated constructor stub
	}

	public MeleeWeapon(String name, int rarity, boolean equipable) {
		super(name, rarity, equipable);
		// TODO Auto-generated constructor stub
	}

	public boolean isTwohanded() {
		return twohanded;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
