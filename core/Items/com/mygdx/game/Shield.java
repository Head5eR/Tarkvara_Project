package com.mygdx.game;

import java.util.List;

public class Shield extends Weapon {
	int armor;
	private Shield(int minDamage, int maxDamage, int str, int dex, int stam, int wrath, String twohanded, String type,
			String level, String name, int armor) {
		super(minDamage, maxDamage, str,dex,stam, wrath, twohanded, type, level, name);
		this.armor = armor;
	}
	
	public static Shield getShieldRandRarity(String level) {
		String type = "shield";
		List <String> weaponStats = getWeaponFromXML(type, level);
		String name = weaponStats.get(0);
		int armor = Integer.parseInt(weaponStats.get(1));
		int strength = Integer.parseInt(weaponStats.get(2));
		int dexterity = Integer.parseInt(weaponStats.get(3));
		int stamina = Integer.parseInt(weaponStats.get(4));
		int wrath = Integer.parseInt(weaponStats.get(5));
		String twohanded = weaponStats.get(6);
		return new Shield(0,0, strength, dexterity, stamina, wrath, twohanded, type, level, name, armor);
	}

	protected int getArmor() {
		return armor;
	}
}
