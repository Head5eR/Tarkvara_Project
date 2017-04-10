package com.mygdx.game;

import java.util.List;

public class MeleeWeapon extends Weapon {
		
	private MeleeWeapon(int minDamage, int maxDamage, int str, int dex, int stam, int wrath, String twohanded, String type,
			String level, String name) {
		super(minDamage, maxDamage, str,dex,stam, wrath, twohanded, type, level, name);
	}
	
	public static MeleeWeapon getMeleeWeaponRandRarity(String type, String level) {
		List <String> weaponStats = getWeaponFromXML(type, level);
		String name = weaponStats.get(0);
		int  minDamage = Integer.parseInt(weaponStats.get(1));
		int maxDamage = Integer.parseInt(weaponStats.get(2));
		int strength = Integer.parseInt(weaponStats.get(3));
		int dexterity = Integer.parseInt(weaponStats.get(4));
		int stamina = Integer.parseInt(weaponStats.get(5));
		int wrath = Integer.parseInt(weaponStats.get(6));
		String twohanded = weaponStats.get(7);
		
		return new MeleeWeapon(minDamage, maxDamage, strength, dexterity, stamina, wrath, twohanded, type, level, name);
	}

}
