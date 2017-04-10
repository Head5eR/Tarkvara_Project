package com.mygdx.game;

import java.util.List;

public class Legarmor extends Armor {

	public static Legarmor getLegarmorRandRarity(String level) {
		String type = "legs";
		List <String> armorStats = getArmorFromXML(type, level);
		String name = armorStats.get(0);
		int armor = Integer.parseInt(armorStats.get(1));
		int strength = Integer.parseInt(armorStats.get(2));
		int dexterity = Integer.parseInt(armorStats.get(3));
		int stamina = Integer.parseInt(armorStats.get(4));
		int wrath = Integer.parseInt(armorStats.get(5));
		
		return new Legarmor(strength, dexterity, stamina, wrath, type, level, name, armor);
	}
	
	private Legarmor(int str, int dex, int stam, int wrath, String type, String level, String name, int armor) {
		super(str, dex, stam, wrath, type, level, name, armor);
		
	}
}
