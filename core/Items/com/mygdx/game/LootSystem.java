package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;

public class LootSystem {
	
	public static Item generateLoot(Monster monster) { //insert monster difficulty
		String difficulty = monster.getMod().getName();
		String itemLvl = "";
		Item item = null;
		
		Random random = new Random();
		
		float x = random.nextFloat();
		
		String[] weaponTypes = {"axe", "sword", "shield", "blade"};
		String[] armorTypes = {"head", "feet", "arms", "legs", "body"};
		
		
		if(difficulty.equals("weak")) {  //generate loot according to the monster's difficulty			
			itemLvl = "1";
			
		} else if(difficulty.equals("medium")){
			if(x <= 0.7) {  //return item level 1-2
				itemLvl = "1";
			}else {
				itemLvl = "2";
			}
		} else if(difficulty.equals("strong")){  //return item level 1-2 (more chances to get item level 2)
			if(x <= 0.5) {  //return item level 1-2
				itemLvl = "1";
			}else {
				itemLvl = "2";
			}
			
		} else if(difficulty.equals("dangerous")){  //return item level 1-3
			if(x <= 0.2) {
				itemLvl = "1";
			}else if(x > 0.2 & x <= 0.6) {
				itemLvl = "2";
			}else {
				itemLvl = "3";
			}
		} else if(difficulty.equals("deadly")){  //return item level 1-3 (high chances to get item lvl2, medium chances to get item lvl3)
			if(x <= 0.05) {
				itemLvl = "1";
			}else if(x > 0.05 & x <= 0.65) {
				itemLvl = "2";
			}else {
				itemLvl = "3";
			}			
		}
		
		if(MathUtils.random(1) == 0) { // 50% armor 50% weapon
			String weaponType = weaponTypes[MathUtils.random(0, weaponTypes.length-1)];
			if(itemLvl != "") {
				if(weaponType != "shield") {
					item = MeleeWeapon.getMeleeWeaponRandRarity(weaponType, itemLvl);
				} else {
					item = Shield.getShieldRandRarity(itemLvl);
				}
			}
		} else {
			String armorType = armorTypes[MathUtils.random(0, armorTypes.length-1)];
			if(itemLvl != "") {
				item = Armor.getRandomArmor(armorType, itemLvl);
			}
		}
		return item;				
	}
	
	public static int dropPotion() {
		if(MathUtils.random(1) == 0) {
			return 1;
		}		
		return 0;
	}
	
}

