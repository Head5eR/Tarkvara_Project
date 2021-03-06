package com.mygdx.game;

import java.io.Serializable;

import com.badlogic.gdx.math.MathUtils;

public abstract class Item implements Serializable {
	private String name;
	private String[] rarities = {"Common","Uncommon", "Rare", "Mystic", "Legendary"};
	private String rarity; // 1-5 (Common, Uncommon, Rare, Mystic, Legendary)
	private boolean equippable;
	
	public Item() {
		this.rarity = rarities[MathUtils.random(4)];
	}
	
	public Item(String name, boolean equippable) {
		this.name = name;
		this.rarity = rarities[MathUtils.random(4)];
		this.equippable = equippable;
	}
	
	public Item(String name, int rarity, boolean equippable) {
		super();
		this.name = name;
		this.rarity = rarities[rarity];
		this.equippable = equippable;
	}
	public boolean isEquipable() {
		return equippable;
	}
	
	public boolean checkIfWeapon(Item item) {
		return item instanceof Weapon;
	}
	
	public boolean checkIfShield(Item item) {
		return item instanceof Shield;
	}
	
	public boolean checkIfMelee(Item item) {
		return item instanceof MeleeWeapon;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	
	@Override
	public String toString() {
		return rarity + " " + name;
	}
	
	public static boolean isArmorItem(Item item) {
		return item instanceof Armor;
	}
	
	
}
