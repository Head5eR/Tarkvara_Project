package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

public class Hero {
	private int strength;
	private int agility;
	private int intelligence;
	private Location loc;
	final int MOVELEFT = 1;
	final int MOVEUP = 2;
	final int MOVERIGHT = 4;
	final int MOVEDOWN = 8;
	final int EMPTY = 0;
	
	List<Item> inventory = new ArrayList<Item>();
	List<Headgear[]> headSlot = new ArrayList<Headgear[]>();
	List<Bodyarmor[]> torsoSlot = new ArrayList<Bodyarmor[]>();
	List<Gloves[]> armsSlot = new ArrayList<Gloves[]>();
	List<Legarmor[]> legsSlot = new ArrayList<Legarmor[]>();
	List<Boots[]> toesSlot = new ArrayList<Boots[]>();
	
	public Hero(int strength, int agility, int intelligence, Location loc) {
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
		this.loc = loc;
		
		// Ugly equipment slots initialization...
		// Every slot is an array of certain type with length of 1, 
		// so that it is possible to add only one item to the slot
		Headgear[] hs = new Headgear[1];
			hs[0] = new Headgear("Helmet", true);
			headSlot.add(hs);
		Bodyarmor[] ts = new Bodyarmor[1];
			ts[0] = new Bodyarmor("Chainmail", true);
			torsoSlot.add(ts);
		Gloves[] as = new Gloves[1];
			as[0] = new Gloves("Gauntlets", true);
			armsSlot.add(as);
		Legarmor[] ls = new Legarmor[1];
			ls[0] = new Legarmor("Pants", true);
			legsSlot.add(ls);
		Boots[] toes = new Boots[1];
			toes[0] = new Boots("High boots", true);
			toesSlot.add(toes);
	}
	
	public String getAllEquiped() {
		return "Character has following equipped items: \n 1.Head: " + headSlot.get(0)[0] + "\n 2.Torso: " 
				+ torsoSlot.get(0)[0] + " \n 3.Arms: " + armsSlot.get(0)[0] + " \n 4.Legs: " + legsSlot.get(0)[0] 
				+ "\n 5.Toes: " + toesSlot.get(0)[0];
	}
	
	public String getInventory() {
		String out = "";
		if(inventory.isEmpty()) {
			out = "Inventory is empty";
		} else {
			for(Item item : inventory) {
				out += item.toString() + "\n";
			}
		}
		return out;
	}
	
	public void unequip() {
		// TO-DO!
	}
	
	public void equipFromInv(int itemNumber) { // needs to be rewritten in the smarter way
		if(inventory.size() <= itemNumber) { // if larger then requested item doesn't exist
			Item item = (inventory.get(itemNumber));

			if(item.isEquipable()) {
				switch(item.getClass().toString()) {
					case("Headgear"): { 
						if(headSlot.get(0)[0] == null) { // if doesn't have item then just add it to the slot
							headSlot.get(0)[0] = (Headgear) inventory.get(itemNumber);
						} else {
							inventory.add(headSlot.get(0)[0]); // if there is an item in slot, then put it in inventory
							headSlot.get(0)[0] = (Headgear) inventory.get(itemNumber);
						}
					}
					case("Bodyarmor"): {
						if(torsoSlot.get(0)[0] == null) {
							torsoSlot.get(0)[0] = (Bodyarmor) inventory.get(itemNumber);
						} else {
							inventory.add(headSlot.get(0)[0]);
							torsoSlot.get(0)[0] = (Bodyarmor) inventory.get(itemNumber);
						}
					}
					case("Legarmor"): {
						if(legsSlot.get(0)[0] == null) {
							legsSlot.get(0)[0] = (Legarmor) inventory.get(itemNumber);
						} else {
							inventory.add(legsSlot.get(0)[0]);
							legsSlot.get(0)[0] = (Legarmor) inventory.get(itemNumber);
						}
					}
					case("Gloves"): {
						if(armsSlot.get(0)[0] == null) {
							armsSlot.get(0)[0] = (Gloves) inventory.get(itemNumber);
						} else {
							inventory.add(headSlot.get(0)[0]);
							armsSlot.get(0)[0] = (Gloves) inventory.get(itemNumber);
						}
					}
					case("Boots"): {
						if(toesSlot.get(0)[0] == null) {
							toesSlot.get(0)[0] = (Boots) inventory.get(itemNumber);
						} else {
							inventory.add(headSlot.get(0)[0]);
							toesSlot.get(0)[0] = (Boots) inventory.get(itemNumber);
						}
					}
				}
			}
		} else {
			return;
		}
	}
	
	public int getHealth() {
		return strength * 18;
	}
	
	public int getMana() {
		return intelligence * 13;
	}
	
	public float getEvasion() {
		return (float) (agility*intelligence*0.1);
	}
	
	public int getArmor() {
		return (int) (agility*0.5);
	}
	
	public int getAttackDamage() {
		return (int) (strength*agility*0.2);
	}
	
	public int getWrath() {
		return (int) (strength*intelligence*0.02);
	}

	@Override
	public String toString() {
		return "Hero [strength=" + strength + ", agility=" + agility + ", intelligence=" + intelligence
				+ ", getHealth()=" + getHealth() + ", getMana()=" + getMana() + ", getEvasion()=" + getEvasion()
				+ ", getArmor()=" + getArmor() + ", getAttackDamage()=" + getAttackDamage() + ", getWrath()="
				+ getWrath() + "]";
	}
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	public void move(Matrix map, int direction) {
		switch(direction){
			case MOVELEFT: { if((loc.getX() > 0) && (map.getCell(loc.getX()-1, loc.getY()) == EMPTY)) {loc = loc.goLeft(1); } break;} 
			case MOVEUP: { if(loc.getY() > 0 && (map.getCell(loc.getX(), loc.getY()-1) == EMPTY)) {loc = loc.goUp(1); } break;} 
			case MOVERIGHT: { if(map.getLength()-1 > loc.getX() && (map.getCell(loc.getX()+1, loc.getY()) == EMPTY)) {loc = loc.goRight(1); } break;}
			case MOVEDOWN: { if(map.getWidth()-1 > loc.getY() && (map.getCell(loc.getX(), loc.getY()+1) == EMPTY)) {loc = loc.goDown(1); } break;} 
		}
			
	}
}
