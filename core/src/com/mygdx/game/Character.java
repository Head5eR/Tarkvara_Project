package com.mygdx.game;

public abstract class Character {
	private int strength;
	private int dexterity;
	private int stamina;
	protected int hp;
	private Body body;
	
	public Character(int strength, int dexterity, int stamina, String bodytype) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.stamina = stamina;
		this.hp = getMaxHp();
		
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
	
	public int getAttackDamage() {
		return (int) Math.round(getStrength()*getStamina()*0.2);
	}
	
	public int getWrath() {
		return (int) Math.round(getStrength()*getStamina()*getDexterity()*0.02);
	}
	
	public float getEvasion() {
		return (float) (getDexterity()*getStamina()*0.1);
	}
}
