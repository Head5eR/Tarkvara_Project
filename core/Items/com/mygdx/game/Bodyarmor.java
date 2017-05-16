package com.mygdx.game;

import java.util.List;

public class Bodyarmor extends Armor {

	 public static Bodyarmor getBodyarmorRandRarity(String level) {
	  String type = "body";
	  List <String> armorStats = getArmorFromXML(type, level);
	  String name = armorStats.get(0);
	  int armor = Integer.parseInt(armorStats.get(1));
	  int strength = Integer.parseInt(armorStats.get(2));
	  int dexterity = Integer.parseInt(armorStats.get(3));
	  int stamina = Integer.parseInt(armorStats.get(4));
	  int wrath = Integer.parseInt(armorStats.get(5));
	  
	  return new Bodyarmor(strength, dexterity, stamina, wrath, type, level, name, armor);
	 }
	 
	 private Bodyarmor(int str, int dex, int stam, int wrath, String type, 
			 String level, String name, int armor) {
		 super(str, dex, stam, wrath, type, level, name, armor);
	 }
}