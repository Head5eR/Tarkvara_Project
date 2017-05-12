package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;

public abstract class Character {
	private int strength;
	private int dexterity;
	private int stamina;
	private String name;
	protected int hp;
	private int attackMove;
	private Body body;
	private ArrayList<Integer> pickedDefs = new ArrayList<Integer>();
	private ArrayList<Integer> pickedAttacks = new ArrayList<Integer>();
	
	public Character(int strength, int dexterity, int stamina, String bodytype, String name) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.stamina = stamina;
		this.name = name;
		
		body = new Body(bodytype);
	}

	public int getStrength() {
		return strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public int getStamina() {
		return stamina;
	}

	public int getHp() {
		return hp;
	}
	
	public int getMaxHp() {
		return strength*13;
	}

	public Body getBody() {
		return body;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getMinAttackDamage() {
		return (int) Math.round(getMaxAttackDamage() * 0.7);
	}
	
	public int getMaxAttackDamage() {
		return (int) Math.round(getStrength()*getStamina()*0.2);
	}
	
	public int getAttackDamage() {
		return MathUtils.random(getMinAttackDamage(), getMaxAttackDamage());
	}
	
	public String getAttackDamageStr() {
		return getMinAttackDamage() + "-" + getMaxAttackDamage();
	}
	
	public int getWrath() {
		return (int) Math.round(getStrength()*getStamina()*getDexterity()*0.02);
	}
	
	public float getEvasion() {
		return (float) (getDexterity()*0.1);
	}
	
	public ArrayList<Integer> getPickedDefs() {
		return pickedDefs;
	}
	
	public ArrayList<Integer> getPickedAttacks() {
		return pickedAttacks;
	}

	public int getAttackMove(int num) {
		return pickedAttacks.get(num);
	}
	
	public boolean canDefend(int attackMove) {
		return pickedDefs.contains(attackMove);
	}

	public void setAttackMove(int attackMove) {
		this.attackMove = attackMove;
	}

	public void setDefenceMove(int defenceMove) {
		pickedDefs.add(defenceMove);
	}
	
	public String getName() {
		return name;
	}
	
	public int checkIfFatalStrike(int dmg, int bodypart) {
		if(body.getBodyParts().get(bodypart).isCritical()) {
			if((new Random()).nextFloat() <= 1/100) {
				System.out.println("FATAL STRIKE!!!");
				dmg = this.getHp();
			}
		}
		return dmg;
	}
	
	public void takeDmg(int dmg, int bodypart) {
		dmg = checkIfFatalStrike(dmg, bodypart);
		//System.out.println(this.name + " has taken " + dmg + " DMG");
		if (dmg < hp) {
			hp -= dmg;
		} else {
			hp = 0;
		}
	}

	public void setPickedDefs(ArrayList<Integer> pickedDefs) {
		this.pickedDefs = pickedDefs;
	}

	public void setPickedAttacks(ArrayList<Integer> pickedAttacks) {
		this.pickedAttacks = pickedAttacks;
	}
	
	
}
