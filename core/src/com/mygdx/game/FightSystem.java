package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;


public class FightSystem {
	static Random randomizer;
	
	private static void genDefencesForMonster(Monster m) {
		ArrayList<Integer> pickedDefs = m.getDefenceMoves();
		pickedDefs.clear();
		int numOfDefs =(int) Math.ceil(m.getBody().getBodyParts().size() / 4);
		for(int i=0; i<numOfDefs; i++) {
			int pickedPartToBlock = MathUtils.random(m.getBody().getBodyParts().size());
			if(!pickedDefs.contains(pickedPartToBlock)){
				pickedDefs.add(pickedPartToBlock);
			}
		}
	}
	
	private static void genAttacksForMonster(Monster m, Hero h) {
		ArrayList<Integer> pickedAttacks = m.getPickedAttacks();
		pickedAttacks.add(MathUtils.random(h.getBody().getBodyParts().size()-1));
	}
	
	public static int howManyAttacksToPick(Hero h) {
		String weaponSlotClass1 = "";
		String weaponSlotClass2 = "";
		if(!h.isSlotEmpty(h.weaponSlot1)) {
			weaponSlotClass1 = h.weaponSlot1.get(0)[0].getClass().getSimpleName();
		}
		if(!h.isSlotEmpty(h.weaponSlot2)) {
			weaponSlotClass2 = h.weaponSlot2.get(0)[0].getClass().getSimpleName();
		}
		if(weaponSlotClass1 == weaponSlotClass2 & weaponSlotClass1 == "MeleeWeapon") {
			return 2;
		}
		return 1;
	}
	
	public static int howManyDefencesToPick(Hero h) {
		String weaponSlotClass2 = "";
		if(!h.isSlotEmpty(h.weaponSlot2)) {
			weaponSlotClass2 = h.weaponSlot2.get(0)[0].getClass().getSimpleName();
		}
		if(weaponSlotClass2.equals("Shield")) {
			return 2;
		}
		return 1;
	}
	
	public static void fight(Hero h, Monster m) {
		genDefencesForMonster(m);
		genAttacksForMonster(m,h);
		if(h.getDexterity() > m.getDexterity()) {
			for(int i=0; i<h.getPickedAttacks().size(); i++) {
				attack(h, m);
				if(!isDead(m)) {
					attack(m, h);
				} else {
					MyGdxGame.addToLog("Monster has been struck to death!");
				}
			}
			
		} else {
			attack(m, h);
			if(!isDead(h)) {
				attack(h, m);
			} else {
				MyGdxGame.addToLog("GAME OVER");
			}
			
		}
	}
	
	private static void attack(Character attacker, Character target) {
		randomizer = new java.util.Random();
		
		for(int attackMove : attacker.getPickedAttacks()) {
			if(target.canDefend(attackMove)) {
				target.getPickedDefs().remove(target.getPickedDefs().indexOf(attackMove));
				MyGdxGame.addToLog(target.getName() + " has blocked the attack!");
			} else {
				float bodypartExtraEvasion = 0; // probably need to implement bodyparts' own hit chances
				if(target.getBody().getBodyParts().get(attackMove).isCritical()) {
					bodypartExtraEvasion = 0.1f;
				}
				
				if(randomizer.nextFloat() <= target.getEvasion()/10 + bodypartExtraEvasion) {
					MyGdxGame.addToLog(target.getName() + " DODGED THE ATTACK!");
				} else {
					int dealtDamage = attacker.getAttackDamage();
					if(randomizer.nextFloat() <= attacker.getWrath()/1000) {
						MyGdxGame.addToLog("CRITICAL STRIKE!");
						dealtDamage += dealtDamage*0.3;
					}
					MyGdxGame.addToLog(attacker.getName() + " deals " + dealtDamage + " dmg");
					target.takeDmg(dealtDamage, attackMove);
				}
			}
		}
		attacker.getPickedAttacks().clear();
		
	}
	
	public static boolean isDead(Character c) {
		return c.getHp() == 0;
	}

}
