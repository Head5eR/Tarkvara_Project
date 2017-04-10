package com.mygdx.game;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;

public class Shield extends Weapon {
	
	private Shield(int minDamage, int maxDamage, int str, int dex, int stam, int wrath, String twohanded, String type,
			String level, String name) {
		super(minDamage, maxDamage, str,dex,stam, wrath, twohanded, type, level, name);
	}
	
	public static Shield getShieldRandRarity(String level) {
		String type = "shield";
		List <String> weaponStats = getWeaponFromXML(type, level);
		String name = weaponStats.get(0);
		int  minDamage = Integer.parseInt(weaponStats.get(1));
		int maxDamage = Integer.parseInt(weaponStats.get(2));
		int strength = Integer.parseInt(weaponStats.get(3));
		int dexterity = Integer.parseInt(weaponStats.get(4));
		int stamina = Integer.parseInt(weaponStats.get(5));
		int wrath = Integer.parseInt(weaponStats.get(6));
		String twohanded = weaponStats.get(7);
		System.out.println("mindmg and maxdmg " + minDamage + " " + maxDamage);
		return new Shield(minDamage, maxDamage, strength, dexterity, stamina, wrath, twohanded, type, level, name);
	}
}
