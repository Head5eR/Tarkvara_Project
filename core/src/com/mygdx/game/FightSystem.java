package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;


public class FightSystem {
	static Random randomizer;
	
	public static void genDefencesForMonster(Monster m) {
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
	
	public static void fight(Hero h, Monster m) {
		genDefencesForMonster(m);
		if(h.getDexterity() > m.getDexterity()) {
			attack(h, m);
			if(!isDead(m)) {
				attack(m, h);
			} else {
				System.out.println("Monster has been struck to death!");
			}
		} else {
			attack(m, h);
			if(!isDead(h)) {
				attack(h, m);
			} else {
				System.out.println("GAME OVER");
			}
			
		}
	}
	
	private static void attack(Character attacker, Character target) {
		randomizer = new java.util.Random();
		
		if(target.canDefend(attacker.getAttackMove())) {
			System.out.println(target.getName() + " has blocked the attack!");
		} else {
			float bodypartExtraEvasion = 0; // probably need to implement bodyparts' own hit chances
			if(target.getBody().getBodyParts().get(attacker.getAttackMove()).isCritical()) {
				bodypartExtraEvasion = 0.1f;
			}
			
			if(randomizer.nextFloat() <= target.getEvasion()/10 + bodypartExtraEvasion) {
				System.out.println(target.getName() + " DODGED THE ATTACK!");
			} else {
				int dealtDamage = attacker.getAttackDamage();
				if(randomizer.nextFloat() <= attacker.getWrath()/1000) {
					System.out.println("CRITICAL STRIKE!");
					dealtDamage += dealtDamage*0.3;
				}
				System.out.println(attacker.getName() + " deals " + dealtDamage + " dmg");
				target.takeDmg(dealtDamage, attacker.getAttackMove());
			}
		}
	}
	
	public static boolean isDead(Character c) {
		return c.getHp() == 0;
	}

}
